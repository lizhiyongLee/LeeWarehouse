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
 * @Description: 派工单生产任务
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
     * 工单维度分布式锁前缀
     */
    private final String WORK_ORDER_LOCK_PREFIX = "work_order_";

    /**
     * 分布式锁过期时间
     */
    private final long EXPIRE_TIME = 50000L;

    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {
        String lockKey = WORK_ORDER_LOCK_PREFIX + workPlanTaskVO.getOrderId();
        String requestId = UUID.fastUUID().toString();
        try {
            // 获取分布式事务，获取不到则直接返回
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
        // 需要排程的
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
     * 添加单条排程
     *
     * @param workPlanTaskVO
     * @date 2020年12月2日
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void saveSingleWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {

        this.validateWorkPlanTask(workPlanTaskVO);

        // 生成单号
        String taskNo =
                codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.PLAN_TASK_NO, workPlanTaskVO);
        workPlanTaskVO.setTaskCode(taskNo);
        // 默认初始状态
        workPlanTaskVO.setExeStatus(PlanTaskExeStatusEnum.NOT_START.getValue());
        workPlanTaskVO.setPlanStatus(PlanTaskStatusEnum.SCHEDULED.getValue());
        // 设置工位信息
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
            // 获取分布式事务，获取不到则直接返回
            if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                throw new ILSBootException("P-OW-0011", workPlanTaskVO.getOrderNo(), workPlanTaskVO.getProcessCode());
            }

            this.validateWorkPlanTask(workPlanTaskVO);
            // 设置工位信息
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
            // 获取分布式事务，获取不到则直接返回
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
        // 取消排程
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
                        planTaskErrorVO.setErrMsg("同时存在多人调整，请稍后操作");
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
     * 取消单条排程任务
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskErrorVO
     * @date 2020年12月2日
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void cancelSinglePlanTask(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                      List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        // 只有已排程的单据可以取消
        if (PlanTaskStatusEnum.SCHEDULED.getValue().equals(workPlanTaskProcessVO.getPlanStatus())) {

            baseMapper.deleteById(workPlanTaskProcessVO.getId());
            workPlanTaskEmployeeMapper.deleteBytaskId(workPlanTaskProcessVO.getId());
            // 更新工序任务的已排程数量
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());
            QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
            WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
            queryWrapper.eq("id", workPlanTaskProcessVO.getProcessTaskId());
            updateWorkProcessTask.setScheduledQty(
                    BigDecimalUtils.subtract(workProcessTask.getScheduledQty(), workPlanTaskProcessVO.getPlanQty()));
            workProcessTaskService.update(updateWorkProcessTask, queryWrapper);
            // 更新工单状态
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
            planTaskErrorVO.setErrMsg("任务状态不对不能取消");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
        }
    }


    /**
     * 检验派工任务,会更新工单状态以及工序任务的已排程数量
     *
     * @param workPlanTaskVO
     * @date 2020年11月25日
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void validateWorkPlanTask(WorkPlanTaskVO workPlanTaskVO) {

        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskVO.getProcessTaskId());
        //oldPlanQty 派工修改之前的计划数，新增的是为零
        BigDecimal oldPlanQty = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(workPlanTaskVO.getId())) {
            WorkPlanTask oldWorkPlanTask = this.getById(workPlanTaskVO.getId());
            oldPlanQty = oldWorkPlanTask.getPlanQty();
        }

        // 累计的已排程-本派工原来计划数oldPlanQty
        BigDecimal processScheduleQty = BigDecimalUtils.subtract(workProcessTask.getScheduledQty(), oldPlanQty);
        BigDecimal planQty = workPlanTaskVO.getPlanQty();
        BigDecimal reayScheduleQty = BigDecimalUtils.subtract(workProcessTask.getPlanQty(), processScheduleQty);
        boolean bPlanQtyFlag = BigDecimalUtils.greaterThan(planQty, reayScheduleQty);
        if (bPlanQtyFlag) {
            throw new ILSBootException("P-OW-0008", workPlanTaskVO.getOrderNo(), workPlanTaskVO.getProcessCode());
        }

        // 更新工序任务的已排程数量
        QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        queryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask.setScheduledQty(BigDecimalUtils.add(processScheduleQty, planQty));
        workProcessTaskService.update(updateWorkProcessTask, queryWrapper);

        // 如果工单是计划状态更改为已排程状态
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
     * 保存用户
     *
     * @param workPlanTaskVO
     * @date 2020年11月24日
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
                    // 外键设置
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
        // 查询派工任务：按工单，工序顺序升序，时间升序 排列好的派工任务列表
        List<WorkPlanTaskProcessVO> lstWorkPlanTask = baseMapper.queryPlanWorkProcessAscList(idList);
        List<PlanTaskErrorVO> lstPlanTaskErrorVO = new ArrayList<PlanTaskErrorVO>();

        // 任务跟工单时间校验
        boolean bFlag = this.checkWorkOrderDate(lstWorkPlanTask, lstPlanTaskErrorVO, checkWorkOrder);
        if (!bFlag) {
            return this.getPlanTaskErrorMsgVO(MesCommonConstant.MESSAGE_CONFIRM, lstPlanTaskErrorVO);
        }

        // 所相关变量定义
        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;


        // 逐条判断下发逻辑以及更新
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
                        planTaskErrorVO.setErrMsg("同时存在多人调整，请稍后操作");
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
     * @return true 满足条件 false 不满足条件
     * @date 2020年11月30日
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
     * 派工任务下发逻辑校验以及回写状态
     *
     * @param workPlanTask
     * @date 2020年11月26日
     */
    private void validatePlanTaskIssue(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                       List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        boolean bResult = false;
        // 判断状态问题
        String planStatus = workPlanTaskProcessVO.getPlanStatus();
        if (!PlanTaskStatusEnum.SCHEDULED.getValue().equals(planStatus)) {
            PlanTaskErrorVO planTaskErrorVO = new PlanTaskErrorVO();
            planTaskErrorVO.setTaskCode(workPlanTaskProcessVO.getTaskCode());
            planTaskErrorVO.setOrderNo(workPlanTaskProcessVO.getOrderNo());
            planTaskErrorVO
                    .setProcessInfo(workPlanTaskProcessVO.getSeq() + "/" + workPlanTaskProcessVO.getProcessCode());
            planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
            planTaskErrorVO.setErrMsg("任务状态不对不能操作");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
            return;
        }

        // 获取本次任务的上一道工序任务
        String processTaskPriorCode = workPlanTaskProcessVO.getPriorCode();

        // 本次任务对应的工序任务
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());

        if (!MesCommonConstant.ROUTE_PROCESS_FIRST.equals(processTaskPriorCode)) {
            // 不是第一道工序任务需要校验是否超过前道工序数量总和
            bResult = this.issueCheckPreProcessSum(workPlanTaskProcessVO, workProcessTask, lstPlanTaskErrorVO);
            if (!bResult) {
                return;
            }

            String linkType = workPlanTaskProcessVO.getLinkType();
            // 1,前续开始后续可开始2，前续结束后续可开始
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

        // 工位的逻辑校验
        bResult = this.issueCheckStation(workPlanTaskProcessVO, lstPlanTaskErrorVO);
        if (!bResult) {
            return;
        }

        // 派工任务下发执行的数据更新
        this.issueUpdate(workPlanTaskProcessVO);
    }

    /**
     * 派工任务下发执行前一道工序生产总数校验
     *
     * @param workPlanTaskProcessVO      派工任务
     * @param workProcessTask工序任务
     * @param lstPlanTaskIssueErrorMsgVO
     * @return true 满足 false 不满足
     * @date 2020年11月30日
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
            planTaskErrorVO.setErrMsg("前道工序产出数量下发不足");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 下发时检验接续方式为前续开始后续可开始
     *
     * @param workPlanTaskProcessVO
     * @param workProcessTask
     * @param lstPlanTaskIssueErrorMsgVO
     * @return
     * @date 2020年11月30日
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
            // 取前序工序里面大于本道任务计划时间的最近的任务
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
     * 下发时检验接续方式为前续结束后续可开始
     *
     * @param workPlanTaskProcessVO
     * @param workProcessTask
     * @param lstPlanTaskIssueErrorMsgVO
     * @return
     * @date 2020年11月30日
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
            // 取前序工序里面大于本道任务计划时间的最近的任务
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
     * 根据任务
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
            planTaskErrorVO.setErrMsg("前道工序产出数量下发不足");
        } else {
            planTaskErrorVO.setErrMsg("和前道工序接续任务" + lstWorkPlanTask.get(0).getTaskCode() + "发生时间冲突");
        }
        lstPlanTaskErrorVO.add(planTaskErrorVO);
        return false;
    }


    /**
     * 派工任务下发执行工位检查
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskIssueErrorMsgVO
     * @return true 满足 false 不满足
     * @date 2020年11月30日
     */
    private boolean issueCheckStation(WorkPlanTaskProcessVO workPlanTaskProcessVO,
                                      List<PlanTaskErrorVO> lstPlanTaskErrorVO) {
        String stationId = workPlanTaskProcessVO.getStationId();
        Workstation workstation = workstationService.getById(stationId);
        String multiStation = workstation.getMultistation();
        // 如果是多工位的不执行时间冲突检查
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
                planTaskErrorVO.setErrMsg(taskCode + "和当前工位的任务时间安排发生冲突");
                lstPlanTaskErrorVO.add(planTaskErrorVO);
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 派工任务下发执行的数据更新
     *
     * @param workPlanTaskProcessVO
     * @date 2020年11月27日
     */
    private void issueUpdate(WorkPlanTaskProcessVO workPlanTaskProcessVO) {

        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());
        // 更新工序任务的已下发数量
        QueryWrapper<WorkProcessTask> workProcessTaskQueryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        workProcessTaskQueryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask
                .setPublishQty(BigDecimalUtils.add(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty()));
        workProcessTaskService.update(updateWorkProcessTask, workProcessTaskQueryWrapper);
        // 更新派工任务状态
        QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
        WorkPlanTask updateWorkPlanTask = new WorkPlanTask();
        workPlanTaskQueryWrapper.eq("id", workPlanTaskProcessVO.getId());
        updateWorkPlanTask.setDistrubuteTime(new Date());
        updateWorkPlanTask.setPlanStatus(PlanTaskStatusEnum.ISSUED.getValue());
        this.update(updateWorkPlanTask, workPlanTaskQueryWrapper);
        // 工单是已排程-》已下发
        WorkOrder workOrder = workOrderService.getById(workPlanTaskProcessVO.getOrderId());
        if (WorkOrderStatusEnum.SCHEDULED.getValue().equals(workOrder.getStatus())) {
            QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
            WorkOrder updateWorkOrder = new WorkOrder();
            workOrderQueryWrapper.eq("id", workOrder.getId());
            updateWorkOrder.setStatus(WorkOrderStatusEnum.ISSUED.getValue());
            workOrderService.update(updateWorkOrder, workOrderQueryWrapper);
        }

        // 生成执行任务
        this.generateProduceTask(workPlanTaskProcessVO);
    }

    /**
     * 生成生产任务
     *
     * @param workPlanTaskProcessVO
     * @date 2020年12月8日
     */
    private void generateProduceTask(WorkPlanTaskProcessVO workPlanTaskProcessVO) {
        //获取标准生产作业流程全局开关
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.SOP_TEMPLATE_SWITCH);
        String switchIdent = bizConfig.getConfigValue();

        // 获取计划人员
        QueryWrapper<WorkPlanTaskEmployee> queryEmployee = new QueryWrapper<>();
        queryEmployee.eq("task_id", workPlanTaskProcessVO.getId());
        List<WorkPlanTaskEmployee> lstWorkPlanTaskEmployee = workPlanTaskEmployeeMapper.selectList(queryEmployee);

        // 执行任务
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

        //根据任务建立sop步骤和控件
        if (null != switchIdent && switchIdent.equals(ZeroOrOneEnum.ONE.getStrCode())) {
            insertSop(workPlanTaskProcessVO, workProduceTask);
        }

        //保存自定义参数
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
     * 根据任务建立sop步骤和控件
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

        //先给sopTemplateSteps倒序排序，然后设置下一个步骤的id
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
            //插入完成之后，跟新下个步骤id
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
        // 查询派工任务：按工单，工序顺序降序，时间降序 排列好的派工任务列表
        List<WorkPlanTaskProcessVO> lstWorkPlanTask = baseMapper.queryPlanWorkProcessDescList(idList);
        // 撤回不成功单据记录
        List<PlanTaskErrorVO> lstPlanTaskErrorVO = new ArrayList<PlanTaskErrorVO>();

        // 所相关变量定义
        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;

        // 逐条判断下发逻辑以及更新
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
                        planTaskErrorVO.setErrMsg("同时存在多人调整，请稍后操作");
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
     * 撤回
     *
     * @param workPlanTaskProcessVO
     * @param lstPlanTaskIssueErrorMsgVO
     * @date 2020年11月27日
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
            planTaskErrorVO.setErrMsg("任务状态不对不能取消");
            lstPlanTaskErrorVO.add(planTaskErrorVO);
        } else {
            // 获取下一道工序任务编码
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
                    planTaskErrorVO.setErrMsg("后道工序的接续任务" + lstWorkPlanTask.get(0).getTaskCode() + "需先撤回");
                    planTaskErrorVO.setPlanQty(workPlanTaskProcessVO.getPlanQty());
                    lstPlanTaskErrorVO.add(planTaskErrorVO);
                }
            }
        }
    }

    /**
     * 撤回任务更新数据
     *
     * @param workPlanTaskProcessVO
     * @date 2020年11月27日
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void undoIssueUpdate(WorkPlanTaskProcessVO workPlanTaskProcessVO) {
        // 更新派工任务状态
        QueryWrapper<WorkPlanTask> workPlanTaskQueryWrapper = new QueryWrapper<>();
        WorkPlanTask updateWorkPlanTask = new WorkPlanTask();
        workPlanTaskQueryWrapper.eq("id", workPlanTaskProcessVO.getId());
        updateWorkPlanTask.setPlanStatus(PlanTaskStatusEnum.SCHEDULED.getValue());
        this.update(updateWorkPlanTask, workPlanTaskQueryWrapper);

        // 更新工序任务的已下发数量
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTaskProcessVO.getProcessTaskId());
        QueryWrapper<WorkProcessTask> workProcessTaskQueryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        workProcessTaskQueryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask
                .setPublishQty(
                        BigDecimalUtils.subtract(workProcessTask.getPublishQty(), workPlanTaskProcessVO.getPlanQty()));
        workProcessTaskService.update(updateWorkProcessTask, workProcessTaskQueryWrapper);

        // 更新订单状态
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

        // 删除已经生产的执行任务
        this.undoProduceTask(workPlanTaskProcessVO);
    }

    /**
     * 删除派工任务对应的执行任务
     *
     * @param workPlanTaskProcessVO
     * @date 2020年12月8日
     */
    private void undoProduceTask(WorkPlanTaskProcessVO workPlanTaskProcessVO) {
        QueryWrapper<WorkProduceTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("plan_task_id", workPlanTaskProcessVO.getId());
        List<WorkProduceTask> lstWorkProduceTask = workProduceTaskService.list(taskQueryWrapper);
        // 删除执行任务
        workProduceTaskService.deleteByPlanTaskId(workPlanTaskProcessVO.getId());
        if (!CommonUtil.isEmptyOrNull(lstWorkProduceTask)) {
            for (WorkProduceTask task : lstWorkProduceTask) {
                // 删除执行人员
                workProduceTaskEmployeeMapper.deleteByExcuteTaskId(task.getId());
                //删除sop步骤和控件
                sopStepMapper.deleteByRelatedTaskId(task.getId());
                sopControlMapper.deleteByRelatedTaskId(task.getId());
            }
        }
    }

    /**
     * 构建返回msgVO
     *
     * @param errCode
     * @param lstPlanTaskErrorVO
     * @return
     * @date 2020年11月30日
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
        //根据工位建立甘特图VO
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
            //工位类型：1多任务，0单任务
            Workstation workstation = workstationMapper.selectById(workPlanTask.getStationId());
            ganttChartVO.setMultistation(workstation.getMultistation());
            //起止时间
            Map<String, Long> dateMap = new HashMap<>(3);
            dateMap.put("start", workPlanTask.getPlanStartTime().getTime());
            dateMap.put("end", workPlanTask.getPlanEndTime().getTime());
            //临时时间，用于优化甘特图显示，将开始时间延迟1秒，不作为业务操作依据
            dateMap.put("temp", workPlanTask.getPlanStartTime().getTime() + 1000L);
            ganttChartVO.setTime(dateMap);
            //可选工位
            ganttChartVO.setOptionalStationId(this.queryStationById(workPlanTask.getOrderId(), workPlanTask.getProcessId(), workPlanTask.getSeq()));
            //planStatus+exeStatus组合展示状态
            ganttChartVO.setStatus(generateTaskStatus(workPlanTask));
            ganttChartVOList.add(ganttChartVO);
        }
        return ganttChartVOList;
    }

    /**
     * 根据查询条件查询甘特图
     *
     * @param queryWorkPlanTaskVO
     * @return
     */
    private List<WorkPlanTask> getWorkPlanTaskList(WorkPlanTaskVO queryWorkPlanTaskVO) {
        QueryWrapper<WorkPlanTask> queryWrapper = new QueryWrapper<>();
        //甘特图不需要取消的任务
        queryWrapper.ne("exe_status", PlanTaskExeStatusEnum.CANCEL.getValue());
        //工单号、物料名称、任务状态（plan_status+exe_status）
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
        //工位支持多匹配
        String stationCode = queryWorkPlanTaskVO.getStationCode();
        if (StringUtils.isNotBlank(stationCode)) {
            String[] stationCodeList = stationCode.split(",");
            queryWrapper.in("station_code", Arrays.asList(stationCodeList));
        }
        //销售订单号查询
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
        //开始和结束时间
        if (queryWorkPlanTaskVO.getPlanStartTime() != null && queryWorkPlanTaskVO.getPlanEndTime() != null) {
            queryWrapper.between("plan_start_time", queryWorkPlanTaskVO.getPlanStartTime(), queryWorkPlanTaskVO.getPlanEndTime());
            queryWrapper.between("plan_end_time", queryWorkPlanTaskVO.getPlanStartTime(), queryWorkPlanTaskVO.getPlanEndTime());
        }
        //车间查询
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
        //根据创建时间倒序
        queryWrapper.orderByDesc("create_time");
        return workPlanTaskMapper.selectList(queryWrapper);
    }

    /**
     * 根据任务执行情况组装任务状态
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
     * 查询可用工位列表
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
        // 工位ID集合
        return workProcessTaskService.getWorkstationIds(one.getId(), workflowType, processId, seq);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateBatchWorkPlanTaskVO(List<WorkPlanTaskVO> workPlanTaskVOList) {
        workPlanTaskVOList.forEach(this::updateWorkPlanTask);
    }

    /**
     * 消息发送
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
