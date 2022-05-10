package com.ils.modules.mes.sop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.util.TenantContext;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import com.ils.modules.mes.base.craft.entity.ProcessParaHead;
import com.ils.modules.mes.base.craft.service.ParaTemplateHeadService;
import com.ils.modules.mes.base.craft.service.ProcessParaHeadService;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;
import com.ils.modules.mes.base.craft.vo.ProcessParaVO;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.service.ReportTemplateService;
import com.ils.modules.mes.base.qc.service.QcMethodService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskMapper;
import com.ils.modules.mes.execution.service.WorkProduceMaterialRecordService;
import com.ils.modules.mes.execution.service.WorkProduceRecordService;
import com.ils.modules.mes.execution.service.WorkProduceTaskParaService;
import com.ils.modules.mes.execution.vo.WorkProduceRecordInfoVO;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import com.ils.modules.mes.material.service.ItemTransferRecordService;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.mapper.QcTaskMapper;
import com.ils.modules.mes.qc.service.QcTaskService;
import com.ils.modules.mes.qc.vo.QcTaskDetailVO;
import com.ils.modules.mes.qc.vo.QcTaskSaveVO;
import com.ils.modules.mes.report.entity.TaskReport;
import com.ils.modules.mes.report.service.TaskReportService;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.entity.SopStep;
import com.ils.modules.mes.sop.mapper.SopStepMapper;
import com.ils.modules.mes.sop.service.SopControlService;
import com.ils.modules.mes.sop.service.SopStepService;
import com.ils.modules.mes.sop.vo.SopControlVO;
import com.ils.modules.mes.sop.vo.SopStepMsgVO;
import com.ils.modules.mes.sop.vo.SopStepVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.system.entity.UserRole;
import com.ils.modules.system.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ils.modules.mes.base.sop.controller.SopTemplateController.EXECUTE_AUTHORITY_ROLE;
import static com.ils.modules.mes.base.sop.controller.SopTemplateController.EXECUTE_AUTHORITY_USER;

/**
 * @Description: 标准作业任务步骤
 * @Author: Conner
 * @Date: 2021-07-16
 * @Version: V1.0
 */
@Service
public class SopStepServiceImpl extends ServiceImpl<SopStepMapper, SopStep> implements SopStepService {

    @Autowired
    private SopControlService sopControlService;
    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;
    @Autowired
    private QcTaskService qcTaskService;
    @Autowired
    private QcTaskMapper qcTaskMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    @Lazy
    private ItemTransferRecordService itemTransferRecordService;
    @Autowired
    @Lazy
    private WorkProduceMaterialRecordService workProduceMaterialRecordService;
    @Autowired
    @Lazy
    private WorkProduceRecordService workProduceRecordService;
    @Autowired
    @Lazy
    private TaskReportService taskReportService;
    @Autowired
    @Lazy
    private WorkProduceTaskParaService workProduceTaskParaService;
    @Autowired
    private MsgHandleServer msgHandleServer;

    @Override
    public void saveSopStep(SopStep sopStep) {
        baseMapper.insert(sopStep);
    }

    @Override
    public void updateSopStep(SopStep sopStep) {
        baseMapper.updateById(sopStep);
    }

    @Override
    public List<SopStepVO> queryByTaskId(String taskId) {
        LoginUser loginUser = CommonUtil.getLoginUser();

        QueryWrapper<UserRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("user_id", loginUser.getId());
        List<UserRole> roleList = userRoleService.list(roleQueryWrapper);

        List<SopStepVO> sopStepVOList = new ArrayList<>();

        //查询步骤
        QueryWrapper<SopStep> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("related_task_id", taskId);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.orderByAsc("step_seq");
        List<SopStep> sopSteps = baseMapper.selectList(queryWrapper);

        //通过步骤查询控件
        for (SopStep sopStep : sopSteps) {
            SopStepVO sopStepVO = new SopStepVO();
            BeanUtil.copyProperties(sopStep, sopStepVO);
            //查询并设置步骤下面的控件名字
            QueryWrapper<SopControl> controlQueryWrapper = new QueryWrapper<>();
            controlQueryWrapper.eq("sop_step_id", sopStep.getId());
            controlQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            controlQueryWrapper.orderByAsc("control_seq");
            List<SopControl> list = sopControlService.list(controlQueryWrapper);
            List<SopControlVO> lstSopControlVO = this.getLstSopControlVO(list, taskId);

            sopStepVO.setSopControlList(lstSopControlVO);
            sopStepVO.setExecute(ZeroOrOneEnum.ZERO.getStrCode());
            //设置权限
            String executeAuthority = sopStep.getExecuteAuthority();
            if (EXECUTE_AUTHORITY_USER.equals(executeAuthority)) {
                if (loginUser.getId().equals(sopStep.getExecuter())) {
                    sopStepVO.setExecute(ZeroOrOneEnum.ONE.getStrCode());
                }
            } else if (EXECUTE_AUTHORITY_ROLE.equals(executeAuthority)) {
                List<String> roleIds = roleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
                if (roleIds.contains(sopStep.getExecuter())) {
                    sopStepVO.setExecute(ZeroOrOneEnum.ONE.getStrCode());
                }
            }
            sopStepVOList.add(sopStepVO);
        }
        return sopStepVOList;
    }

    /**
     * 添加控件执行记录
     *
     * @param list
     * @param taskId
     * @return
     */
    private List<SopControlVO> getLstSopControlVO(List<SopControl> list, String taskId) {
        List<SopControlVO> lstSopControlVO = new ArrayList<>();
        for (SopControl sopControl : list) {
            SopControlVO sopControlVO = new SopControlVO();
            BeanUtil.copyProperties(sopControl, sopControlVO);
            //1、标签入库；
            if (SopControlTypeEnum.LABEL_IN.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<ItemTransferRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                List<ItemTransferRecord> tagInStorageRecordList = itemTransferRecordService.list(queryWrapper);
                sopControlVO.setTagInStorageRecordList(tagInStorageRecordList);
            }
            //2、库存入库；
            if (SopControlTypeEnum.STORAGE_IN.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<ItemTransferRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                List<ItemTransferRecord> stockInStorageRecordList = itemTransferRecordService.list(queryWrapper);
                sopControlVO.setStockInStorageRecordList(stockInStorageRecordList);
            }
            //3、投料；
            if (SopControlTypeEnum.FEED.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<WorkProduceMaterialRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                List<WorkProduceMaterialRecord> workProduceMaterialRecordList = workProduceMaterialRecordService.list(queryWrapper);
                sopControlVO.setWorkProduceMaterialRecordList(workProduceMaterialRecordList);
            }
            // 4、产出；
            if (SopControlTypeEnum.OUTPUT.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                List<WorkProduceRecord> workProduceRecordList = workProduceRecordService.list(queryWrapper);
                List<WorkProduceRecordInfoVO> workProduceRecordInfoVOList = new ArrayList<>();
                workProduceRecordList.forEach(workProduceRecord -> {
                    WorkProduceRecordInfoVO workProduceRecordInfoVO = new WorkProduceRecordInfoVO();
                    BeanUtil.copyProperties(workProduceRecord, workProduceRecordInfoVO);
                    workProduceRecordInfoVOList.add(workProduceRecordInfoVO);
                });
                sopControlVO.setWorkProduceRecordList(workProduceRecordInfoVOList);
            }
            // 5、质检；
            if (SopControlTypeEnum.QC.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<QcTask> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                QcTask one = qcTaskService.getOne(queryWrapper);
                if (null != one) {
                    QcTaskDetailVO qcTaskDetailVO = qcTaskService.queryQcTaskDetail(one.getId());
                    sopControlVO.setQcTaskDetailVO(qcTaskDetailVO);
                }
            }
            // 6、标签出库；
            if (SopControlTypeEnum.LABEL_OUT.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<ItemTransferRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                List<ItemTransferRecord> tagInStorageRecordList = itemTransferRecordService.list(queryWrapper);
                sopControlVO.setTagInStorageRecordList(tagInStorageRecordList);
            }
            // 7、库存出库、
            if (SopControlTypeEnum.STORAGE_OUT.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<ItemTransferRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                List<ItemTransferRecord> tagInStorageRecordList = itemTransferRecordService.list(queryWrapper);
                sopControlVO.setTagInStorageRecordList(tagInStorageRecordList);
            }
            // 8、报告模板。
            if (SopControlTypeEnum.REPORT_TEMPLATE.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<TaskReport> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sop_control_id", sopControl.getId());
                TaskReport taskReport = taskReportService.getOne(queryWrapper);
                sopControlVO.setTaskReport(taskReport);
            }
            // 9、联产出。
            if (SopControlTypeEnum.JOINT_PRODUCT.getValue().equals(sopControl.getControlType())) {
                setJoinProduct(sopControl, sopControlVO);
            }
            // 10、参数模板。
            if (SopControlTypeEnum.PARA_TEMPLATE.getValue().equals(sopControl.getControlType())) {
                QueryWrapper<WorkProduceTaskPara> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("produce_task_id", taskId);
                List<WorkProduceTaskPara> workProduceTaskParaList = workProduceTaskParaService.list(queryWrapper);
                sopControlVO.setWorkProduceTaskParaList(workProduceTaskParaList);
            }
            lstSopControlVO.add(sopControlVO);
        }
        return lstSopControlVO;
    }

    /**
     * 控件执行记录：联产出记录查询
     *
     * @param sopControl
     * @param sopControlVO
     */
    private void setJoinProduct(SopControl sopControl, SopControlVO sopControlVO) {
        QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sop_control_id", sopControl.getId());
        List<WorkProduceRecord> workProduceRecordList = workProduceRecordService.list(queryWrapper);
        //根据单个物料进行分组和统计
        Map<String, WorkProduceRecordInfoVO> mergeWorkProduceRecordMap = new HashMap<>(8);
        for (WorkProduceRecord workProduceRecord : workProduceRecordList) {
            WorkProduceRecordInfoVO mergeWorkProduceRecordInfoVO = mergeWorkProduceRecordMap.get(workProduceRecord.getItemCode());
            if (null == mergeWorkProduceRecordInfoVO) {
                WorkProduceRecordInfoVO workProduceRecordInfoVO = new WorkProduceRecordInfoVO();
                BeanUtil.copyProperties(workProduceRecord, workProduceRecordInfoVO);
                workProduceRecordInfoVO.setDataSize(1);
                mergeWorkProduceRecordMap.put(workProduceRecord.getItemCode(), workProduceRecordInfoVO);
            } else {
                mergeWorkProduceRecordInfoVO.setSubmitQty(mergeWorkProduceRecordInfoVO.getSubmitQty().add(workProduceRecord.getSubmitQty()));
                mergeWorkProduceRecordInfoVO.setDataSize(mergeWorkProduceRecordInfoVO.getDataSize() + 1);
            }
        }
        List<WorkProduceRecordInfoVO> workProduceRecordInfoVOList = new ArrayList<>(mergeWorkProduceRecordMap.values());
        sopControlVO.setWorkProduceRecordList(workProduceRecordInfoVOList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void completeTask(String id) {
        SopStep sopStep = baseMapper.selectById(id);
        //判断要完成的步骤是否是可进行下一步的步骤
        if (!SopStepEnum.IN_PROGRESS.getValue().equals(sopStep.getStepStatus())) {
            throw new ILSBootException("P-SOP-0091");
        }
        //更新任务的当前节点状态和下一步状态
        sopStep.setStepStatus(SopStepEnum.COMPLETE.getValue());
        baseMapper.updateById(sopStep);
        if (ZeroOrOneEnum.ONE.getStrCode().equals(sopStep.getLastStep())) {
            return;
        }

        SopStep nexSopStep = baseMapper.selectById(sopStep.getNextStepId());
        nexSopStep.setStepStatus(SopStepEnum.IN_PROGRESS.getValue());
        baseMapper.updateById(nexSopStep);

        sopQcTaskCreateByStepId(nexSopStep.getId());

        sendMsg(nexSopStep);
    }

    @Override
    public void sopQcTaskCreateByStepId(String stepId) {

        //通过步骤id查询对应的质检控件
        QueryWrapper<SopControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("control_type", SopControlTypeEnum.QC.getValue());
        queryWrapper.eq("sop_step_id", stepId);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());

        List<SopControl> list = sopControlService.list(queryWrapper);
        //如果质检控件为空，直接返回
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        //构建对应的质检任务信息
        for (SopControl sopControl : list) {
            String qcTaskId = buildQcTask(sopControl);

            //更新控件实体为质检任务的id
            sopControl.setEntityItem(qcTaskId);
            sopControlService.updateById(sopControl);
        }
    }

    @Autowired
    private QcMethodService qcMethodService;

    /**
     * 判断下个步骤是否有质检控件，如果有则自动生成对应的质检任务
     *
     * @param sopControl
     */
    private String buildQcTask(SopControl sopControl) {
        //查询控件对应的生产任务
        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(sopControl.getRelatedTaskId());

        QcTaskSaveVO qcTaskSaveVO = new QcTaskSaveVO();
        qcTaskSaveVO.setQcType(sopControl.getControlLogic());

        qcTaskSaveVO.setMethodId(sopControl.getEntityItem());
        qcTaskSaveVO.setMethodName(qcMethodService.getById(sopControl.getEntityItem()).getMethodName());
        qcTaskSaveVO.setLocationType("2");
        qcTaskSaveVO.setLocationCode(workProduceTask.getStationCode());
        qcTaskSaveVO.setLocationId(workProduceTask.getStationId());
        qcTaskSaveVO.setLocationName(workProduceTask.getStationName());

        qcTaskSaveVO.setItemCode(workProduceTask.getItemCode());
        qcTaskSaveVO.setItemId(workProduceTask.getItemId());
        qcTaskSaveVO.setItemName(workProduceTask.getItemName());
        qcTaskSaveVO.setRelatedReceiptType("1");

        //判断模板步骤的执行权限字段是角色还是用户，如果是用户，则直接指定该用户，如果是角色，则不指定，让用户可以去领取
        SopStep sopStep = baseMapper.selectById(sopControl.getSopStepId());
        if (EXECUTE_AUTHORITY_USER.equals(sopStep.getExecuteAuthority())) {
            qcTaskSaveVO.setTaskEmployeeIds(sopStep.getExecuter());
        } else {
            //查询该角色下面的所有人
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", sopStep.getExecuter());
            List<UserRole> list = userRoleService.list(queryWrapper);
            String employees = list.stream().map(UserRole::getUserId).collect(Collectors.joining(","));
            qcTaskSaveVO.setTaskEmployeeIds(employees);
        }

        //设置管理步骤id和控件id
        qcTaskSaveVO.setSopControlId(sopControl.getId());
        qcTaskSaveVO.setSopStepId(sopControl.getSopStepId());

        //设置相关生产任务的信息
        qcTaskSaveVO.setRelatedReceiptId(sopControl.getRelatedTaskId());
        qcTaskSaveVO.setBatchNo(workProduceTask.getBatchNo());
        qcTaskSaveVO.setOrderId(workProduceTask.getOrderId());
        qcTaskSaveVO.setOrderNo(workProduceTask.getOrderNo());
        qcTaskSaveVO.setProcessId(workProduceTask.getProcessId());
        qcTaskSaveVO.setProcessCode(workProduceTask.getProcessCode());
        qcTaskSaveVO.setProcessName(workProduceTask.getProcessName());
        qcTaskService.saveQcTask(qcTaskSaveVO);
        return qcTaskSaveVO.getId();
    }

    private void sendMsg(SopStep sopStep) {
        String executeAuthority = sopStep.getExecuteAuthority();
        String executor = sopStep.getExecuter();
        List<String> receiverIds = new ArrayList<>();
        if (EXECUTE_AUTHORITY_USER.equals(executeAuthority)) {
            receiverIds.add(executor);
        } else {
            QueryWrapper<UserRole> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.eq("role_id", executor);
            List<UserRole> userList = userRoleService.list(roleQueryWrapper);
            userList.forEach(userRole -> receiverIds.add(userRole.getUserId()));

        }
        LoginUser loginUser = CommonUtil.getLoginUser();
        SopStepMsgVO sopStepMsgVO = new SopStepMsgVO();
        sopStepMsgVO.setStepName(sopStep.getStepName());
        if (RelatedTaskTypeEnum.PRODUCE.getValue().equals(sopStep.getRelatedTaskType())) {
            WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(sopStep.getRelatedTaskId());
            sopStepMsgVO.setRelatedTaskType(RelatedTaskTypeEnum.PRODUCE.getDesc());
            sopStepMsgVO.setProcessName(workProduceTask.getProcessName());
            sopStepMsgVO.setWorkstation(workProduceTask.getStationName());
            sopStepMsgVO.setTaskCode(workProduceTask.getTaskCode());
        }
        if (RelatedTaskTypeEnum.QC.getValue().equals(sopStep.getRelatedTaskType())) {
            QcTask qcTask = qcTaskMapper.selectById(sopStep.getRelatedTaskId());
            sopStepMsgVO.setRelatedTaskType(RelatedTaskTypeEnum.QC.getDesc());
            sopStepMsgVO.setProcessName(qcTask.getProcessName());
            sopStepMsgVO.setWorkstation(qcTask.getLocationName());
            sopStepMsgVO.setTaskCode(qcTask.getTaskCode());
        }
        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, loginUser.getRealname(), sopStepMsgVO);
        msgParamsVO.setTenantId(TenantContext.getTenant());
        msgHandleServer.sendMsg(MesCommonConstant.MSG_SOP_STEP, MesCommonConstant.INFORM, msgParamsVO);
    }
}
