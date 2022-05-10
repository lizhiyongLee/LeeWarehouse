package com.ils.modules.mes.produce.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.constants.BusinessModuleConstant;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.mapper.WorkOrderMapper;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.system.entity.UserRole;
import com.ils.modules.system.service.UserRoleService;
import com.ils.modules.uflo.annotation.WorkflowAdapter;
import com.ils.modules.uflo.service.AuditOptCallBackService;
import com.ils.modules.uflo.vo.AuditInputParamVO;
import com.ils.modules.uflo.vo.AuditOutputParamVO;
import com.ils.modules.uflo.vo.AuditTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishaojie
 * @description 计划工单审批
 * @date 2021/6/24 16:44
 */
@Service
@WorkflowAdapter(businessType = BusinessModuleConstant.WORK_ORDER_AUDIT, name = "工单审批适配器")
public class WorkOrderAuditServiceImpl implements AuditOptCallBackService {

    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 提交审批回调
     *
     * @param inputParamVO
     * @param outputParamVO
     */
    @Override
    public void startCallBack(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        // 更新审批状态
        updateAuditStatus(inputParamVO, outputParamVO);
        // 发消息
        sendMsg(inputParamVO.getBusinessId(), outputParamVO.getNextAuditUserList());
    }

    @Override
    public void completeCallBack(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        // 更新审批状态
        updateAuditStatus(inputParamVO, outputParamVO);
        // 发消息
        List<String> receiverIds = outputParamVO.getNextAuditUserList();
        //如果当前审批人是最后一个，则不用发消息了
        if (CollectionUtil.isNotEmpty(receiverIds)) {
            receiverIds.add(outputParamVO.getSubmitUser());
            // 发消息
            sendMsg(inputParamVO.getBusinessId(), receiverIds);
        }
    }

    @Override
    public void rejectCallBack(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        // 更新审批状态
        updateAuditStatus(inputParamVO, outputParamVO);
        // 发消息
        List<String> receiverList = new ArrayList<>();
        receiverList.add(outputParamVO.getSubmitUser());
        sendMsg(inputParamVO.getBusinessId(), receiverList);
    }

    @Override
    public void cancelCallBack(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        // 更新审批状态
        updateAuditStatus(inputParamVO, outputParamVO);
    }

    @Override
    public AuditTemplateVO getTemplate(AuditInputParamVO auditVO) {
        return null;
    }

    private void updateAuditStatus(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setId(inputParamVO.getBusinessId());
        workOrder.setAuditStatus(outputParamVO.getAuditStatus());
        workOrder.setFlowId(outputParamVO.getProcessRootId());
        workOrderMapper.updateById(workOrder);
    }

    private void sendMsg(String workOrderId, List<String> receiverIds) {
        WorkOrder workOrder = workOrderMapper.selectById(workOrderId);

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.in("role_id", receiverIds);
        List<UserRole> list = userRoleService.list(userRoleQueryWrapper);
        if (!CommonUtil.isEmptyOrNull(list)) {
            receiverIds.clear();
            list.forEach(userRole -> receiverIds.add(userRole.getUserId()));
        }

        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, workOrder.getCreateBy(), workOrder);
        msgParamsVO.setTenantId(TenantContext.getTenant());
        msgHandleServer.sendMsg(MesCommonConstant.MSG_AUDIT_WORK_ORDER, MesCommonConstant.INFORM, msgParamsVO);
    }
}
