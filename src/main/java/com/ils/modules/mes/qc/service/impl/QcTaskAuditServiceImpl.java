package com.ils.modules.mes.qc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.constants.BusinessModuleConstant;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.AuditStatusEnum;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.mapper.QcTaskMapper;
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
 * @description 质检任务审批
 * @date 2021/6/24 16:44
 */
@Service
@WorkflowAdapter(businessType = BusinessModuleConstant.QC_TASK_AUDIT, name = "质检任务审批适配器")
public class QcTaskAuditServiceImpl implements AuditOptCallBackService {

    @Autowired
    private QcTaskMapper qcTaskMapper;
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

    /**
     * 完成审批回调
     *
     * @param inputParamVO
     * @param outputParamVO
     */
    @Override
    public void completeCallBack(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        // 更新审批状态
        updateAuditStatus(inputParamVO, outputParamVO);
        List<String> receiverIds = outputParamVO.getNextAuditUserList();
        //如果当前审批人是最后一个，则不用发消息了
        if (CollectionUtil.isNotEmpty(receiverIds)) {
            receiverIds.add(outputParamVO.getSubmitUser());
            // 发消息
            sendMsg(inputParamVO.getBusinessId(), receiverIds);
        }
    }

    /**
     * 拒绝审批回调
     *
     * @param inputParamVO
     * @param outputParamVO
     */
    @Override
    public void rejectCallBack(AuditInputParamVO inputParamVO, AuditOutputParamVO outputParamVO) {
        // 更新审批状态
        updateAuditStatus(inputParamVO, outputParamVO);
        // 发消息
        List<String> receiverList = new ArrayList<>();
        receiverList.add(outputParamVO.getSubmitUser());
        sendMsg(inputParamVO.getBusinessId(), receiverList);
    }

    /**
     * 取消审批回调
     *
     * @param inputParamVO
     * @param outputParamVO
     */
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
        QcTask qcTask = new QcTask();
        qcTask.setId(inputParamVO.getBusinessId());
        qcTask.setAuditStatus(outputParamVO.getAuditStatus());
        qcTask.setFlowId(outputParamVO.getProcessRootId());
        qcTaskMapper.updateById(qcTask);
    }

    private void sendMsg(String businessId, List<String> receiverIds) {
        QcTask qcTask = qcTaskMapper.selectById(businessId);

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.in("role_id", receiverIds);
        List<UserRole> list = userRoleService.list(userRoleQueryWrapper);
        if (!CommonUtil.isEmptyOrNull(list)) {
            receiverIds.clear();
            list.forEach(userRole -> receiverIds.add(userRole.getUserId()));
        }

        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, qcTask.getCreateBy(), qcTask);
        msgParamsVO.setTenantId(TenantContext.getTenant());
        msgHandleServer.sendMsg(MesCommonConstant.MSG_AUDIT_QC_TASK, MesCommonConstant.INFORM, msgParamsVO);
    }
}
