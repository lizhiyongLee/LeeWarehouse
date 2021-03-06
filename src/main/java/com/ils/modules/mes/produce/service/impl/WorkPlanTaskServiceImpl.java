package com.ils.modules.mes.produce.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.service.ProcessService;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.mapper.WorkShopMapper;
import com.ils.modules.mes.base.factory.mapper.WorkstationMapper;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.entity.SopTemplateControl;
import com.ils.modules.mes.base.sop.entity.SopTemplateStep;
import com.ils.modules.mes.base.sop.mapper.SopTemplateControlMapper;
import com.ils.modules.mes.base.sop.mapper.SopTemplateMapper;
import com.ils.modules.mes.base.sop.mapper.SopTemplateStepMapper;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskParaMapper;
import com.ils.modules.mes.produce.entity.*;
import com.ils.modules.mes.produce.mapper.*;
import com.ils.modules.mes.produce.service.WorkOrderLineService;
import com.ils.modules.mes.produce.vo.*;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.entity.SopStep;
import com.ils.modules.mes.sop.mapper.SopControlMapper;
import com.ils.modules.mes.sop.mapper.SopStepMapper;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.system.entity.BizConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.common.util.RedisUtil;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.service.TeamEmployeeService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.entity.WorkProduceTaskEmployee;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskEmployeeMapper;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.CodeGeneratorService;
import com.ils.modules.system.service.UserService;

import cn.hutool.core.lang.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: ?????????????????????
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Service
public class WorkPlanTaskServiceImpl extends ServiceImpl<WorkPlanTaskMapper, WorkPlanTask> implements WorkPlanTaskService {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private UserService sysUserService;

    @Autowired
    private WorkPlanTaskEmployeeMapper workPlanTaskEmployeeMapper;

    @Autowired
    private WorkPlanTaskMapper workPlanTaskMapper;

    @Autowired
    private TeamEmployeeService teamEmployeeService;

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    @Lazy
    private WorkOrderService workOrderService;

    @Autowired
    @Lazy
    private WorkProcessTaskService workProcessTaskService;

    @Autowired
    @Lazy
    private WorkProduceTaskService workProduceTaskService;

    @Autowired
    private WorkProduceTaskEmployeeMapper workProduceTaskEmployeeMapper;
    @Autowired
    private WorkOrderLineParaMapper workOrderLineParaMapper;
    @Autowired
    private WorkProduceTaskParaMapper workProduceTaskParaMapper;
    @Autowired
    private WorkOrderLineService workOrderLineService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProcessService processService;

    @Autowired
    private WorkstationMapper workstationMapper;
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private SaleOrderMapper saleOrderMapper;
    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private SopStepMapper sopStepMapper;
    @Autowired
    private SopControlMapper sopControlMapper;
    @Autowired
    private SopTemplateMapper sopTemplateMapper;
    @Autowired
    private SopTemplateControlMapper sopTemplateControlMapper;
    @Autowired
    private SopTemplateStepMapper sopTemplateStepMapper;

    /**
     * ??????????????????????????????
     */
    private final String WORK_ORDER_LOCK_PREFIX = "work_order_";

    /**
     * ????????????????????????
     */
    private final long EXPIRE_TIME = 50000L;

    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {
        String lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskVO.getOrderId();
        String requestId = UUID.fastUUID().toString();
        try {
            // ???????????????????????????????????????????????????
            if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                throw new ILSBootException("P-OW-0011", workPlanTaskVO.getOrderNo(), workPlanTaskVO.getProcessCode());
            }
            this.saveSingleWorkPlanTask(workPlanTaskVO);

        } finally {
            redisUtil.releaseDistributedLock(lockKey, requestId);
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void batchSaveWorkPlanTask(List<WorkPlanTaskVO> lstWorkPlanTaskVO) {
        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;
        // ???????????????
        try {
            for (WorkPlanTaskVO workPlanTaskVO : lstWorkPlanTaskVO) {
                lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskVO.getOrderId();
                requestId = UUID.fastUUID().toString();
                if (!lockMap.containsKey(lockKey)) {
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        throw new ILSBootException("P-OW-0011", workPlanTaskVO.getOrderNo(),
                                workPlanTaskVO.getProcessCode());
                    }
                    lockMap.put(lockKey, requestId);
                }
                this.saveSingleWorkPlanTask(workPlanTaskVO);
            }
        } finally {
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }

        }
    }

    /**
     * ??????????????????
     *
     * @param workPlanTaskVO
     * @date 2020???12???2???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void saveSingleWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {

        this.validateWorkPlanTask(workPlanTaskVO);

        // ????????????
        String taskNo =
                codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.PLAN_TASK_NO, workPlanTaskVO);
        workPlanTaskVO.setTaskCode(taskNo);
        // ??????????????????
        workPlanTaskVO.setExeStatus(PlanTaskExeStatusEnum.NOT_START.getValue());
        workPlanTaskVO.setPlanStatus(PlanTaskStatusEnum.SCHEDULED.getValue());
        // ??????????????????
        Workstation workstation = workstationService.getById(workPlanTaskVO.getStationId());
        workPlanTaskVO.setStationName(workstation.getStationName());
        workPlanTaskVO.setStationCode(workstation.getStationCode());
        workPlanTaskVO.setTotalQty(BigDecimal.ZERO);

        baseMapper.insert(workPlanTaskVO);

        this.setEmployee(workPlanTaskVO);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {
        String lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskVO.getOrderId();
        String requestId = UUID.fastUUID().toString();
        try {
            // ???????????????????????????????????????????????????
            if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                throw new ILSBootException("P-OW-0011", workPlanTaskVO.getOrderNo(), workPlanTaskVO.getProcessCode());
            }

            this.validateWorkPlanTask(workPlanTaskVO);
            // ??????????????????
            Workstation workstation = workstationService.getById(workPlanTaskVO.getStationId());
            workPlanTaskVO.setStationName(workstation.getStationName());
            workPlanTaskVO.setStationCode(workstation.getStationCode());

            baseMapper.updateById(workPlanTaskVO);

            workPlanTaskEmployeeMapper.deleteBytaskId(workPlanTaskVO.getId());
            this.setEmployee(workPlanTaskVO);

        } finally {
            redisUtil.releaseDistributedLock(lockKey, requestId);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkPlanTaskLockStatus(WorkPlanTaskVO workPlanTaskVO) {
        String lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskVO.getOrderId();
        String requestId = UUID.fastUUID().toString();
        try {
            // ???????????????????????????????????????????????????
            if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                throw new ILSBootException("P-OW-0011", workPlanTaskVO.getOrderNo(), workPlanTaskVO.getProcessCode());
            }
            WorkPlanTaskVO updateWorkPlanTaskVO = new WorkPlanTaskVO();
            updateWorkPlanTaskVO.setId(workPlanTaskVO.getId());
            updateWorkPlanTaskVO.setLockStatus(workPlanTaskVO.getLockStatus());
            baseMapper.updateById(updateWorkPlanTaskVO);
        } finally {
            redisUtil.releaseDistributedLock(lockKey, requestId);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public PlanTaskErrorMsgVO cancelPlanTask(List<String> idList) {
        List<WorkPlanTaskProcessVO> lstWorkPlanTask = baseMapper.queryPlanWorkProcessAscList(idList);
        List<PlanTaskErrorVO> lstPlanTaskErrorVO = new ArrayList<PlanTaskErrorVO>(8);
        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;
        // ????????????
        try {
            for (WorkPlanTaskProcessVO workPlanTaskProcessVO : lstWorkPlanTask) {
                lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskProcessVO.getOrderId();
                requestId = UUID.fastUUID().toString();
                if (!lockMap.containsKey(lockKey)) {
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
                        planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
                        planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
                        planTaskErrorVO.setProcessInfo(
                                workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
                        planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
                        planTaskErrorVO.setErrMsg("??????????????????????????????????????????");
                        lstPlanTaskErrorVO.add(planTaskErrorVO);
                        continue;
                    }
                    lockMap.put(lockKey, requestId);
                }
                this.cancelSinglePlanTask(workPlanTaskProcessVO, lstPlanTaskErrorVO);
            }
        } finally {
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }

        return this.getPlanTaskErrorMsgVO(MesCommonConstant.MESSAGE_TIP, lstPlanTaskErrorVO);
    }

    /**
     * ????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskErrorVO
     * @date 2020???12???2???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void cancelSinglePlanTask(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                      List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        // ????????????????????????????????????
        if (PlanTaskStatusEnum.SCHEDULED.getValue().equals(workPlanTaskProcessVO.getPlanStatus())) {

            baseMapper.deleteById(workPlanTaskProcessVO.getId());
            workPlanTaskEmployeeMapper.deleteBytaskId(workPlanTaskProcessVO.getId());
            // ????????????????????????????????????
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());
            QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
            WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
            queryWrapper.eq("id", workPlanTaskProcessVO.getProcessTaskId());
            updateWorkProcessTask.setScheduledQty(
                    BigDecimalUtils.subtract(workProcessTask.getScheduledQty(), workPlanTaskProcessVO.getPlanQty()));
            workProcessTaskService.update(updateWorkProcessTask, queryWrapper);
            // ??????????????????
            QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
            workPlanTaskQueryWrapper.eq("order_id", workPlanTaskProcessVO.getOrderId());
            int count = this.count(workPlanTaskQueryWrapper);
            if (count == 0) {
                QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
                WorkOrder updateWorkOrder = new WorkOrder();
                workOrderQueryWrapper.eq("id", workPlanTaskProcessVO.getOrderId());
                updateWorkOrder.setStatus(WorkOrderStatusEnum.PLAN.getValue());
                workOrderService.update(updateWorkOrder, workOrderQueryWrapper);
            }
        } else {
            PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
            planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
            planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
            planTaskErrorVO
                    .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
            planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
            planTaskErrorVO.setErrMsg("??????????????????????????????");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
        }
    }


    /**
     * ??????????????????,?????????????????????????????????????????????????????????
     *
     * @param workPlanTaskVO
     * @date 2020???11???25???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void validateWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {

        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskVO.getProcessTaskId());
        //oldPlanQty ???????????????????????????????????????????????????
        BigDecimal oldPlanQty = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(workPlanTaskVO.getId())) {
            WorkPlanTask oldWorkPlanTask = this.getById(workPlanTaskVO.getId());
            oldPlanQty = oldWorkPlanTask.getPlanQty();
        }

        // ??????????????????-????????????????????????oldPlanQty
        BigDecimal processScheduleQty = BigDecimalUtils.subtract(workProcessTask.getScheduledQty(), oldPlanQty);
        BigDecimal planQty = workPlanTaskVO.getPlanQty();
        BigDecimal reayScheduleQty = BigDecimalUtils.subtract(workProcessTask.getPlanQty(), processScheduleQty);
        boolean bPlanQtyFlag = BigDecimalUtils.greaterThan(planQty, reayScheduleQty);
        if (bPlanQtyFlag) {
            throw new ILSBootException("P-OW-0008", workPlanTaskVO.getOrderNo(), workPlanTaskVO.getProcessCode());
        }

        // ????????????????????????????????????
        QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        queryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask.setScheduledQty(BigDecimalUtils.add(processScheduleQty, planQty));
        workProcessTaskService.update(updateWorkProcessTask, queryWrapper);

        // ???????????????????????????????????????????????????
        WorkOrder workOrder = workOrderService.getById(workPlanTaskVO.getOrderId());
        if (WorkOrderStatusEnum.PLAN.getValue().equals(workOrder.getStatus())) {
            QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
            WorkOrder updateWorkOrder = new WorkOrder();
            workOrderQueryWrapper.eq("id", workOrder.getId());
            updateWorkOrder.setStatus(WorkOrderStatusEnum.SCHEDULED.getValue());
            workOrderService.update(updateWorkOrder, workOrderQueryWrapper);
        }
    }

    /**
     * ????????????
     *
     * @param workPlanTaskVO
     * @date 2020???11???24???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void setEmployee(WorkPlanTaskVO workPlanTaskVO) {
        if (MesCommonConstant.PLAN_TASK_USER_TYPE.equals(workPlanTaskVO.getUserType())) {
            if (StringUtils.isNoneBlank(workPlanTaskVO.getEmployeeIds())) {
                String[] employeeIds = workPlanTaskVO.getEmployeeIds().split(",");
                for (String userId : employeeIds) {
                    WorkPlanTaskEmployee entity = new WorkPlanTaskEmployee();
                    entity.setEmployeeId(userId);
                    User user = sysUserService.getById(userId);
                    entity.setEmployeeName(user.getRealname());
                    // ????????????
                    entity.setTaskId(workPlanTaskVO.getId());
                    workPlanTaskEmployeeMapper.insert(entity);
                }
            }
        } else {
            if (StringUtils.isBlank(workPlanTaskVO.getTeamId())) {
                return;
            }
            List<TeamEmployee> teamEmployeeList = teamEmployeeService.selectByMainId(workPlanTaskVO.getTeamId());
            for (TeamEmployee temp : teamEmployeeList) {
                WorkPlanTaskEmployee entity = new WorkPlanTaskEmployee();
                entity.setEmployeeId(temp.getEmployeeId());
                entity.setEmployeeCode(temp.getEmployeeCode());
                entity.setEmployeeName(temp.getEmployeeName());
                entity.setTaskId(workPlanTaskVO.getId());
                workPlanTaskEmployeeMapper.insert(entity);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkPlanTask(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkPlanTask(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage<WorkPlanTaskVO> listPage(Page<WorkPlanTaskVO> page, QueryWrapper<WorkPlanTaskVO> queryWrapper) {
        return baseMapper.listPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public PlanTaskErrorMsgVO issuePlanTask(List<String> idList, String checkWorkOrder) {
        // ?????????????????????????????????????????????????????????????????? ??????????????????????????????
        List<WorkPlanTaskProcessVO> lstWorkPlanTask = baseMapper.queryPlanWorkProcessAscList(idList);
        List<PlanTaskErrorVO> lstPlanTaskErrorVO = new ArrayList<PlanTaskErrorVO>();

        // ???????????????????????????
        boolean bFlag = this.checkWorkOrderDate(lstWorkPlanTask, lstPlanTaskErrorVO, checkWorkOrder);
        if (!bFlag) {
            return this.getPlanTaskErrorMsgVO(MesCommonConstant.MESSAGE_CONFIRM, lstPlanTaskErrorVO);
        }

        // ?????????????????????
        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;


        // ????????????????????????????????????
        try {
            for (WorkPlanTaskProcessVO workPlanTaskProcessVO : lstWorkPlanTask) {
                lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskProcessVO.getOrderId();
                requestId = UUID.fastUUID().toString();
                if (!lockMap.containsKey(lockKey)) {
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
                        planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
                        planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
                        planTaskErrorVO.setProcessInfo(
                                workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
                        planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
                        planTaskErrorVO.setErrMsg("??????????????????????????????????????????");
                        lstPlanTaskErrorVO.add(planTaskErrorVO);
                        continue;
                    }
                    lockMap.put(lockKey, requestId);
                }
                this.validatePlanTaskIssue(workPlanTaskProcessVO, lstPlanTaskErrorVO);
            }
        } finally {
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }

        return this.getPlanTaskErrorMsgVO(MesCommonConstant.MESSAGE_TIP, lstPlanTaskErrorVO);
    }

    /**
     * @param lstWorkPlanTask
     * @param lstPlanTaskErrorVO
     * @param checkWorkOrder
     * @return true ???????????? false ???????????????
     * @date 2020???11???30???
     */
    private boolean checkWorkOrderDate(List<WorkPlanTaskProcessVO> lstWorkPlanTask,
                                       List<PlanTaskErrorVO> lstPlanTaskErrorVO, String checkWorkOrder) {
        boolean bCheckFlag = false;
        Date planStartTime = null;
        Date planEndTime = null;
        if (ZeroOrOneEnum.ONE.getStrCode().equals(checkWorkOrder)) {
            for (WorkPlanTaskProcessVO workPlanTaskProcessVO : lstWorkPlanTask) {
                String workOrderId = workPlanTaskProcessVO.getOrderId();
                WorkOrder workOrder = workOrderService.getById(workOrderId);
                planStartTime = workOrder.getPlanStartTime();
                planEndTime = workOrder.getPlanEndTime();
                bCheckFlag = false;
                if (null != planStartTime
                        && planStartTime.getTime() > workPlanTaskProcessVO.getPlanStartTime().getTime()) {
                    bCheckFlag = true;
                }

                if (null != planEndTime && planEndTime.getTime() < workPlanTaskProcessVO.getPlanEndTime().getTime()) {
                    bCheckFlag = true;
                }

                if (bCheckFlag) {
                    PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
                    planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
                    planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
                    planTaskErrorVO.setPlanStartTime(workPlanTaskProcessVO.getPlanStartTime());
                    planTaskErrorVO.setPlanEndTime(workPlanTaskProcessVO.getPlanEndTime());
                    planTaskErrorVO.setOrderStartTime(planStartTime);
                    planTaskErrorVO.setOrderEndTime(planEndTime);
                    lstPlanTaskErrorVO.add(planTaskErrorVO);
                }
            }

            if (CommonUtil.isEmptyOrNull(lstPlanTaskErrorVO)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param workPlanTask
     * @date 2020???11???26???
     */
    private void validatePlanTaskIssue(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                       List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        boolean bResult = false;
        // ??????????????????
        String planStatus = workPlanTaskProcessVO.getPlanStatus();
        if (!PlanTaskStatusEnum.SCHEDULED.getValue().equals(planStatus)) {
            PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
            planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
            planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
            planTaskErrorVO
                    .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
            planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
            planTaskErrorVO.setErrMsg("??????????????????????????????");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
            return;
        }

        // ??????????????????????????????????????????
        String processTaskPriorCode = workPlanTaskProcessVO.getPriorCode();

        // ?????????????????????????????????
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());

        if (!MesCommonConstant.ROUTE_PROCESS_FIRST.equals(processTaskPriorCode)) {
            // ???????????????????????????????????????????????????????????????????????????
            bResult = this.issueCheckPreProcessSum(workPlanTaskProcessVO, workProcessTask, lstPlanTaskErrorVO);
            if (!bResult) {
                return;
            }

            String linkType = workPlanTaskProcessVO.getLinkType();
            // 1,???????????????????????????2??????????????????????????????
            if (ProcessLineTypeEnum.LINK_TYPE_1.getValue().equals(linkType)) {
                bResult =
                        this.issueCheckProcessLink1(workPlanTaskProcessVO, workProcessTask, lstPlanTaskErrorVO);
            } else {
                bResult =
                        this.issueCheckProcessLink2(workPlanTaskProcessVO, workProcessTask, lstPlanTaskErrorVO);
            }
            if (!bResult) {
                return;
            }
        }

        // ?????????????????????
        bResult = this.issueCheckStation(workPlanTaskProcessVO, lstPlanTaskErrorVO);
        if (!bResult) {
            return;
        }

        // ???????????????????????????????????????
        this.issueUpdate(workPlanTaskProcessVO);
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param workPlanTaskProcessVO      ????????????
     * @param workProcessTask????????????
     * @param lstPlanTaskIssueErrorMsgVO
     * @return true ?????? false ?????????
     * @date 2020???11???30???
     */
    private boolean issueCheckPreProcessSum(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                            WorkProcessTask workProcessTask, List<PlanTaskErrorVO> lstPlanTaskErrorVO) {

        WorkProcessTask preWorkProcessTask = workProcessTaskService.getById(workProcessTask.getPriorCode());
        BigDecimal prePublishQty = preWorkProcessTask.getPublishQty();
        BigDecimal prePlanQty = preWorkProcessTask.getPlanQty();

        BigDecimal publishQty =
                BigDecimalUtils.add(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty());
        BigDecimal planQty = workProcessTask.getPlanQty();

        // publishQty/planQty<=prePublishQty/prePlanQty ==>prePlanQty*publishQty<=planQty*prePublishQty
        boolean bFlag = BigDecimalUtils.greaterThan(BigDecimalUtils.multiply(prePlanQty, publishQty),
                BigDecimalUtils.multiply(planQty, prePublishQty));
        if (bFlag) {
            PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
            planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
            planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
            planTaskErrorVO
                    .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
            planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
            planTaskErrorVO.setErrMsg("????????????????????????????????????");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
            return false;
        } else {
            return true;
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @param workProcessTask
     * @param lstPlanTaskIssueErrorMsgVO
     * @return
     * @date 2020???11???30???
     */
    private boolean issueCheckProcessLink1(WorkPlanTaskProcessVO workPlanTaskProcessVO, WorkProcessTask workProcessTask,
                                           List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
        workPlanTaskQueryWrapper.eq("process_task_id", workPlanTaskProcessVO.getPriorCode());
        workPlanTaskQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
        workPlanTaskQueryWrapper.le("plan_start_time", workPlanTaskProcessVO.getPlanStartTime());
        List<WorkPlanTask> lstWorkPlanTask = this.list(workPlanTaskQueryWrapper);
        BigDecimal prePublishQty = BigDecimal.ZERO;
        for (WorkPlanTask workPlanTask : lstWorkPlanTask) {
            prePublishQty = BigDecimalUtils.add(prePublishQty, workPlanTask.getPlanQty());
        }
        WorkProcessTask preWorkProcessTask = workProcessTaskService.getById(workProcessTask.getPriorCode());
        BigDecimal prePlanQty = preWorkProcessTask.getPlanQty();

        BigDecimal publishQty =
                BigDecimalUtils.add(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty());
        BigDecimal planQty = workProcessTask.getPlanQty();
        // publishQty/planQty<=prePublishQty/prePlanQty ==>prePlanQty*publishQty<=planQty*prePublishQty
        boolean bFlag = BigDecimalUtils.greaterThan(BigDecimalUtils.multiply(prePlanQty, publishQty),
                BigDecimalUtils.multiply(planQty, prePublishQty));
        if (bFlag) {
            // ?????????????????????????????????????????????????????????????????????
            workPlanTaskQueryWrapper.clear();
            workPlanTaskQueryWrapper.eq("process_task_id", workPlanTaskProcessVO.getPriorCode());
            workPlanTaskQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
            workPlanTaskQueryWrapper.gt("plan_start_time", workPlanTaskProcessVO.getPlanStartTime());
            workPlanTaskQueryWrapper.orderByAsc("plan_start_time");
            lstWorkPlanTask = this.list(workPlanTaskQueryWrapper);
            return setErrorMsg(workPlanTaskProcessVO, lstPlanTaskErrorVO, lstWorkPlanTask);
        } else {
            return true;
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @param workProcessTask
     * @param lstPlanTaskIssueErrorMsgVO
     * @return
     * @date 2020???11???30???
     */
    private boolean issueCheckProcessLink2(WorkPlanTaskProcessVO workPlanTaskProcessVO, WorkProcessTask workProcessTask,
                                           List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
        workPlanTaskQueryWrapper.eq("process_task_id", workPlanTaskProcessVO.getPriorCode());
        workPlanTaskQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
        workPlanTaskQueryWrapper.le("plan_end_time", workPlanTaskProcessVO.getPlanStartTime());
        List<WorkPlanTask> lstWorkPlanTask = this.list(workPlanTaskQueryWrapper);
        BigDecimal prePublishQty = BigDecimal.ZERO;
        for (WorkPlanTask workPlanTask : lstWorkPlanTask) {
            prePublishQty = BigDecimalUtils.add(prePublishQty, workPlanTask.getPlanQty());
        }
        WorkProcessTask preWorkProcessTask = workProcessTaskService.getById(workProcessTask.getPriorCode());
        BigDecimal prePlanQty = preWorkProcessTask.getPlanQty();

        BigDecimal publishQty =
                BigDecimalUtils.add(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty());
        BigDecimal planQty = workProcessTask.getPlanQty();
        // publishQty/planQty<=prePublishQty/prePlanQty ==>prePlanQty*publishQty<=planQty*prePublishQty
        boolean bFlag = BigDecimalUtils.greaterThan(BigDecimalUtils.multiply(prePlanQty, publishQty),
                BigDecimalUtils.multiply(planQty, prePublishQty));
        if (bFlag) {
            // ?????????????????????????????????????????????????????????????????????
            workPlanTaskQueryWrapper.clear();
            workPlanTaskQueryWrapper.eq("process_task_id", workPlanTaskProcessVO.getPriorCode());
            workPlanTaskQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
            workPlanTaskQueryWrapper.gt("plan_end_time", workPlanTaskProcessVO.getPlanStartTime());
            workPlanTaskQueryWrapper.orderByAsc("plan_end_time");
            lstWorkPlanTask = this.list(workPlanTaskQueryWrapper);
            return setErrorMsg(workPlanTaskProcessVO, lstPlanTaskErrorVO, lstWorkPlanTask);
        } else {
            return true;
        }
    }

    /**
     * ????????????
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskErrorVO
     * @param lstWorkPlanTask
     * @return
     */
    private boolean setErrorMsg(WorkPlanTaskProcessVO workPlanTaskProcessVO, List<PlanTaskErrorVO> lstPlanTaskErrorVO, List<WorkPlanTask> lstWorkPlanTask) {
        PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
        planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
        planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
        planTaskErrorVO.setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
        planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
        if (CommonUtil.isEmptyOrNull(lstWorkPlanTask)) {
            planTaskErrorVO.setErrMsg("????????????????????????????????????");
        } else {
            planTaskErrorVO.setErrMsg("???????????????????????????" + lstWorkPlanTask.get(0).getTaskCode() + "??????????????????");
        }
        lstPlanTaskErrorVO.add(planTaskErrorVO);
        return false;
    }


    /**
     * ????????????????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskIssueErrorMsgVO
     * @return true ?????? false ?????????
     * @date 2020???11???30???
     */
    private boolean issueCheckStation(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                      List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        String stationId = workPlanTaskProcessVO.getStationId();
        Workstation workstation = workstationService.getById(stationId);
        String multiStation = workstation.getMultistation();
        // ????????????????????????????????????????????????
        if (ZeroOrOneEnum.ONE.getStrCode().equals(multiStation)) {
            return true;
        } else {
            QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
            workPlanTaskQueryWrapper.in("exe_status", Arrays.asList(PlanTaskExeStatusEnum.NOT_START.getValue(),
                    PlanTaskExeStatusEnum.PRODUCE.getValue(), PlanTaskExeStatusEnum.SUSPEND.getValue()
            ));
            workPlanTaskQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
            workPlanTaskQueryWrapper.eq("station_id", stationId);
            workPlanTaskQueryWrapper.lt("plan_start_time", workPlanTaskProcessVO.getPlanEndTime());
            workPlanTaskQueryWrapper.gt("plan_end_time", workPlanTaskProcessVO.getPlanStartTime());
            if (StringUtils.isNotBlank(workPlanTaskProcessVO.getId())) {
                workPlanTaskQueryWrapper.ne("id", workPlanTaskProcessVO.getId());
            }
            List<WorkPlanTask> lstWorkPlanTask = this.list(workPlanTaskQueryWrapper);
            if (!CommonUtil.isEmptyOrNull(lstWorkPlanTask)) {
                String taskCode = lstWorkPlanTask.get(0).getTaskCode();
                PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
                planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
                planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
                planTaskErrorVO
                        .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
                planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
                planTaskErrorVO.setErrMsg(taskCode + "????????????????????????????????????????????????");
                lstPlanTaskErrorVO.add(planTaskErrorVO);
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @date 2020???11???27???
     */
    private void issueUpdate(WorkPlanTaskProcessVO workPlanTaskProcessVO) {

        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());
        // ????????????????????????????????????
        QueryWrapper<WorkProcessTask> workProcessTaskQueryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        workProcessTaskQueryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask
                .setPublishQty(BigDecimalUtils.add(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty()));
        workProcessTaskService.update(updateWorkProcessTask, workProcessTaskQueryWrapper);
        // ????????????????????????
        QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
        WorkPlanTask updateWorkPlanTask = new WorkPlanTask();
        workPlanTaskQueryWrapper.eq("id", workPlanTaskProcessVO.getId());
        updateWorkPlanTask.setDistrubuteTime(new Date());
        updateWorkPlanTask.setPlanStatus(PlanTaskStatusEnum.ISSUED.getValue());
        this.update(updateWorkPlanTask, workPlanTaskQueryWrapper);
        // ??????????????????-????????????
        WorkOrder workOrder = workOrderService.getById(workPlanTaskProcessVO.getOrderId());
        if (WorkOrderStatusEnum.SCHEDULED.getValue().equals(workOrder.getStatus())) {
            QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
            WorkOrder updateWorkOrder = new WorkOrder();
            workOrderQueryWrapper.eq("id", workOrder.getId());
            updateWorkOrder.setStatus(WorkOrderStatusEnum.ISSUED.getValue());
            workOrderService.update(updateWorkOrder, workOrderQueryWrapper);
        }

        // ??????????????????
        this.generateProduceTask(workPlanTaskProcessVO);
    }

    /**
     * ??????????????????
     *
     * @param workPlanTaskProcessVO
     * @date 2020???12???8???
     */
    private void generateProduceTask(WorkPlanTaskProcessVO workPlanTaskProcessVO) {
        //??????????????????????????????????????????
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.SOP_TEMPLATE_SWITCH);
        String switchIdent = bizConfig.getConfigValue();

        // ??????????????????
        QueryWrapper<WorkPlanTaskEmployee> queryEmployee = new QueryWrapper<>();
        queryEmployee.eq("task_id", workPlanTaskProcessVO.getId());
        List<WorkPlanTaskEmployee> lstWorkPlanTaskEmployee = workPlanTaskEmployeeMapper.selectList(queryEmployee);

        // ????????????
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");
        WorkProduceTask workProduceTask = new WorkProduceTask();
        BeanUtils.copyProperties(workPlanTaskProcessVO, workProduceTask, ignoreProperty);
        workProduceTask.setPlanTaskId(workPlanTaskProcessVO.getId());
        workProduceTask.setTotalQty(BigDecimal.ZERO);
        workProduceTask.setGoodQty(BigDecimal.ZERO);
        workProduceTask.setBadQty(BigDecimal.ZERO);
        workProduceTask.setSop(switchIdent);

        Process process = processService.getById(workProduceTask.getProcessId());
        if (null != process) {
            workProduceTask.setTemplateId(process.getTemplateId());
            workProduceTaskService.save(workProduceTask);
        }
        if (!CommonUtil.isEmptyOrNull(lstWorkPlanTaskEmployee)) {
            List<WorkProduceTaskEmployee> lstWorkProduceTaskEmployee =
                    new ArrayList<WorkProduceTaskEmployee>(lstWorkPlanTaskEmployee.size());
            for (WorkPlanTaskEmployee employee : lstWorkPlanTaskEmployee) {
                WorkProduceTaskEmployee workProduceTaskEmployee = new WorkProduceTaskEmployee();
                BeanUtils.copyProperties(employee, workProduceTaskEmployee);
                workProduceTaskEmployee.setExcuteTaskId(workProduceTask.getId());
                workProduceTaskEmployee.setUsingType(ProduceTaskEmployeeUsingEnum.ASSIGN.getValue());
                lstWorkProduceTaskEmployee.add(workProduceTaskEmployee);
            }
            workProduceTaskEmployeeMapper.insertBatchSomeColumn(lstWorkProduceTaskEmployee);
            this.sendMsg(workProduceTask, lstWorkProduceTaskEmployee);
        }

        //??????????????????sop???????????????
        if (null != switchIdent && switchIdent.equals(ZeroOrOneEnum.ONE.getStrCode())) {
            insertSop(workPlanTaskProcessVO, workProduceTask);
        }

        //?????????????????????
        String lineId = null;
        List<WorkOrderLine> workOrderLineList = workOrderLineService.selectByMainId(workPlanTaskProcessVO.getOrderId());
        for (WorkOrderLine workOrderLine : workOrderLineList) {
            if (workOrderLine.getProcessId().equals(workPlanTaskProcessVO.getProcessId())) {
                lineId = workOrderLine.getId();
            }
        }
        QueryWrapper<WorkOrderLinePara> workOrderLineParaQueryWrapper = new QueryWrapper<>();
        workOrderLineParaQueryWrapper.eq("work_order_line_id", lineId);
        List<WorkOrderLinePara> paraList = workOrderLineParaMapper.selectList(workOrderLineParaQueryWrapper);
        paraList.forEach(workOrderLinePara -> {
            WorkProduceTaskPara workProduceTaskPara = new WorkProduceTaskPara();
            BeanUtils.copyProperties(workOrderLinePara, workProduceTaskPara, ignoreProperty);
            workProduceTaskPara.setProduceTaskId(workProduceTask.getId());
            workProduceTaskPara.setOrderId(workProduceTask.getOrderId());
            workProduceTaskPara.setOrderNo(workProduceTask.getOrderNo());
            workProduceTaskPara.setItemId(workProduceTask.getItemId());
            workProduceTaskPara.setItemCode(workProduceTask.getItemCode());
            workProduceTaskPara.setItemName(workProduceTask.getItemName());
            workProduceTaskParaMapper.insert(workProduceTaskPara);
        });
    }

    /**
     * ??????????????????sop???????????????
     *
     * @param workPlanTaskProcessVO
     * @param workProduceTask
     */
    private void insertSop(WorkPlanTaskProcessVO workPlanTaskProcessVO, WorkProduceTask workProduceTask) {
        WorkOrder workOrder = workOrderMapper.selectById(workPlanTaskProcessVO.getOrderId());
        String workflowType = workOrder.getWorkflowType();

        QueryWrapper<SopTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        queryWrapper.eq("process_code", workPlanTaskProcessVO.getProcessCode());
        if (WorkflowTypeEnum.ROUTE.getValue().equals(workflowType) || WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType)) {
            queryWrapper.eq("entity_code", workOrder.getRouteId());
        } else if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
            queryWrapper.eq("entity_code", workOrder.getProductId());
        }
        List<SopTemplate> sopTemplateList = sopTemplateMapper.selectList(queryWrapper);

        if (CollectionUtil.isEmpty(sopTemplateList) || sopTemplateList.size() > 1) {
            throw new ILSBootException("P-SOP-0093", workPlanTaskProcessVO.getProcessCode());
        }
        SopTemplate sopTemplate = sopTemplateList.get(0);
        List<SopTemplateStep> sopTemplateSteps = sopTemplateStepMapper.selectByMainId(sopTemplate.getId());

        //??????sopTemplateSteps?????????????????????????????????????????????id
        String nextStepId = "end";
        CollectionUtil.sortByProperty(sopTemplateSteps, "stepSeq");
        Collections.reverse(sopTemplateSteps);

        List<SopTemplateControl> sopTemplateControlList = sopTemplateControlMapper.selectByMainId(sopTemplate.getId());
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");
        for (SopTemplateStep sopTemplateStep : sopTemplateSteps) {
            SopStep sopStep = new SopStep();
            BeanUtils.copyProperties(sopTemplateStep, sopStep, ignoreProperty);
            sopStep.setNextStepId(nextStepId);
            sopStep.setRelatedTaskId(workProduceTask.getId());
            sopStep.setRelatedTaskType(RelatedTaskTypeEnum.PRODUCE.getValue());
            sopStepMapper.insert(sopStep);
            //???????????????????????????????????????id
            nextStepId = sopStep.getId();
            for (SopTemplateControl sopTemplateControl : sopTemplateControlList) {
                if (sopTemplateControl.getTemplateStepId().equals(sopTemplateStep.getId())) {
                    SopControl sopControl = new SopControl();
                    BeanUtils.copyProperties(sopTemplateControl, sopControl, ignoreProperty);
                    sopControl.setRelatedTaskId(workProduceTask.getId());
                    sopControl.setSopStepId(sopStep.getId());
                    sopControlMapper.insert(sopControl);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public PlanTaskErrorMsgVO undoIssuePlanTask(List<String> idList) {
        // ?????????????????????????????????????????????????????????????????? ??????????????????????????????
        List<WorkPlanTaskProcessVO> lstWorkPlanTask = baseMapper.queryPlanWorkProcessDescList(idList);
        // ???????????????????????????
        List<PlanTaskErrorVO> lstPlanTaskErrorVO = new ArrayList<PlanTaskErrorVO>();

        // ?????????????????????
        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;

        // ????????????????????????????????????
        try {
            for (WorkPlanTaskProcessVO workPlanTaskProcessVO : lstWorkPlanTask) {
                lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskProcessVO.getOrderId();
                requestId = UUID.fastUUID().toString();
                if (!lockMap.containsKey(lockKey)) {
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
                        planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
                        planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
                        planTaskErrorVO.setProcessInfo(
                                workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
                        planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
                        planTaskErrorVO.setErrMsg("??????????????????????????????????????????");
                        lstPlanTaskErrorVO.add(planTaskErrorVO);
                        continue;
                    }
                    lockMap.put(lockKey, requestId);
                }
                this.validateUndoIssuePlanTask(workPlanTaskProcessVO, lstPlanTaskErrorVO);
            }
        } finally {
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }

        return this.getPlanTaskErrorMsgVO(MesCommonConstant.MESSAGE_TIP, lstPlanTaskErrorVO);
    }


    /**
     * ??????
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskIssueErrorMsgVO
     * @date 2020???11???27???
     */
    private void validateUndoIssuePlanTask(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                           List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        String exeStatus = workPlanTaskProcessVO.getExeStatus();
        String planStatus = workPlanTaskProcessVO.getPlanStatus();
        if (!PlanTaskExeStatusEnum.NOT_START.getValue().equals(exeStatus)
                || !PlanTaskStatusEnum.ISSUED.getValue().equals(planStatus)) {
            PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
            planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
            planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
            planTaskErrorVO
                    .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
            planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
            planTaskErrorVO.setErrMsg("??????????????????????????????");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
        } else {
            // ?????????????????????????????????
            String nextProcessCode = workPlanTaskProcessVO.getNextCode();
            if (MesCommonConstant.ROUTE_PROCESS_END.equals(nextProcessCode)) {
                this.undoIssueUpdate(workPlanTaskProcessVO);
            } else {
                QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
                workPlanTaskQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
                workPlanTaskQueryWrapper.eq("process_task_id", nextProcessCode);
                List<WorkPlanTask> lstWorkPlanTask = this.list(workPlanTaskQueryWrapper);
                if (CommonUtil.isEmptyOrNull(lstWorkPlanTask)) {
                    this.undoIssueUpdate(workPlanTaskProcessVO);
                } else {
                    PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
                    planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
                    planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
                    planTaskErrorVO
                            .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
                    planTaskErrorVO.setErrMsg("???????????????????????????" + lstWorkPlanTask.get(0).getTaskCode() + "????????????");
                    planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
                    lstPlanTaskErrorVO.add(planTaskErrorVO);
                }
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @date 2020???11???27???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void undoIssueUpdate(WorkPlanTaskProcessVO workPlanTaskProcessVO) {
        // ????????????????????????
        QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
        WorkPlanTask updateWorkPlanTask = new WorkPlanTask();
        workPlanTaskQueryWrapper.eq("id", workPlanTaskProcessVO.getId());
        updateWorkPlanTask.setPlanStatus(PlanTaskStatusEnum.SCHEDULED.getValue());
        this.update(updateWorkPlanTask, workPlanTaskQueryWrapper);

        // ????????????????????????????????????
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());
        QueryWrapper<WorkProcessTask> workProcessTaskQueryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        workProcessTaskQueryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask
                .setPublishQty(
                        BigDecimalUtils.subtract(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty()));
        workProcessTaskService.update(updateWorkProcessTask, workProcessTaskQueryWrapper);

        // ??????????????????
        QueryWrapper<WorkPlanTask> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_id", workPlanTaskProcessVO.getOrderId());
        orderQueryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
        int count = this.count(orderQueryWrapper);
        if (count == 0) {
            QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
            WorkOrder updateWorkOrder = new WorkOrder();
            workOrderQueryWrapper.eq("id", workPlanTaskProcessVO.getOrderId());
            updateWorkOrder.setStatus(WorkOrderStatusEnum.SCHEDULED.getValue());
            workOrderService.update(updateWorkOrder, workOrderQueryWrapper);
        }

        // ?????????????????????????????????
        this.undoProduceTask(workPlanTaskProcessVO);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param workPlanTaskProcessVO
     * @date 2020???12???8???
     */
    private void undoProduceTask(WorkPlanTaskProcessVO workPlanTaskProcessVO) {
        QueryWrapper<WorkProduceTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("plan_task_id", workPlanTaskProcessVO.getId());
        List<WorkProduceTask> lstWorkProduceTask = workProduceTaskService.list(taskQueryWrapper);
        // ??????????????????
        workProduceTaskService.deleteByPlanTaskId(workPlanTaskProcessVO.getId());
        if (!CommonUtil.isEmptyOrNull(lstWorkProduceTask)) {
            for (WorkProduceTask task : lstWorkProduceTask) {
                // ??????????????????
                workProduceTaskEmployeeMapper.deleteByExcuteTaskId(task.getId());
                //??????sop???????????????
                sopStepMapper.deleteByRelatedTaskId(task.getId());
                sopControlMapper.deleteByRelatedTaskId(task.getId());
            }
        }
    }

    /**
     * ????????????msgVO
     *
     * @param errCode
     * @param lstPlanTaskErrorVO
     * @return
     * @date 2020???11???30???
     */
    public PlanTaskErrorMsgVO getPlanTaskErrorMsgVO(String errCode, List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        PlanTaskErrorMsgVO msgVO = new PlanTaskErrorMsgVO();
        if (CommonUtil.isEmptyOrNull(lstPlanTaskErrorVO)) {
            msgVO.setErrorCode(MesCommonConstant.MESSAGE_NO);
        } else {
            msgVO.setErrorCode(errCode);
            msgVO.setLstPlanTaskErrorVO(lstPlanTaskErrorVO);
        }

        return msgVO;
    }

    @Override
    public List<GanttChartVO> getGanttChart(WorkPlanTaskVO queryWorkPlanTaskVO, HttpServletRequest req) {
        List<WorkPlanTask> workPlanTaskList = getWorkPlanTaskList(queryWorkPlanTaskVO);
        //???????????????????????????VO
        List<GanttChartVO> ganttChartVOList = new ArrayList<>();
        for (WorkPlanTask workPlanTask : workPlanTaskList) {
            GanttChartVO ganttChartVO = new GanttChartVO();
            ganttChartVO.setRowId(workPlanTask.getStationId());
            ganttChartVO.setRowName(workPlanTask.getStationName());
            ganttChartVO.setLabel(workPlanTask.getTaskCode());
            ganttChartVO.setId(workPlanTask.getId());
            ganttChartVO.setLockStatus(workPlanTask.getLockStatus());
            ganttChartVO.setOrderNo(workPlanTask.getOrderNo());
            ganttChartVO.setOrderId(workPlanTask.getOrderId());
            ganttChartVO.setProcessCode(workPlanTask.getProcessCode());
            ganttChartVO.setPlanType(workPlanTask.getPlanType());
            //???????????????1????????????0?????????
            Workstation workstation = workstationMapper.selectById(workPlanTask.getStationId());
            ganttChartVO.setMultistation(workstation.getMultistation());
            //????????????
            Map<String, Long> dateMap = new HashMap<>(3);
            dateMap.put("start", workPlanTask.getPlanStartTime().getTime());
            dateMap.put("end", workPlanTask.getPlanEndTime().getTime());
            //??????????????????????????????????????????????????????????????????1?????????????????????????????????
            dateMap.put("temp", workPlanTask.getPlanStartTime().getTime() + 1000L);
            ganttChartVO.setTime(dateMap);
            //????????????
            ganttChartVO.setOptionalStationId(this.queryStationById(workPlanTask.getOrderId(), workPlanTask.getProcessId(), workPlanTask.getSeq()));
            //planStatus+exeStatus??????????????????
            ganttChartVO.setStatus(generateTaskStatus(workPlanTask));
            ganttChartVOList.add(ganttChartVO);
        }
        return ganttChartVOList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param queryWorkPlanTaskVO
     * @return
     */
    private List<WorkPlanTask> getWorkPlanTaskList(WorkPlanTaskVO queryWorkPlanTaskVO) {
        QueryWrapper<WorkPlanTask> queryWrapper = new QueryWrapper<>();
        //?????????????????????????????????
        queryWrapper.ne("exe_status", PlanTaskExeStatusEnum.CANCEL.getValue());
        //??????????????????????????????????????????plan_status+exe_status???
        if (StringUtils.isNotBlank(queryWorkPlanTaskVO.getOrderNo())) {
            queryWrapper.like("order_no", queryWorkPlanTaskVO.getOrderNo());
        }
        if (StringUtils.isNotBlank(queryWorkPlanTaskVO.getItemName())) {
            queryWrapper.like("item_name", queryWorkPlanTaskVO.getItemName());
        }
        if (StringUtils.isNotBlank(queryWorkPlanTaskVO.getPlanStatus()) && StringUtils.isNotBlank(queryWorkPlanTaskVO.getExeStatus())) {
            queryWrapper.like("plan_status", queryWorkPlanTaskVO.getPlanStatus());
            queryWrapper.like("exe_status", queryWorkPlanTaskVO.getExeStatus());
        }
        //?????????????????????
        String stationCode = queryWorkPlanTaskVO.getStationCode();
        if (StringUtils.isNotBlank(stationCode)) {
            String[] stationCodeList = stationCode.split(",");
            queryWrapper.in("station_code", Arrays.asList(stationCodeList));
        }
        //?????????????????????
        List<String> orderListBySaleOrder = new ArrayList<>();
        if (StringUtils.isNotBlank(queryWorkPlanTaskVO.getSaleOrderNo())) {
            SaleOrder saleOrder = saleOrderMapper.selectByNo(queryWorkPlanTaskVO.getSaleOrderNo());
            if (saleOrder == null) {
                return new ArrayList<>();
            }
            QueryWrapper<WorkOrder> queryWorkOrderWrapper = new QueryWrapper<>();
            queryWorkOrderWrapper.eq("sale_order_id", saleOrder.getId());
            List<WorkOrder> workOrderList = workOrderMapper.selectList(queryWorkOrderWrapper);
            workOrderList.forEach(workOrder -> orderListBySaleOrder.add(workOrder.getOrderNo()));
            queryWrapper.in("order_no", orderListBySaleOrder);
        }
        //?????????????????????
        if (queryWorkPlanTaskVO.getPlanStartTime() != null && queryWorkPlanTaskVO.getPlanEndTime() != null) {
            queryWrapper.between("plan_start_time", queryWorkPlanTaskVO.getPlanStartTime(), queryWorkPlanTaskVO.getPlanEndTime());
            queryWrapper.between("plan_end_time", queryWorkPlanTaskVO.getPlanStartTime(), queryWorkPlanTaskVO.getPlanEndTime());
        }
        //????????????
        if (StringUtils.isNotBlank(queryWorkPlanTaskVO.getWorkShopId())) {
            WorkShop workShop = workShopMapper.selectById(queryWorkPlanTaskVO.getWorkShopId());
            QueryWrapper<Workstation> workstationQueryWrapper = new QueryWrapper<>();
            workstationQueryWrapper.eq("up_area", workShop.getId());
            List<Workstation> workstationList = workstationMapper.selectList(workstationQueryWrapper);
            if(CommonUtil.isEmptyOrNull(workstationList)){
                return new ArrayList<>();
            }
            queryWrapper.in("station_code", workstationList.stream().map(Workstation::getStationCode).collect(Collectors.toList()));
        }
        //????????????????????????
        queryWrapper.orderByDesc("create_time");
        return workPlanTaskMapper.selectList(queryWrapper);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param workPlanTask
     * @return GanttChartStatusEnum
     */
    @Override
    public String generateTaskStatus(WorkPlanTask workPlanTask) {
        if (PlanTaskStatusEnum.SCHEDULED.getValue().equals(workPlanTask.getPlanStatus())) {
            return GanttChartStatusEnum.SCHEDULED.getValue();
        } else {
            if (PlanTaskExeStatusEnum.NOT_START.getValue().equals(workPlanTask.getExeStatus())) {
                return GanttChartStatusEnum.ISSUED.getValue();
            } else if (PlanTaskExeStatusEnum.PRODUCE.getValue().equals(workPlanTask.getExeStatus())) {
                return GanttChartStatusEnum.PRODUCE.getValue();
            } else if (PlanTaskExeStatusEnum.SUSPEND.getValue().equals(workPlanTask.getExeStatus())) {
                return GanttChartStatusEnum.SUSPEND.getValue();
            } else {
                return GanttChartStatusEnum.FINISH.getValue();
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param orderId
     * @param processId
     * @param seq
     * @return
     */
    private List<String> queryStationById(String orderId, String processId, Integer seq) {
        WorkOrder workOrder = workOrderMapper.selectById(orderId);
        QueryWrapper<WorkOrderLine> workOrderLineQueryWrapper = new QueryWrapper<>();
        workOrderLineQueryWrapper.eq("process_id", processId);
        workOrderLineQueryWrapper.eq("seq", seq);
        workOrderLineQueryWrapper.eq("order_id", workOrder.getId());
        WorkOrderLine one = workOrderLineService.getOne(workOrderLineQueryWrapper);
        String workflowType = workOrder.getWorkflowType();
        // ??????ID??????
        return workProcessTaskService.getWorkstationIds(one.getId(), workflowType, processId, seq);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateBatchWorkPlanTaskVO(List<WorkPlanTaskVO> workPlanTaskVOList) {
        workPlanTaskVOList.forEach(this::updateWorkPlanTask);
    }

    /**
     * ????????????
     *
     * @param workProduceTask
     * @param lstWorkProduceTaskEmployee
     */
    private void sendMsg(WorkProduceTask workProduceTask, List<WorkProduceTaskEmployee> lstWorkProduceTaskEmployee) {
        if (!CommonUtil.isEmptyOrNull(lstWorkProduceTaskEmployee)) {
            List<String> receiverIds = new ArrayList<>();
            lstWorkProduceTaskEmployee.forEach(workProduceTaskEmployee -> receiverIds.add(workProduceTaskEmployee.getEmployeeId()));
            MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, workProduceTask.getCreateBy(), workProduceTask);
            msgParamsVO.setTenantId(TenantContext.getTenant());
            msgHandleServer.sendMsg(MesCommonConstant.MSG_WORK_PRODUCE_TASK, MesCommonConstant.INFORM, msgParamsVO);
        }
    }
}
