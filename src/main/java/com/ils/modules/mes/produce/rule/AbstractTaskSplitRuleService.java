package com.ils.modules.mes.produce.rule;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.common.util.DateUtils;
import com.ils.common.util.SpringContextUtils;
import com.ils.modules.mes.base.factory.entity.Shift;
import com.ils.modules.mes.base.factory.service.ShiftService;
import com.ils.modules.mes.base.factory.service.WorkCalendarService;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;
import com.ils.modules.mes.base.schedule.entity.SchedulePrepareTime;
import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;
import com.ils.modules.mes.base.schedule.service.SchedulePrepareTimeService;
import com.ils.modules.mes.base.schedule.service.ScheduleStandardProductionVolumeService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.service.WorkOrderLineService;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.produce.util.AutoScheContextEnv;
import com.ils.modules.mes.produce.util.AutoTimeUtils;
import com.ils.modules.mes.produce.vo.AutoScheShiftVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkstationVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;
import com.ils.modules.system.service.CodeGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 拆分规则接口
 * @author: fengyi
 */
@Slf4j
public abstract class AbstractTaskSplitRuleService {

    protected WorkOrderLineService workOrderLineService;
    protected ShiftService shiftService;
    protected ScheduleStandardProductionVolumeService scheduleStandardProductionVolumeService;
    protected WorkCalendarService workCalendarService;
    protected SchedulePrepareTimeService schedulePrepareTimeService;
    protected CodeGeneratorService codeGeneratorService;
    protected WorkOrderService workOrderService;
    protected WorkPlanTaskService workPlanTaskService;
    protected WorkProcessTaskService workProcessTaskService;

    public AbstractTaskSplitRuleService() {
        workOrderLineService = SpringContextUtils.getBean(WorkOrderLineService.class);
        shiftService = SpringContextUtils.getBean(ShiftService.class);
        scheduleStandardProductionVolumeService = SpringContextUtils.getBean(ScheduleStandardProductionVolumeService.class);
        workCalendarService = SpringContextUtils.getBean(WorkCalendarService.class);
        schedulePrepareTimeService = SpringContextUtils.getBean(SchedulePrepareTimeService.class);
        codeGeneratorService = SpringContextUtils.getBean(CodeGeneratorService.class);
        workOrderService = SpringContextUtils.getBean(WorkOrderService.class);
        workPlanTaskService = SpringContextUtils.getBean(WorkPlanTaskService.class);
        workProcessTaskService = SpringContextUtils.getBean(WorkProcessTaskService.class);
    }

    /**
     * 拆分规则接口
     *
     * @param autoScheContextEnv
     */
    public abstract void splitTask(AutoScheContextEnv autoScheContextEnv);

    /**
     * 获取工艺路线工序的设置
     *
     * @param autoScheWorkProcessVO
     * @return
     */
    protected WorkOrderLine getWorkOrderLine(AutoScheWorkProcessVO autoScheWorkProcessVO) {
        QueryWrapper<WorkOrderLine> queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", autoScheWorkProcessVO.getOrderId());
        queryWrapper.eq("process_id", autoScheWorkProcessVO.getProcessId());
        queryWrapper.eq("seq", autoScheWorkProcessVO.getSeq());
        return workOrderLineService.getOne(queryWrapper);
    }

    /**
     * 获取最早结束工位
     *
     *
     * @param autoScheWorkProcessVO
     * @param lstAutoScheWorkstationVO
     * @return
     */
    public AutoScheWorkstationVO getMinTimeWorkstation(AutoScheWorkProcessVO autoScheWorkProcessVO, List<AutoScheWorkstationVO> lstAutoScheWorkstationVO) {

        if (CollectionUtil.isEmpty(lstAutoScheWorkstationVO)) {
            throw new ILSBootException("P-AU-1001", autoScheWorkProcessVO.getOrderNo(), autoScheWorkProcessVO.getFullItem(), autoScheWorkProcessVO.getFullProcess());
        }

        if (lstAutoScheWorkstationVO.size() == 1) {
            return lstAutoScheWorkstationVO.get(0);
        }
        AutoScheWorkstationVO autoScheWorkstationVO = lstAutoScheWorkstationVO.get(0);
        for (int j = 1; j < lstAutoScheWorkstationVO.size(); j++) {
            AutoScheWorkstationVO tempVO = lstAutoScheWorkstationVO.get(j);
            if (tempVO.getMaxTime() < autoScheWorkstationVO.getMaxTime()) {
                autoScheWorkstationVO = tempVO;
            }
        }
        return autoScheWorkstationVO;
    }

    /**
     * 获取班次
     */
    protected Map<String, AutoScheShiftVO> getShift() {
        QueryWrapper<Shift> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        List<Shift> lstShift = shiftService.list(queryWrapper);
        Map<String, AutoScheShiftVO> shiftMap = lstShift.stream().map(shift -> {
            AutoScheShiftVO autoScheShiftVO = new AutoScheShiftVO();
            BeanUtils.copyProperties(shift, autoScheShiftVO);
            autoScheShiftVO.setShiftDuration(AutoTimeUtils.getTimeDuration(shift.getShiftStartTime(), shift.getShiftEndTime()));
            return autoScheShiftVO;
        }).collect(Collectors.toMap(AutoScheShiftVO::getId, Function.identity()));
        return shiftMap;
    }

    /**
     * 获取标准工位标准产能
     *
     * @param autoScheWorkProcessVO
     * @param autoScheWorkstationVO
     * @return
     */
    protected ScheduleStandardProductionVolume getStandardProductionVolume(AutoScheWorkProcessVO autoScheWorkProcessVO,
                                                                           AutoScheWorkstationVO autoScheWorkstationVO) {
        QueryWrapper<ScheduleStandardProductionVolume> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        queryWrapper.eq("item_id", autoScheWorkProcessVO.getItemId());
        queryWrapper.eq("process_id", autoScheWorkProcessVO.getProcessId());
        queryWrapper.eq("station_id", autoScheWorkstationVO.getId());
        queryWrapper.eq("process_seq", autoScheWorkProcessVO.getSeq());
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(autoScheWorkProcessVO.getWorkflowType())) {
            // 建工单工序任务时选择的如果是bom 则对应bom
            queryWrapper.eq("product_bom_id", autoScheWorkProcessVO.getProductId());
            queryWrapper.eq("standard_type", StandardTypeEnum.PRODUCT_BOM.getValue());
        } else {
            // 建工单工序任务时选择的如果是非bom 则对应工艺路线
            queryWrapper.eq("route_id", autoScheWorkProcessVO.getRouteId());
            queryWrapper.eq("standard_type", StandardTypeEnum.ROUTE.getValue());
        }

        queryWrapper.orderByDesc("id");

        List<ScheduleStandardProductionVolume> lstVolume = scheduleStandardProductionVolumeService.list(queryWrapper);
        if (!CommonUtil.isEmptyOrNull(lstVolume)) {
            return lstVolume.get(0);
        } else {
            // 如果为空 则再次按照物料 工序  工位 查找是否存在通用标准产能（工序）
            QueryWrapper<ScheduleStandardProductionVolume> processQueryWrapper = new QueryWrapper<>();
            processQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
            processQueryWrapper.eq("item_id", autoScheWorkProcessVO.getItemId());
            processQueryWrapper.eq("process_id", autoScheWorkProcessVO.getProcessId());
            processQueryWrapper.eq("station_id", autoScheWorkstationVO.getId());
            processQueryWrapper.eq("process_seq", autoScheWorkProcessVO.getSeq());
            processQueryWrapper.eq("standard_type", StandardTypeEnum.PROCESS.getValue());

            lstVolume = scheduleStandardProductionVolumeService.list(processQueryWrapper);
            if (!CommonUtil.isEmptyOrNull(lstVolume)) {
                return lstVolume.get(0);
            } else {

                // 如果没有 抛异常
                throw new ILSBootException("P-AU-0066", autoScheWorkProcessVO.getFullItem(), autoScheWorkstationVO.getFullStation(), autoScheWorkProcessVO.getFullProcess());
            }
        }
    }

    /**
     * 根据产能计算taskSum数量所需毫秒时长
     *
     * @param stdProVolume
     * @param schePlanQty
     * @return
     */
    protected Long calTimeDurationByStdVolume(ScheduleStandardProductionVolume stdProVolume, BigDecimal schePlanQty) {
        // 每一个设置产能的需要的毫秒时长
        Long t1 = AutoTimeUtils.convertTime(stdProVolume.getTimePeriod(), stdProVolume.getTimeUnit());

        BigDecimal totalDuration =
                BigDecimalUtils.divide(BigDecimalUtils.multiply(schePlanQty, new BigDecimal(t1)), stdProVolume.getQty(), 0);

        log.info("生产{}个需要时间(ms): {}, 计划数量: {}, 生产数量持续时长(ms): {} ### {}", stdProVolume.getQty(), printMillisecondsStr(t1), schePlanQty, totalDuration, printMillisecondsStr(totalDuration.longValue()));

        return totalDuration.longValue();
    }


    /**
     * 获取基准时间
     *
     * @param autoScheContextEnv    全局上下文变量
     * @param lstWorkPlanTask       自动排程结果
     * @param autoScheWorkProcessVO 当前工序
     * @param autoScheWorkstationVO 当前工位
     * @author niushuai
     * @date: 2021/6/16 9:04:03
     * @return: {@link Date}
     */
    protected Date getScheBaseTime(AutoScheContextEnv autoScheContextEnv, List<WorkPlanTask> lstWorkPlanTask, AutoScheWorkProcessVO autoScheWorkProcessVO, AutoScheWorkstationVO autoScheWorkstationVO) {
        Date scheBaseTime;
        if (MesCommonConstant.ROUTE_PROCESS_FIRST.equals(autoScheWorkProcessVO.getPriorCode())) {
            // 获取工作日历的排程基准时间
            scheBaseTime = this.getFirstScheBaseTime(autoScheContextEnv.getAutoScheParam().getBaseTime(), autoScheWorkstationVO);
            log.info("首工序: {}", DateUtil.formatDateTime(scheBaseTime));
        } else {
            scheBaseTime = this.getNotFirstScheBaseTime(autoScheContextEnv, lstWorkPlanTask, autoScheWorkProcessVO, autoScheWorkstationVO);
            log.info("非首工序: {}", DateUtil.formatDateTime(scheBaseTime));
        }
        return scheBaseTime;
    }


    /**
     * 获取排程基准时间 - 首工序
     *
     * @param baseTime
     * @param autoScheWorkstationVO
     * @return
     */
    protected Date getFirstScheBaseTime(Date baseTime, AutoScheWorkstationVO autoScheWorkstationVO) {
        // 判断基准时间
        Long t1 = baseTime.getTime();
        if (autoScheWorkstationVO.getMaxTime() > t1
                && ZeroOrOneEnum.ZERO.getStrCode().equals(autoScheWorkstationVO.getMultistation())) {
            return new Date(autoScheWorkstationVO.getMaxTime());
        } else {
            return baseTime;
        }
    }

    /**
     * 获取排程基准时间 - 非首工序
     *
     * @param autoScheContextEnv
     * @param lstWorkPlanTask
     * @param currWorkProcess
     * @param currWorkstation
     * @author niushuai
     * @date: 2021/6/7 13:50:05
     * @return: {@link Date}
     */
    protected Date getNotFirstScheBaseTime(AutoScheContextEnv autoScheContextEnv, List<WorkPlanTask> lstWorkPlanTask, AutoScheWorkProcessVO currWorkProcess, AutoScheWorkstationVO currWorkstation) {
        /*
         非首工序
            判断接续方式 linkType 得到临时开始时间1
                1 前续开始后续可开始
                    查询该工序的上一个工序的开始时间作为当前工序的开始时间
                2 前续结束后续可开始
                    查询该工序的上一个工序的结束时间作为当前工序的开始时间
            上述得到的开始时间为临时基准时间 仍然需要和基准时间做对比
         */
        // ProcessLineTypeEnum.LINK_TYPE_1  前序开始后序开始
        // ProcessLineTypeEnum.LINK_TYPE_2  前序结束后序开始

        Date baseTime = autoScheContextEnv.getAutoScheParam().getBaseTime();
        log.info("baseTime, 基准时间: {}", DateUtil.formatDateTime(baseTime));

        // 查找当前所排工序中是否存在前工序
        String processTaskId = currWorkProcess.getPriorCode();

        // 如果能找到数据 则必然只有一条
        List<AutoScheWorkProcessVO> processTaskListOfPage = autoScheContextEnv.getLstAutoScheWorkProcessVO().
                stream().filter(item -> item.getId().equals(processTaskId)).collect(Collectors.toList());

        log.info("当前排成工序任务id:{}, 使用工序id:{}, 使用工序name:{}, 是否为多工位:{} ", currWorkProcess.getId(), currWorkProcess.getProcessId(), currWorkProcess.getProcessName(), currWorkstation.getMultistation());
        Date previousProcessMinStartDate = null, previousProcessMaxEndDate = null, tmpDate = baseTime;
        if (CollectionUtil.isNotEmpty(processTaskListOfPage)) {
            // 非空 获取前一工序已排任务的开始时间
            List<WorkPlanTask> planedTaskList = lstWorkPlanTask.stream().filter(item -> item.getProcessTaskId().equals(processTaskId)).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(planedTaskList)) {
                previousProcessMinStartDate = planedTaskList.stream().sorted(Comparator.comparing(WorkPlanTask::getPlanStartTime)).collect(Collectors.toList()).get(0).getPlanStartTime();
                previousProcessMaxEndDate = planedTaskList.stream().sorted(Comparator.comparing(WorkPlanTask::getPlanEndTime)).collect(Collectors.toList()).get(planedTaskList.size() - 1).getPlanEndTime();
            }

            // 当前工序的接续方式
            String linkType = currWorkProcess.getLinkType();
            if (ProcessLineTypeEnum.LINK_TYPE_1.getValue().equals(linkType)) {

                // 前序开始后续开始 取前一工序计划开始时间
                tmpDate = previousProcessMinStartDate;
            } else if (ProcessLineTypeEnum.LINK_TYPE_2.getValue().equals(linkType)) {

                // 前序结束后续开始 取前一工序计划结束时间
                tmpDate = previousProcessMaxEndDate;
            } else {
                throw new ILSBootException("P-AU-0080", currWorkProcess.getProcessName(), linkType);
            }

            if (ZeroOrOneEnum.ONE.getStrCode().equals(currWorkstation.getMultistation())) {
                // 是多工位 使用tmin与基准时间比较 返回时间较大的一个作为新的基准时间
                return tmpDate.getTime() > baseTime.getTime() ? tmpDate : baseTime;
            } else {
                // 非多工位 使用当前工位的最晚开始时间tmax与当前工位的开始时间做比较 返回时间较大的一个作为新的基准时间
                return currWorkstation.getMaxTime() > tmpDate.getTime() ? new Date(currWorkstation.getMaxTime()) : tmpDate;
            }
        }


        // 判断是否是多工位
        if (ZeroOrOneEnum.ONE.getStrCode().equals(currWorkstation.getMultistation())) {
            return baseTime;
        } else {
            return currWorkstation.getMaxTime() > baseTime.getTime() ? new Date(currWorkstation.getMaxTime()) : baseTime;
        }

    }


    /**
     * 获取动态准备时间
     *
     * @param autoScheWorkstationVO
     * @param itemId
     * @return
     */
    protected long getPrepareTime(AutoScheWorkstationVO autoScheWorkstationVO, String itemId) {
        // 判断基准时间
        if (StringUtils.isBlank(autoScheWorkstationVO.getItemId())
                || autoScheWorkstationVO.getItemId().equals(itemId)) {
            return 0L;
        }

        QueryWrapper<SchedulePrepareTime> queryWrapper = new QueryWrapper();

        queryWrapper.eq("station_id", autoScheWorkstationVO.getId());
        queryWrapper.eq("source_item_id", autoScheWorkstationVO.getItemId());
        queryWrapper.eq("destination_item_id", itemId);
        List<SchedulePrepareTime> lstSchedulePrepare = schedulePrepareTimeService.list(queryWrapper);
        if (!CommonUtil.isEmptyOrNull(lstSchedulePrepare)) {
            SchedulePrepareTime schedulePrepareTime = lstSchedulePrepare.get(0);
            return AutoTimeUtils.convertTime(schedulePrepareTime.getPrepareDuration(),
                    schedulePrepareTime.getUnitName());
        } else {
            return 0L;
        }
    }

    /**
     * 获取工作日历
     *
     * @param baseTime
     * @param autoScheWorkstationVO
     * @return
     */
    protected List<WorkCalendarVO> getWorkCalendarList(Date baseTime, AutoScheWorkstationVO autoScheWorkstationVO, boolean isFirst) {
        // 分别获取基准时间的日期，以及时间
        String strDate = DateUtils.formatDate(baseTime, AutoTimeUtils.DATE_PATTERN);
        String strTime = DateUtils.formatDate(baseTime, AutoTimeUtils.TIME_PATTERN);
        try {
            Date baseDate = DateUtils.parseDate(strDate, AutoTimeUtils.DATE_PATTERN);
            QueryWrapper<WorkCalendarVO> queryWrapper = new QueryWrapper<WorkCalendarVO>();
            queryWrapper.eq("a.is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            queryWrapper.eq("a.type", WorkCalendarTypeEnum.PRODUCTION.getValue());
            queryWrapper.eq("a.station_id", autoScheWorkstationVO.getId());
            if (isFirst) {
                queryWrapper.and(wrapper -> wrapper
                                // 跨天的
                                .nested(i -> i.eq("a.workdate", baseDate).apply("b.shift_start_time>b.shift_end_time"))
                                .or()
                                // 不跨天 且结束时间在基准时间之后的
                                .nested(i -> i.eq("a.workdate", baseDate).gt("b.shift_end_time", strTime))
                        // .or().nested(i -> i.gt("a.workdate", baseDate))
                );
            } else {
                queryWrapper.eq("a.workdate", baseDate);
            }
            queryWrapper.orderByAsc("a.workdate");
            queryWrapper.orderByAsc("b.shift_start_time");
            List<WorkCalendarVO> lstWorkCalendarVO = workCalendarService.queryWorkCalendarList(queryWrapper);

            return lstWorkCalendarVO;
        } catch (ParseException e) {
            throw new ILSBootException("P-AU-0067");
        }
    }

    /**
     * 根据工位id查询在给定的基准时间之后的工作日历
     *
     * @param scheBaseTime 计算后的基准时间
     * @param stationId    工位id
     * @author niushuai
     * @date: 2021/6/4 13:35:10
     * @return: {@link List<WorkCalendarVO>}
     */
    protected List<WorkCalendarVO> getWorkCalendarListNew(Date scheBaseTime, String stationId) {

        String strDate = DateUtils.formatDate(scheBaseTime, AutoTimeUtils.DATE_PATTERN);
        String strTime = DateUtils.formatDate(scheBaseTime, AutoTimeUtils.TIME_PATTERN);

        QueryWrapper<WorkCalendarVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a.is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.eq("a.type", WorkCalendarTypeEnum.PRODUCTION.getValue());
        queryWrapper.eq("a.station_id", stationId);

        queryWrapper.and(
                wrapper -> wrapper
                        .nested(item -> item.eq("a.workdate", strDate).gt("b.shift_end_time", strTime))
                        .or()
                        .nested(item -> item.eq("a.workdate", strDate).apply("b.shift_start_time > b.shift_end_time"))
                        .or()
                        .nested(item -> item.gt("a.workdate", strDate))
        );

        queryWrapper.orderByAsc("a.workdate");
        queryWrapper.orderByAsc("b.shift_start_time");

        List<WorkCalendarVO> lstWorkCalendarVO = workCalendarService.queryWorkCalendarList(queryWrapper);

        Optional.ofNullable(lstWorkCalendarVO).ifPresent(item -> item.forEach(WorkCalendarVO::setShiftFullDateTime));
        if (CollectionUtil.isNotEmpty(lstWorkCalendarVO)) {
            lstWorkCalendarVO.forEach(WorkCalendarVO::setShiftFullDateTime);
        }

        return lstWorkCalendarVO;
    }

    /**
     * 生成计划任务
     *
     * @param autoScheWorkstationVO
     * @param autoScheWorkProcessVO
     * @return
     */
    protected WorkPlanTask generatePlanTask(AutoScheWorkstationVO autoScheWorkstationVO, AutoScheWorkProcessVO autoScheWorkProcessVO) {
        WorkPlanTask workPlanTask = new WorkPlanTask();
        workPlanTask.setTenantId(CommonUtil.getTenantId());

        // 工位信息
        workPlanTask.setStationId(autoScheWorkstationVO.getId());
        workPlanTask.setStationCode(autoScheWorkstationVO.getStationCode());
        workPlanTask.setStationName(autoScheWorkstationVO.getStationName());

        // 工序任务信息
        workPlanTask.setProcessTaskId(autoScheWorkProcessVO.getId());
        workPlanTask.setOrderId(autoScheWorkProcessVO.getOrderId());
        workPlanTask.setOrderNo(autoScheWorkProcessVO.getOrderNo());

        // 批次号
        workPlanTask.setBatchNo(autoScheWorkProcessVO.getBatchNo());

        // 工序信息
        workPlanTask.setProcessId(autoScheWorkProcessVO.getProcessId());
        workPlanTask.setProcessCode(autoScheWorkProcessVO.getProcessCode());
        workPlanTask.setProcessName(autoScheWorkProcessVO.getProcessName());

        // 顺序
        workPlanTask.setSeq(autoScheWorkProcessVO.getSeq());

        // 产品信息
        workPlanTask.setItemId(autoScheWorkProcessVO.getItemId());
        workPlanTask.setItemCode(autoScheWorkProcessVO.getItemCode());
        workPlanTask.setItemName(autoScheWorkProcessVO.getItemName());

        // 其他信息
        workPlanTask.setLockStatus(ZeroOrOneEnum.ZERO.getStrCode());
        workPlanTask.setStockCheck(ZeroOrOneEnum.ZERO.getStrCode());
        workPlanTask.setExeStatus(PlanTaskExeStatusEnum.NOT_START.getValue());
        workPlanTask.setPlanStatus(PlanTaskStatusEnum.SCHEDULED.getValue());
        workPlanTask.setTotalQty(BigDecimal.ZERO);
        workPlanTask.setPlanQty(autoScheWorkProcessVO.getSchePlanQty());
        // 指定用户方式
        workPlanTask.setUserType(ProduceUserTypeEnum.SHIFT.getValue());

        return workPlanTask;

    }

    public void batchGenerateCode(List<WorkPlanTask> waitSavingWorkPlanTaskList) {
        if (CollectionUtil.isEmpty(waitSavingWorkPlanTaskList)) {
            throw new ILSBootException("P-AU-0082");
        }
        waitSavingWorkPlanTaskList.forEach(item -> item.setTaskCode(codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.PLAN_TASK_NO, item)));
    }

    /**
     * <pre>
     *     保存以及保存相关操作处理
     *      1、保存 work_plan_task
     *      2、更新已排数量和剩余数量 mes_work_process_task
     *      3、更新计划工单的状态 mes_work_order
     *
     * </pre>
     *
     * @param waitSavingWorkPlanTaskList
     * @author niushuai
     * @date: 2021/6/9 17:55:29
     * @return: void
     */
    public void finalDealPlanTask(List<WorkPlanTask> waitSavingWorkPlanTaskList) {

        // 更新计划工单状态由未排产变更为已排产
        // mes_work_order
        this.updateStatus(waitSavingWorkPlanTaskList);
        // 更新待排程工单的排程数量
        // mes_work_process_task
        this.updateNum(waitSavingWorkPlanTaskList);
        // 将拆分后的任务按顺序插入数据库
        // 统一生成code
        this.batchGenerateCode(waitSavingWorkPlanTaskList);
        // 生成已排程任务
        // mes_work_plan_task
        workPlanTaskService.saveBatch(waitSavingWorkPlanTaskList);
    }

    /**
     * 更新计划工作单的状态 由计划变更为已排程
     *
     * @param waitSavingWorkPlanTaskList 已排程的任务
     * @author niushuai
     * @date: 2021/6/11 16:40:23
     * @return: void
     */
    protected void updateStatus(List<WorkPlanTask> waitSavingWorkPlanTaskList) {


        // id in
        List<String> processTaskIdList = waitSavingWorkPlanTaskList.stream().map(WorkPlanTask::getProcessTaskId).collect(Collectors.toList());

        QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct order_id");
        queryWrapper.in("id", processTaskIdList);

        List<WorkProcessTask> workOrderList = workProcessTaskService.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtil.isEmpty(workOrderList)) {
            throw new ILSBootException("P-AU-0083");
        }

        Set<String> workOrderIdList = workOrderList.stream().map(WorkProcessTask::getOrderId).collect(Collectors.toSet());

        UpdateWrapper<WorkOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", WorkOrderStatusEnum.SCHEDULED.getValue());

        // status = 1 com.ils.modules.mes.enums.WorkOrderStatusEnum.PLAN
        updateWrapper.eq("status", WorkOrderStatusEnum.PLAN.getValue());
        updateWrapper.in("id", workOrderIdList);

        workOrderService.update(updateWrapper);
    }

    /**
     * 更新已排数量 mes_work_process_task
     *
     * @param waitSavingWorkPlanTaskList 已排程的任务
     * @author niushuai
     * @date: 2021/6/11 16:41:20
     * @return: void
     */
    protected void updateNum(List<WorkPlanTask> waitSavingWorkPlanTaskList) {
        for (WorkPlanTask waitPlanTask : waitSavingWorkPlanTaskList) {

            WorkProcessTask oldProcessTask = workProcessTaskService.getById(waitPlanTask.getProcessTaskId());
            // 更新已排程数
            WorkProcessTask newProcessTask = new WorkProcessTask();
            newProcessTask.setId(oldProcessTask.getId());
            newProcessTask.setScheduledQty(oldProcessTask.getScheduledQty().add(waitPlanTask.getPlanQty()));

            workProcessTaskService.updateById(newProcessTask);
        }
    }

    /**
     * 查询出该天班次最早开始时间
     *
     * @param stationId 工位id
     * @param workDate  工作日期
     * @author niushuai
     * @date: 2021/6/16 11:51:03
     * @return: {@link DateTime}
     */
    protected DateTime getMinStartDateTimeDualWorkDate(String stationId, Date workDate) {

        String minStartDateTimeDualWorkDate = workCalendarService.getMinStartDateTimeDualWorkDate(WorkCalendarTypeEnum.PRODUCTION.getValue(), stationId, DateUtil.formatDate(workDate));

        DateTime dateTime = DateTime.of(AutoTimeUtils.combineDateAndTime(workDate, minStartDateTimeDualWorkDate));
        log.info("stationId: {}, {}的最早开始时间为:{} | {}", stationId, DateUtil.formatDate(workDate), minStartDateTimeDualWorkDate, dateTime);
        return dateTime;
    }

    /**
     * 查找当前工位之前的那个工位
     *
     * @param tmpScheBaseTime 基准时间
     * @param currWorkCalendarVO  当前工位
     * @author niushuai
     * @date: 2021/6/16 11:54:41
     * @return: {@link WorkCalendarVO}
     */
    protected WorkCalendarVO findPreviousWorkCalendar(DateTime tmpScheBaseTime, WorkCalendarVO currWorkCalendarVO) {
        // 根据配置查询多少天前的数据 默认10天
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PREVIOUS_WORK_CALENDAR_DAYS);
        if (null == bizConfig) {
            bizConfig = new BizConfig();
            // 默认10天
            bizConfig.setConfigValue(BigDecimal.TEN.toString());
        }

        String maxDate = DateUtil.formatDate(currWorkCalendarVO.getWorkdate());
        String minDate = DateUtil.formatDate(DateUtil.offsetDay(currWorkCalendarVO.getWorkdate(), Math.negateExact(Integer.parseInt(bizConfig.getConfigValue()))));

        QueryWrapper<WorkCalendarVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a.is_deleted", ZeroOrOneEnum.ZERO.getIcode())
                .eq("a.type", WorkCalendarTypeEnum.PRODUCTION.getValue())
                .eq("a.station_id", currWorkCalendarVO.getStationId())
                .ge("a.workdate", minDate)
                .le("a.workdate", maxDate);

        queryWrapper.orderByAsc("a.workdate");
        queryWrapper.orderByAsc("b.shift_start_time");

        // 找到一堆工作日历
        List<WorkCalendarVO> workCalendarList = workCalendarService.queryWorkCalendarList(queryWrapper);

        if (CollectionUtil.isEmpty(workCalendarList)) {
            throw new ILSBootException("P-AU-0084",
                    DateUtil.formatDate(tmpScheBaseTime), bizConfig.getConfigValue(), currWorkCalendarVO.getStationName());
        }

        // 找到相匹配的工作日历
        int previousIndex = -1;
        for (int index = 0; index < workCalendarList.size(); index++) {
            WorkCalendarVO previousWorkCalendar = workCalendarList.get(index);
            previousWorkCalendar.setShiftFullDateTime();

            if (index == 0 && previousWorkCalendar.getId().equals(currWorkCalendarVO.getId())) {
                return previousWorkCalendar;
            }

            if (previousWorkCalendar.getId().equals(currWorkCalendarVO.getId())) {
                // 找到了位置的工序
                previousIndex = index - 1;
                break;
            }
        }

        // 下标未变 未找到相匹配的工作日历数据
        if (previousIndex == -1) {
            throw new ILSBootException("P-AU-0085", currWorkCalendarVO.getId());
        }

        // 前一工作日历
        WorkCalendarVO previousWorkCalendar = workCalendarList.get(previousIndex);
        previousWorkCalendar.setShiftFullDateTime();
        log.info("previousWorkCalendar: {} ~ {}", previousWorkCalendar.getShiftStartDateTime(), previousWorkCalendar.getShiftEndDateTime());

        return previousWorkCalendar;
    }

    protected DateTime reCalcStartDateTime(DateTime tmpScheBaseTime, WorkCalendarVO previousWorkCalendar) {
        log.info("reCalcStartDateTime: tmpScheBaseTime={}, {}, {}", tmpScheBaseTime, previousWorkCalendar.getShiftStartDateTime(), previousWorkCalendar.getShiftEndDateTime());
        DateTime startDateTime = null;
        if (tmpScheBaseTime.isBeforeOrEquals(previousWorkCalendar.getShiftStartDateTime())) {
            // 基准时间在班次开始时间之前
            log.info("基准时间在班次开始时间之前");
            startDateTime = previousWorkCalendar.getShiftStartDateTime();
        } else if (tmpScheBaseTime.isIn(previousWorkCalendar.getShiftStartDateTime(), previousWorkCalendar.getShiftEndDateTime())) {
            // 基准时间在班次开始时间与结束时间之间
            log.info("基准时间在班次开始时间与结束时间之间");
            startDateTime = tmpScheBaseTime;
        } else {
            // 基准时间在班次开始时间之后
            log.info("基准时间在班次开始时间之后");
            startDateTime = previousWorkCalendar.getShiftStartDateTime();
        }
        log.info("reCalcStartDateTime: 计算结果={}", startDateTime);

        return startDateTime;
    }


    /**
     * 根据传入的时间参数计算结束时间
     *
     * @param totalTime      生产任务总共需要时长
     * @param calTotalTime   班组已累计时长
     * @param workCalendarVO 工位的工作日历
     * @author niushuai
     * @date: 2021/6/7 16:07:46
     * @return: {@link Date}
     */
    protected Date calcEndDate(long totalTime, long calTotalTime, WorkCalendarVO workCalendarVO) {

        Date endDate;// 当前班次可以满足消耗 仍要确认任务剩余时间是否完全消耗完 即计算出需要占用当前班组的时间
        long taskLeftTime = totalTime - calTotalTime;
        // 然后endDate = 当前班次的workdate + 当前班次的shiftStartTime + taskLeftTime
        endDate = new Date(AutoTimeUtils.combineDateAndTime(workCalendarVO.getWorkdate(), workCalendarVO.getShiftStartTime()).getTime() + taskLeftTime);
        log.info("calcEndDate: {}", DateUtil.formatDateTime(endDate));
        return endDate;
    }

    /**
     * 获取班次持续时长
     *
     * @param shiftMap
     * @param workCalendarVO
     * @author niushuai
     * @date: 2021/7/19 15:22:23
     * @return: {@link Long}
     */
    protected Long getShiftDuration(Map<String, AutoScheShiftVO> shiftMap, WorkCalendarVO workCalendarVO) {
        AutoScheShiftVO shiftVO = shiftMap.get(workCalendarVO.getShiftId());
        Long shiftDuration = shiftVO.getShiftDuration();
        log.info("获取班次[{}]持续时长: {}", shiftVO.getShiftName(), printMillisecondsStr(shiftDuration));
        return shiftDuration;
    }

    protected long milliseconds2Minute(long milliseconds) {

        return new BigDecimal(milliseconds).divide(new BigDecimal("1000")).divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP).longValue();
    }

    protected String printMillisecondsStr(long milliseconds) {

        return milliseconds + ", " + milliseconds2Minute(milliseconds) + "m";
    }

    /**
     * 完成最后时间的赋值
     *
     * @param lstWorkPlanTask
     * @param workPlanTask    已完成的工序任务
     * @param startDate       计划开始时间
     * @param endDate         计划结束时间
     * @author niushuai
     * @date: 2021/6/3 17:46:57
     * @return: void
     */
    protected void finishWorkPlanTask(List<WorkPlanTask> lstWorkPlanTask, WorkPlanTask workPlanTask, Date startDate, Date endDate) {
        // 开始结束时间
        workPlanTask.setPlanStartTime(startDate);
        workPlanTask.setPlanEndTime(endDate);
        // 计划时间为开始时间即可 此字段只在按班次排程时使用
        workPlanTask.setPlanDate(startDate);
        // 时间单位默认小时
        workPlanTask.setTimeUnit(MesValidatUnitEnum.HOUR.getValue());
        // 填充时间差
        workPlanTask.setPlanTime(new BigDecimal(DateUtil.between(startDate, endDate, DateUnit.HOUR)));
        // 复制出新的对象保存到list中
        WorkPlanTask newWorkPlanTask = new WorkPlanTask();
        BeanUtil.copyProperties(workPlanTask, newWorkPlanTask, false);
        lstWorkPlanTask.add(newWorkPlanTask);

        log.info(System.identityHashCode(workPlanTask) + "|赋值: processName = {}, stationName = {}, startDate = {}, endDate = {}, lstWorkPlanTask.size() = {}",
                workPlanTask.getProcessName(), workPlanTask.getStationName(), DateTime.of(startDate), DateTime.of(endDate), lstWorkPlanTask.size());
    }

}
