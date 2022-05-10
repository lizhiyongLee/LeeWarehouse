package com.ils.modules.mes.execution.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.util.TenantContext;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.service.QcMethodService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.entity.WorkProduceTaskEmployee;
import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import com.ils.modules.mes.execution.mapper.*;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.execution.vo.*;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.mapper.QcTaskMapper;
import com.ils.modules.mes.sop.entity.SopStep;
import com.ils.modules.mes.sop.service.SopStepService;
import com.ils.modules.mes.sop.vo.SopStepMsgVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.entity.UserRole;
import com.ils.modules.system.service.UserRoleService;
import com.ils.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ils.modules.mes.base.sop.controller.SopTemplateController.EXECUTE_AUTHORITY_USER;

/**
 * @Description: 执行生产任务
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
@Service
public class WorkProduceTaskServiceImpl extends ServiceImpl<WorkProduceTaskMapper, WorkProduceTask> implements WorkProduceTaskService {

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private WorkProduceTaskEmployeeMapper workProduceTaskEmployeeMapper;

    @Autowired
    @Lazy
    private WorkPlanTaskService workPlanTaskService;

    @Autowired
    private UserService sysUserService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private WorkProcessTaskService workProcessTaskService;
    @Autowired
    private WorkProduceTaskParaMapper workProduceTaskParaMapper;
    @Autowired
    private WorkProduceMaterialRecordMapper workProduceMaterialRecordMapper;
    @Autowired
    private SopStepService sopStepService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;
    @Autowired
    private QcTaskMapper qcTaskMapper;
    @Autowired
    private WorkProduceRecordMapper workProduceRecordMapper;
    @Autowired
    private WorkShopService workShopService;
    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private QcMethodService qcMethodService;

    @Override
    public void saveWorkProduceTask(WorkProduceTask workProduceTask) {
        baseMapper.insert(workProduceTask);
    }

    @Override
    public void updateWorkProduceTask(WorkProduceTask workProduceTask) {
        baseMapper.updateById(workProduceTask);
    }

    @Override
    public void delWorkProduceTask(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchWorkProduceTask(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public boolean deleteByPlanTaskId(String planTaskId) {
        return baseMapper.deleteByPlanTaskId(planTaskId);
    }

    @Override
    public WorkProduceTaskInfoVO getWorkProduceTaskInfoById(String id) {
        WorkProduceTaskInfoVO workProduceTaskInfoVO = baseMapper.getProduceTaskInfoById(id);
        QueryWrapper<WorkProduceTaskEmployee> queryWrapper = new QueryWrapper();
        queryWrapper.eq("excute_task_id", id);
        queryWrapper.in("using_type", Arrays.asList(ProduceTaskEmployeeUsingEnum.SELF.getValue(),
                ProduceTaskEmployeeUsingEnum.ASSIGN.getValue()));
        LoginUser user = CommonUtil.getLoginUser();
        queryWrapper.eq("employee_id", user.getId());
        List<WorkProduceTaskEmployee> lstWorkProduceTaskEmployee =
                workProduceTaskEmployeeMapper.selectList(queryWrapper);
        if (!CommonUtil.isEmptyOrNull(lstWorkProduceTaskEmployee)) {
            WorkProduceTaskEmployee entity = lstWorkProduceTaskEmployee.get(0);
            workProduceTaskInfoVO.setUsingType(entity.getUsingType());
            workProduceTaskInfoVO.setEmployeeCode(entity.getEmployeeCode());
            workProduceTaskInfoVO.setEmployeeName(entity.getEmployeeName());
            workProduceTaskInfoVO.setEmployeeId(entity.getEmployeeId());
        }
        Item item = itemService.getById(workProduceTaskInfoVO.getItemId());
        workProduceTaskInfoVO.setBatchFlag(item.getBatch());
        workProduceTaskInfoVO.setQrcodeFlag(item.getQrcode());

        WorkOrder workOrder = workOrderService.getById(workProduceTaskInfoVO.getOrderId());
        workProduceTaskInfoVO.setWorkFlowType(workOrder.getWorkflowType());
        workProduceTaskInfoVO.setQcStatus(queryIfHasQcMethodRelateWorkProduce(id));
        //参数模板
        QueryWrapper<WorkProduceTaskPara> workProduceTaskParaQueryWrapper = new QueryWrapper<>();
        workProduceTaskParaQueryWrapper.eq("produce_task_id", id);
        List<WorkProduceTaskPara> workProduceTaskParaList = workProduceTaskParaMapper.selectList(workProduceTaskParaQueryWrapper);
        workProduceTaskInfoVO.setWorkProduceTaskParaList(workProduceTaskParaList);
        return workProduceTaskInfoVO;
    }

    /**
     * 查询是否有对应的质检方案
     *
     * @param id
     * @return
     */
    private String queryIfHasQcMethodRelateWorkProduce(String id) {
        List<QcMethod> qcMethodList = qcMethodService.queryQcMeThodByItemIdAndQcType(QcTaskQcTypeEnum.PRODUCTION_CHECK.getValue(), id);
        if (CollectionUtil.isNotEmpty(qcMethodList)) {
            return ItemQcStatusEnum.WAIT_TEST.getDesc();
        }
        return ItemQcStatusEnum.QUALIFIED.getDesc();
    }

    @Override
    public void signWorkProduceTask(String id) {
        QueryWrapper<WorkProduceTaskEmployee> queryWrapper = new QueryWrapper();
        queryWrapper.eq("excute_task_id", id);
        LoginUser user = CommonUtil.getLoginUser();
        queryWrapper.eq("employee_id", user.getId());
        List<WorkProduceTaskEmployee> lstWorkProduceTaskEmployee =
                workProduceTaskEmployeeMapper.selectList(queryWrapper);
        if (CommonUtil.isEmptyOrNull(lstWorkProduceTaskEmployee)) {
            WorkProduceTaskEmployee entity = new WorkProduceTaskEmployee();
            entity.setExcuteTaskId(id);
            entity.setUsingType(ProduceTaskEmployeeUsingEnum.SELF.getValue());
            entity.setEmployeeId(user.getId());
            entity.setEmployeeName(user.getRealname());
            workProduceTaskEmployeeMapper.insert(entity);
        } else {
            WorkProduceTaskEmployee entity = lstWorkProduceTaskEmployee.get(0);
            if (ProduceTaskEmployeeUsingEnum.SUSPEND.getValue().equals(entity.getUsingType())) {
                entity.setUsingType(ProduceTaskEmployeeUsingEnum.SELF.getValue());
                workProduceTaskEmployeeMapper.updateById(entity);
            } else {
                throw new ILSBootException("P-OW-0012");
            }
        }
    }

    @Override
    public void suspendWorkProduceTask(String id) {
        WorkProduceTask workProduceTask = this.getById(id);
        boolean isExeStatus = PlanTaskExeStatusEnum.NOT_START.getValue().equals(workProduceTask.getExeStatus())
                || PlanTaskExeStatusEnum.PRODUCE.getValue().equals(workProduceTask.getExeStatus())
                || PlanTaskExeStatusEnum.SUSPEND.getValue().equals(workProduceTask.getExeStatus());
        if (!isExeStatus) {
            throw new ILSBootException("P-OW-0013");
        }
        QueryWrapper<WorkProduceTaskEmployee> queryWrapper = new QueryWrapper();
        queryWrapper.eq("excute_task_id", id);
        LoginUser user = CommonUtil.getLoginUser();
        queryWrapper.eq("employee_id", user.getId());
        List<WorkProduceTaskEmployee> lstWorkProduceTaskEmployee =
                workProduceTaskEmployeeMapper.selectList(queryWrapper);
        if (!CommonUtil.isEmptyOrNull(lstWorkProduceTaskEmployee)) {
            WorkProduceTaskEmployee entity = lstWorkProduceTaskEmployee.get(0);
            if (ProduceTaskEmployeeUsingEnum.SELF.getValue().equals(entity.getUsingType())) {
                entity.setUsingType(ProduceTaskEmployeeUsingEnum.SUSPEND.getValue());
                workProduceTaskEmployeeMapper.updateById(entity);
            } else {
                throw new ILSBootException("P-OW-0012");
            }
        } else {
            throw new ILSBootException("P-OW-0012");
        }

        // 更新交接任务
        if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(workProduceTask.getExeStatus())) {
            QueryWrapper<WorkProduceTask> updateWorkProduceTaskQueryWrapper = new QueryWrapper<>();
            WorkProduceTask updateWorkProduceTask = new WorkProduceTask();
            updateWorkProduceTaskQueryWrapper.eq("id", workProduceTask.getId());
            updateWorkProduceTask.setExeStatus(PlanTaskExeStatusEnum.SUSPEND.getValue());
            baseMapper.update(updateWorkProduceTask, updateWorkProduceTaskQueryWrapper);
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateProduceTaskStatus(UpdateParamVO updateParamsVO) {
        WorkProduceTask oldWorkProduceTask = this.getById(updateParamsVO.getId());
        String updateExeStatus = updateParamsVO.getExeStatus();

        boolean isUpdateCondition = false;

        // 未开始状态可以更新为执行中、或者已经取消
        if (PlanTaskExeStatusEnum.NOT_START.getValue().equals(oldWorkProduceTask.getExeStatus())) {
            this.updateSopStep(updateParamsVO, oldWorkProduceTask, updateExeStatus);
            if (PlanTaskExeStatusEnum.CANCEL.getValue().equals(updateExeStatus)
                    || PlanTaskExeStatusEnum.PRODUCE.getValue().equals(updateExeStatus)) {
                isUpdateCondition = true;
            }
            // 执行状态可以更新为暂停、或者已经结束
        } else if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(oldWorkProduceTask.getExeStatus())) {
            if (PlanTaskExeStatusEnum.SUSPEND.getValue().equals(updateExeStatus)
                    || PlanTaskExeStatusEnum.FINISH.getValue().equals(updateExeStatus)) {
                isUpdateCondition = true;
            }
            // 暂停状态可以更新为执行中、或者已经结束
        } else if (PlanTaskExeStatusEnum.SUSPEND.getValue().equals(oldWorkProduceTask.getExeStatus())) {
            if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(updateExeStatus)
                    || PlanTaskExeStatusEnum.FINISH.getValue().equals(updateExeStatus)) {
                isUpdateCondition = true;
            }
        }

        if (isUpdateCondition) {
            this.updateStatus(oldWorkProduceTask, updateExeStatus);
        } else {
            throw new ILSBootException("P-OW-0014");
        }

    }

    /**
     * 更新生产任务、工单、执行任务的状态
     *
     * @param oldWorkProduceTask
     * @param updateExeStatus
     */
    private void updateStatus(WorkProduceTask oldWorkProduceTask, String updateExeStatus) {
        // 更新执行任务状态
        QueryWrapper<WorkProduceTask> produceTaskQueryWrapper = new QueryWrapper<>();
        WorkProduceTask updateProduceTask = new WorkProduceTask();
        produceTaskQueryWrapper.eq("id", oldWorkProduceTask.getId());
        if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(updateExeStatus)) {
            updateProduceTask.setRealStartTime(new Date());
        }
        if (PlanTaskExeStatusEnum.FINISH.getValue().equals(updateExeStatus)) {
            updateProduceTask.setRealEndTime(new Date());
        }
        updateProduceTask.setExeStatus(updateExeStatus);
        this.update(updateProduceTask, produceTaskQueryWrapper);

        //更新工单状态:已下发改为开工
        WorkOrder checkWorkOrder = workOrderService.getById(oldWorkProduceTask.getOrderId());
        if (WorkOrderStatusEnum.ISSUED.getValue().equals(checkWorkOrder.getStatus())) {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setId(checkWorkOrder.getId());
            workOrder.setRealStartTime(new Date());
            workOrder.setStatus(WorkOrderStatusEnum.STARTED.getValue());
            workOrderService.updateById(workOrder);
        }

        // 更新派工任务状态
        QueryWrapper<WorkPlanTask> planTaskQueryWrapper = new QueryWrapper<>();
        WorkPlanTask updatePlanTask = new WorkPlanTask();
        planTaskQueryWrapper.eq("id", oldWorkProduceTask.getPlanTaskId());
        if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(updateExeStatus)) {
            updatePlanTask.setRealStartTime(new Date());
        }
        if (PlanTaskExeStatusEnum.FINISH.getValue().equals(updateExeStatus)) {
            updatePlanTask.setRealEndTime(new Date());
        }
        updatePlanTask.setExeStatus(updateExeStatus);
        workPlanTaskService.update(updatePlanTask, planTaskQueryWrapper);

        if (PlanTaskExeStatusEnum.CANCEL.getValue().equals(updateExeStatus)) {
            WorkPlanTask workPlanTask = workPlanTaskService.getById(oldWorkProduceTask.getPlanTaskId());
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());

            QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
            WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
            queryWrapper.eq("id", workProcessTask.getId());
            updateWorkProcessTask.setScheduledQty(
                    BigDecimalUtils.subtract(workProcessTask.getScheduledQty(), oldWorkProduceTask.getPlanQty()));
            updateWorkProcessTask.setPublishQty(
                    BigDecimalUtils.subtract(workProcessTask.getPublishQty(), oldWorkProduceTask.getPlanQty()));
            workProcessTaskService.update(updateWorkProcessTask, queryWrapper);
        }
    }

    /**
     * 更新标准作业流程
     *
     * @param updateParamsVO
     * @param oldWorkProduceTask
     * @param updateExeStatus
     */
    private void updateSopStep(UpdateParamVO updateParamsVO, WorkProduceTask oldWorkProduceTask, String updateExeStatus) {
        if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(updateExeStatus)
                && ZeroOrOneEnum.ONE.getStrCode().equals(oldWorkProduceTask.getSop())) {
            QueryWrapper<SopStep> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("related_task_id", updateParamsVO.getId());
            queryWrapper.eq("first_step", ZeroOrOneEnum.ONE.getStrCode());
            queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getStrCode());
            SopStep sopStep = new SopStep();
            sopStep.setStepStatus(SopStepEnum.IN_PROGRESS.getValue());
            SopStep one = sopStepService.getOne(queryWrapper);
            sopStepService.update(sopStep, queryWrapper);
            sopStepService.sopQcTaskCreateByStepId(one.getId());

            this.sendMsg(one);
        }
    }

    @Override
    public void updateProduceTaskTemplate(String taskId, String templateId) {
        WorkProduceTask workProduceTask = this.getById(taskId);
        if (!PlanTaskExeStatusEnum.PRODUCE.getValue().equals(workProduceTask.getExeStatus())) {
            throw new ILSBootException("P-OW-0018");
        }
        WorkProduceTask updateWorkProduceTask = new WorkProduceTask();
        updateWorkProduceTask.setId(taskId);
        updateWorkProduceTask.setTemplateId(templateId);
        this.updateById(updateWorkProduceTask);
    }

    @Override
    public IPage<WorkProduceTask> doneList(Page<WorkProduceTask> page, String userId, String positionName,
                                           QueryWrapper<WorkProduceTask> queryWrapper) {

        if (StringUtils.isNotBlank(positionName)) {
            QueryWrapper<NodeVO> nodeQueryWrapper = new QueryWrapper<>();
            nodeQueryWrapper.eq("a.tenant_id", CommonUtil.getTenantId());
            List<NodeVO> lstNodeVO = workShopService.queryInstitutionList(nodeQueryWrapper);
            List<String> stationNameList = getStationNameList(Arrays.asList(positionName.split(",")), lstNodeVO);
            if (CommonUtil.isEmptyOrNull(stationNameList)) {
                queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ZERO.getStrCode());
            } else {
                queryWrapper.in("station_name", stationNameList);
            }
        }
        return baseMapper.doneList(page, userId, queryWrapper);
    }

    @Override
    public List<String> getStationNameList(List<String> positionNameList, List<NodeVO> lstNodeVO) {

        List<String> subLocationNameList = new ArrayList<>();

        for (String positionName : positionNameList) {
            for (NodeVO nodeVO : lstNodeVO) {
                if (nodeVO.getName().equals(positionName)) {
                    //工位级，直接返回
                    if (NodeTypeEnum.WORKSTATION.getValue().equals(nodeVO.getNodeType())) {
                        return positionNameList;
                    } else {
                        //非工位级，找到下级所有工位
                        lstNodeVO.forEach(checkNodeVO -> {
                            if (checkNodeVO.getUpArea().equals(nodeVO.getId())) {
                                subLocationNameList.add(checkNodeVO.getName());
                            }
                        });
                    }
                }
            }
        }
        if (CommonUtil.isEmptyOrNull(subLocationNameList)) {
            return subLocationNameList;
        }
        return getStationNameList(subLocationNameList, lstNodeVO);
    }


    @Override
    public IPage<WorkProduceTask> todoList(Page<WorkProduceTask> page, String userId,
                                           QueryWrapper<WorkProduceTask> queryWrapper) {
        return baseMapper.todoList(page, userId, queryWrapper);
    }

    @Override
    public IPage<WorkProduceTaskQueryVO> queryProduceTaskList(Page<WorkProduceTaskQueryVO> page,
                                                              QueryWrapper<WorkProduceTaskQueryVO> queryWrapper) {
        return baseMapper.queryProduceTaskList(page, queryWrapper);
    }

    @Override
    public WorkProduceTaskDetailVO queryDetailById(String id) {
        WorkProduceTask workProduceTask = this.getById(id);
        WorkProduceTaskDetailVO workProduceTaskDetailVO = new WorkProduceTaskDetailVO();
        BeanUtils.copyProperties(workProduceTask, workProduceTaskDetailVO);

        QueryWrapper<WorkProduceTaskEmployee> queryWrapper = new QueryWrapper();
        queryWrapper.eq("excute_task_id", id);
        queryWrapper.in("using_type", Arrays.asList(ProduceTaskEmployeeUsingEnum.SELF.getValue(),
                ProduceTaskEmployeeUsingEnum.ASSIGN.getValue()));
        List<WorkProduceTaskEmployee> lstWorkProduceTaskEmployee =
                workProduceTaskEmployeeMapper.selectList(queryWrapper);
        workProduceTaskDetailVO.setEmployeeIds(
                lstWorkProduceTaskEmployee.stream().map(item -> item.getEmployeeId()).collect(Collectors.joining(",")));
        workProduceTaskDetailVO.setEmployeeNames(
                lstWorkProduceTaskEmployee.stream().map(item -> item.getEmployeeName()).collect(Collectors.joining(",")));

        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        workProduceTaskDetailVO.setBomId(workOrder.getBomId());
        workProduceTaskDetailVO.setProductId(workOrder.getProductId());
        workProduceTaskDetailVO.setRouteId(workOrder.getRouteId());
        workProduceTaskDetailVO.setStatus(workOrder.getStatus());
        workProduceTaskDetailVO.setWorkOrderPlanEndTime(workOrder.getPlanEndTime());
        workProduceTaskDetailVO.setWorkOrderPlanStartTime(workOrder.getPlanStartTime());
        workProduceTaskDetailVO.setWorkflowType(workOrder.getWorkflowType());
        Page<GroupMaterialRecordVO> page = new Page<GroupMaterialRecordVO>(1, Integer.MAX_VALUE);
        IPage<GroupMaterialRecordVO> pageList = null;
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workOrder.getWorkflowType())) {
            QueryWrapper<GroupMaterialRecordVO> queryMaterialWrapper = new QueryWrapper();
            queryMaterialWrapper.eq("c .order_id", workOrder.getId());
            queryMaterialWrapper.eq("c .seq", workProduceTask.getSeq());
            queryMaterialWrapper.eq("c .process_id", workProduceTask.getProcessId());

            pageList =
                    workProduceMaterialRecordMapper.queryGroupProductBomMaterialPageList(workProduceTask.getId(), page, queryMaterialWrapper, null);

            // 工序任务
            WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());

            // bom 方式下 本次生成任务数量：工序任务数量=本次物料最大用量数量：工序上物料用量总量
            List<GroupMaterialRecordVO> lstGroupMaterialRecord = pageList.getRecords();
            if (!CommonUtil.isEmptyOrNull(lstGroupMaterialRecord)) {
                for (GroupMaterialRecordVO groupMaterialRecordVO : lstGroupMaterialRecord) {
                    BigDecimal materialTotalQty = BigDecimalUtils.divide(
                            BigDecimalUtils.multiply(groupMaterialRecordVO.getProcessTotalQty(),
                                    workProduceTask.getPlanQty()),
                            workProcessTask.getPlanQty(), 6);
                    groupMaterialRecordVO.setProcessTotalQty(materialTotalQty);
                }
            }
            workProduceTaskDetailVO.setLstGroupMaterialRecordVO(lstGroupMaterialRecord);
        } else {
            pageList = workProduceMaterialRecordMapper.queryGroupMaterialRecordPageList(id, null, page);
            workProduceTaskDetailVO.setLstGroupMaterialRecordVO(pageList.getRecords());
        }
        WorkProduceTaskInfoVO workProduceTaskInfoVO = baseMapper.getProduceTaskInfoById(id);
        List<WorkProduceTaskInfoVO> lstWorkProduceTaskInfoVO = new ArrayList(1);
        lstWorkProduceTaskInfoVO.add(workProduceTaskInfoVO);
        workProduceTaskDetailVO.setLstWorkProduceTaskInfoVO(lstWorkProduceTaskInfoVO);

        //联产出记录
        workProduceTaskDetailVO.setItemCellList(this.getJoinRecordList(id));

        //参数模板
        QueryWrapper<WorkProduceTaskPara> workProduceTaskParaQueryWrapper = new QueryWrapper<>();
        workProduceTaskParaQueryWrapper.eq("produce_task_id", id);
        List<WorkProduceTaskPara> workProduceTaskParaList = workProduceTaskParaMapper.selectList(workProduceTaskParaQueryWrapper);
        workProduceTaskDetailVO.setWorkProduceTaskParaList(workProduceTaskParaList);
        return workProduceTaskDetailVO;
    }

    /**
     * 联产出记录合并为物料单元格式进行展示
     *
     * @param id 生产任务id
     * @return
     */
    private List<ItemCell> getJoinRecordList(String id) {
        QueryWrapper<WorkProduceRecord> workProduceRecordQueryWrapper = new QueryWrapper<>();
        workProduceRecordQueryWrapper.eq("product_type", RecordProductTypeEnum.JOINT_PRODUCT.getValue());
        workProduceRecordQueryWrapper.eq("produce_task_id", id);
        List<WorkProduceRecord> workProduceRecordList = workProduceRecordMapper.selectList(workProduceRecordQueryWrapper);
        Map<String, ItemCell> itemCellMap = new HashMap<>(8);
        workProduceRecordList.forEach(workProduceRecord -> {
            ItemCell itemCell = itemCellMap.get(workProduceRecord.getItemCode());
            if (null == itemCell) {
                itemCell = new ItemCell();
                itemCell.setItemId(workProduceRecord.getItemId());
                itemCell.setItemCode(workProduceRecord.getItemCode());
                itemCell.setItemName(workProduceRecord.getItemName());
                itemCell.setUnitName(workProduceRecord.getUnitName());
                itemCell.setUnitId(workProduceRecord.getUnit());
                itemCell.setQty(workProduceRecord.getSubmitQty());
                itemCellMap.put(workProduceRecord.getItemCode(), itemCell);
            } else {
                itemCell.setQty(itemCell.getQty().add(workProduceRecord.getSubmitQty()));
            }
        });
        return new ArrayList<>(itemCellMap.values());
    }

    @Override
    public void assignProduceTask(WorkProduceTaskDetailVO workProduceTaskDetailVO) {

        workProduceTaskEmployeeMapper.deleteByExcuteTaskId(workProduceTaskDetailVO.getId());

        if (StringUtils.isNoneBlank(workProduceTaskDetailVO.getEmployeeIds())) {
            String[] employeeIds = workProduceTaskDetailVO.getEmployeeIds().split(",");
            for (String userId : employeeIds) {
                WorkProduceTaskEmployee entity = new WorkProduceTaskEmployee();
                entity.setEmployeeId(userId);
                User user = sysUserService.getById(userId);
                entity.setEmployeeName(user.getRealname());
                // 外键设置
                entity.setExcuteTaskId(workProduceTaskDetailVO.getId());
                entity.setUsingType(ProduceTaskEmployeeUsingEnum.ASSIGN.getValue());
                workProduceTaskEmployeeMapper.insert(entity);
            }
        }
    }

    @Override
    public IPage<WorkProduceTask> allQrcodeTaskList(Page<WorkProduceTask> page, QueryWrapper<WorkProduceTask> queryWrapper) {
        return baseMapper.allQrcodeTaskList(page, queryWrapper);
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
