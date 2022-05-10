package com.ils.modules.mes.produce.rule.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;
import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;
import com.ils.modules.mes.config.aspect.StopWatchUtil;
import com.ils.modules.mes.enums.ProducePlanTypeEnum;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.rule.AbstractTaskSplitRuleService;
import com.ils.modules.mes.produce.util.AutoScheContextEnv;
import com.ils.modules.mes.produce.util.AutoTimeUtils;
import com.ils.modules.mes.produce.vo.AutoScheShiftVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkstationVO;
import com.ils.modules.mes.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 按班组拆分 计算排程任务
 *
 * @author niushuai
 * @date 2021/5/7 9:51:54
 */
@Slf4j
@Service
public class TaskShiftSplitRuleServiceImpl extends AbstractTaskSplitRuleService {

    /**
     * 拆分规则接口 按班组拆分
     *
     * @param autoScheContextEnv
     */
    @Override
    public void splitTask(AutoScheContextEnv autoScheContextEnv) {
        // 按班组拆分任务
        // throw new ILSBootException("P-AU-0089");

        // 有序的工序列表
        List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO = autoScheContextEnv.getLstAutoScheWorkProcessVO();
        // 自动排程结果
        List<WorkPlanTask> lstWorkPlanTask = new ArrayList(lstAutoScheWorkProcessVO.size());

        StopWatchUtil.start("按班组拆分任务");
        int index = 0;
        for (AutoScheWorkProcessVO autoScheWorkProcessVO : lstAutoScheWorkProcessVO) {
            index++;
            StopWatchUtil.start(index + "_" + autoScheWorkProcessVO.getOrderNo());
            StopWatchUtil.start(index + "_" + autoScheWorkProcessVO.getOrderNo() + "_第一阶段");
            // log.info("new task begin {}|{}", autoScheWorkProcessVO.getProcessName(), autoScheWorkProcessVO.getItemName());
            // 获取可用的，最早结束的工位
            AutoScheWorkstationVO autoScheWorkstationVO = super.getMinTimeWorkstation(autoScheWorkProcessVO, autoScheContextEnv.getProcessWorkstationMap().get(autoScheWorkProcessVO.getId()));
            // log.info("最早结束工位: {} | {}, 时间: {}", autoScheWorkstationVO.getId(), autoScheWorkstationVO.getStationName(), autoScheWorkstationVO.getMaxTimeStr());

            // 生成工序对应的任务
            WorkPlanTask workPlanTask = super.generatePlanTask(autoScheWorkstationVO, autoScheWorkProcessVO);
            workPlanTask.setPlanType(ProducePlanTypeEnum.SHIFT.getValue());

            // 获取工序对应设置准备时长
            WorkOrderLine workOrderLine = super.getWorkOrderLine(autoScheWorkProcessVO);
            Long processPrepareTime = AutoTimeUtils.convertTime(workOrderLine.getSetupTime(), workOrderLine.getSetupTimeUnit());

            // 动态准备时间 - 换型时间
            long prepareTime = super.getPrepareTime(autoScheWorkstationVO, autoScheWorkProcessVO.getItemId());
            // 总计需要准备的时长
            long totalPrepareTime = processPrepareTime + prepareTime;

            // log.info("工序准备时长: {}, 动态准备时长: {}, 总计准备时长: {}", processPrepareTime, prepareTime, printMillisecondsStr(totalPrepareTime));

            // 获取标准产能
            ScheduleStandardProductionVolume stdProVolume = super.getStandardProductionVolume(autoScheWorkProcessVO, autoScheWorkstationVO);
            // log.info("标准产能: {}, {}, {}", stdProVolume.getTimePeriod(), stdProVolume.getTimeUnit(), stdProVolume.getQty());
            // 计算完成本次任务的时长
            // Long taskDuration = super.calTimeDurationByStdVolume(stdProVolume, autoScheWorkProcessVO.getSchePlanQty());


            // 所需时长
            // long totalTime = prepareTime + taskDuration + processPrepareTime;

            // 获取根据条件修正后的基准时间
            Date scheBaseTime = super.getScheBaseTime(autoScheContextEnv, lstWorkPlanTask, autoScheWorkProcessVO, autoScheWorkstationVO);

            // 获取班次数据  所有班次数据
            Map<String, AutoScheShiftVO> shiftMap = this.getShift();

            //统计累计时长
            // long calTotalTime = 0L;
            //排程开始日期, 结束日期
            Date startDate = null, endDate;

            //根据工位id查询在给定的基准时间之后的工作日历
            List<WorkCalendarVO> lstWorkCalendarVO = super.getWorkCalendarListNew(scheBaseTime, autoScheWorkstationVO.getId());
            // log.info("获取工位日历： {}, {} | {}", DateTime.of(scheBaseTime), autoScheWorkstationVO.getStationName(), lstWorkCalendarVO.size());

            StopWatchUtil.stop(index + "_" + autoScheWorkProcessVO.getOrderNo() + "_第一阶段");
            StopWatchUtil.start(index + "_" + autoScheWorkProcessVO.getOrderNo() + "_第二阶段");


            if (CommonUtil.isEmptyOrNull(lstWorkCalendarVO)) {
                throw new ILSBootException("P-AU-0077", autoScheWorkstationVO.getStationName(), DateUtil.formatDateTime(scheBaseTime));
            } else {

                ListIterator<WorkCalendarVO> workCalendarIterator = lstWorkCalendarVO.listIterator();
                // 是否是第一次循环  第一次循环需要定开始时间
                boolean firstItem = true;
                // 是否是上个班次遗留时间   会导致早班未计算则直接跳到中班
                boolean canUseNextGetElement = true;
                boolean isSkipedElement = false;
                WorkCalendarVO workCalendarVO = null;
                while (workCalendarIterator.hasNext()) {
                    // 如果第一次计算为班次遗留时间 则不再获取下一个
                    int nextIndex = workCalendarIterator.nextIndex();
                    if (canUseNextGetElement || nextIndex > 1) {
                        workCalendarVO = workCalendarIterator.next();
                        // 还原跳过元素的状态
                        //isSkipedElement = false;
                    } else {
                        isSkipedElement = false;
                    }

                    log.info("\n\n\n");
                    log.info("=== workCalendarIterator.hasNext() =={}==={}==={}~{}===================",
                            workCalendarVO.getStationName(), workCalendarVO.getShiftName(),
                            workCalendarVO.getShiftStartDateTime(), workCalendarVO.getShiftEndDateTime());

                    AutoScheShiftVO shiftVO = shiftMap.get(workCalendarVO.getShiftId());
                    Long shiftDuration = shiftVO.getShiftDuration();

                    DateTime shiftStartDateTime = workCalendarVO.getShiftStartDateTime();
                    DateTime shiftEndDateTime = workCalendarVO.getShiftEndDateTime();

                    // log.info("{}当前工作日历时间: {}~{}", workCalendarVO.getShiftName(), shiftStartDateTime, shiftEndDateTime);
                    // log.info("{}班次持续时间: {}", workCalendarVO.getShiftName(), printMillisecondsStr(shiftDuration));

                    // 解决跨天的问题，防止格式化后 结束时间比开始时间早
                    if (shiftStartDateTime.getTime() > shiftEndDateTime.getTime()) {
                        shiftEndDateTime = DateUtil.offsetDay(shiftEndDateTime, 1);
                    }

                    DateTime tmpScheBaseTime = DateTime.of(scheBaseTime);

                    // 处理开始时间
                    if (firstItem) {
                        // log.info("处理开始时间, 基准时间: {}", tmpScheBaseTime);
                        //            2021-06-18 00:30:00  2021-06-18 08:00:00  2021-06-18 12:00:00
                        if (tmpScheBaseTime.isIn(shiftStartDateTime, shiftEndDateTime)) {
                            // 如果在时间之内
                            startDate = scheBaseTime;
                            // 当前班次剩余时间
                            shiftDuration = shiftEndDateTime.getTime() - scheBaseTime.getTime();
                            // log.info("在班次[{}]时间之内: {}, 班次剩余时间: {}", shiftVO.getShiftName(), startDate, printMillisecondsStr(shiftDuration));
                        } else if (tmpScheBaseTime.isBeforeOrEquals(shiftStartDateTime)) {
                            // 如果基准时间小于班次开始时间

                            /* 此处需要考虑跨天的情形
                                 2021-06-18 00:30:00  2021-06-18 08:00:00  2021-06-18 12:00:00
                               上述时间若按之前的逻辑得到的开始时间是 2021-06-18 08:00:00
                               而从2021-06-18 00:30:00到该夜班(2021-06-17)结束时间为空闲时间 未排程 存在问题

                               此处特殊处理下 查询出该天班次最早开始时间 todayMinStartDateTime
                               将 tmpScheBaseTime 与 workDateMinStartDateTime 做比较
                               若前者仍小于后者 则表示 tmpScheBaseTime 为前一个班别的时间 前一个班别为夜班且跨天
                               反之 开始时间 = 班次开始时间

                            */
                            DateTime workDateMinStartDateTime = super.getMinStartDateTimeDualWorkDate(workCalendarVO.getStationId(), workCalendarVO.getWorkdate());

                            if (tmpScheBaseTime.isBefore(workDateMinStartDateTime)) {
                                // log.info("比当前班次开始时间更早, 为上一班次遗留时间");
                                WorkCalendarVO previousWorkCalendar = super.findPreviousWorkCalendar(tmpScheBaseTime, workCalendarVO);
                                startDate = super.reCalcStartDateTime(tmpScheBaseTime, previousWorkCalendar);
                                shiftDuration = previousWorkCalendar.getShiftEndDateTime().getTime() - startDate.getTime();
                                // 将会跳过当前元素
                                isSkipedElement = true;
                            } else {
                                // 开始时间 = 班次开始时间
                                startDate = workCalendarVO.getShiftStartDateTime();
                            }
                            // log.info("在班次[{}]时间之前: {}, 班次剩余时间: {}", shiftVO.getShiftName(), startDate, printMillisecondsStr(shiftDuration));
                        } else {
                            // 基准时间大于班次结束时间
                            // 如果说长度只有1 即没有下一个工位了，直接抛出异常给用户，提醒该工位没有安排工作日历
                            if (!workCalendarIterator.hasNext()) {
                                throw new ILSBootException("P-AU-0078", autoScheWorkstationVO.getStationName(), DateUtil.formatDateTime(scheBaseTime));
                            }

                            workCalendarVO = workCalendarIterator.next();
                            // 开始时间
                            startDate = workCalendarVO.getShiftStartDateTime();
                            // 班次持续时长
                            shiftDuration = getShiftDuration(shiftMap, workCalendarVO);

                            // log.info("在班次[{}]时间之后: {}, 班次剩余时间: {}", shiftVO.getShiftName(), startDate, printMillisecondsStr(shiftDuration));
                        }
                    } else {
                        startDate = shiftStartDateTime;
                    }
                    // log.info("开始时间暂定: {}", startDate);

                    //////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /*
                        代码走到此处的时候 startDate 已经暂定
                        要确定准备时长是否超过本班次剩余时长
                        如果 processPrepareTime + prepareTime < shiftDuring 表示当前班次可以将准备时长消耗完毕
                        否则 当前当前班次无法将准备时长消耗完毕 需要下个班次接着消耗剩余的准备时长
                            此时 startDate 需要重新计算
                                startDate = 消耗完毕时所处班次的开始时间

                     */
                    Date beforeStartDate = new Date(startDate.getTime());

                    if (firstItem) {
                        // 第一个班组才会去消耗准备时长, 其余任务不再需要消耗准备时长
                        startDate = this.consumePrepareTime(autoScheWorkProcessVO, startDate, workCalendarVO, workCalendarIterator, totalPrepareTime, shiftDuration, shiftMap);
                    }
                    firstItem = false;
                    // log.info("得到最终确认的开始时间: {}", startDate);
                    if (!beforeStartDate.equals(startDate)) {
                        // 开始时间已变 shiftDuration 更改为新班次的持续时间
                        shiftDuration = getShiftDuration(shiftMap, workCalendarVO);
                        // log.info("开始时间已发生改变, before: {}, now: {}, 班次持续时长: {}", DateTime.of(beforeStartDate), startDate, printMillisecondsStr(shiftDuration));
                    }

                    // 计算结束时间时要考虑当前时间内能生产多少个产品 并且存在小数时采取向上取整措施
                    // 当前班次剩余时间可生产数量
                    BigDecimal couldPlanTaskNum = this.getCouldPlanTaskNum(stdProVolume, shiftDuration);
                    // 能生产的数量 大于 总数量  本班次能完成 == 结束时间要根据实际生产需要时间 + 开始时间计算
                    if (couldPlanTaskNum.longValue() > autoScheWorkProcessVO.getSchePlanQty().longValue()) {
                        // log.info("能生产的数量: {} 大于 总数量: {}  本班次能完成", couldPlanTaskNum, autoScheWorkProcessVO.getSchePlanQty().longValue());
                        Long currTaskDuration = super.calTimeDurationByStdVolume(stdProVolume, autoScheWorkProcessVO.getSchePlanQty());
                        // 当前任务的尚需生产数量
//                        BigDecimal shouldPlanTaskNum = this.getCouldPlanTaskNum(stdProVolume, currTaskDuration);
                        BigDecimal shouldPlanTaskNum = autoScheWorkProcessVO.getSchePlanQty();
                        // 结束时间
                        endDate = new DateTime(startDate.getTime() + currTaskDuration);
                        // log.info("完成需要时长: {}, 当前任务的尚需生产数量: {}, 结束时间: {}", printMillisecondsStr(currTaskDuration), shouldPlanTaskNum.longValue(), endDate);

                        workPlanTask.setPlanQty(shouldPlanTaskNum);

                        // 班次id
                        workPlanTask.setShiftId(workCalendarVO.getShiftId());

                        super.finishWorkPlanTask(lstWorkPlanTask, workPlanTask, startDate, endDate);
                        autoScheWorkstationVO.setMaxTime(endDate.getTime());
                        break;
                    } else {
                        // log.info("能生产的数量: {} 小于 总数量: {} 本班次不能完成", couldPlanTaskNum, autoScheWorkProcessVO.getSchePlanQty().longValue());
                        // 生产的数量不足以满足总数量的需求 本班次的结束时间即为当前任务的结束时间
                        endDate = workCalendarVO.getShiftEndDateTime();
                        // log.info("本班次结束 开始时间: {}, 结束时间: {}, 生产数量: {}", startDate, endDate, couldPlanTaskNum.longValue());
                        // 剩余未生产数量
                        BigDecimal leftSchePlanQty = autoScheWorkProcessVO.getSchePlanQty().subtract(couldPlanTaskNum);
                        // log.info("剩余未生产数量: {}", leftSchePlanQty.longValue());
                        // 生产数量从总数量中减去
                        autoScheWorkProcessVO.setSchePlanQty(leftSchePlanQty);
                        // 任务结束 要开始下个任务
                        workPlanTask.setPlanQty(couldPlanTaskNum);
                        endDate = new Date(startDate.getTime() + shiftDuration);

                        // 班次id
                        workPlanTask.setShiftId(workCalendarVO.getShiftId());

                        super.finishWorkPlanTask(lstWorkPlanTask, workPlanTask, startDate, endDate);
                        autoScheWorkstationVO.setMaxTime(endDate.getTime());
                        if (isSkipedElement) {
                            canUseNextGetElement = false;
                        } else {
                            canUseNextGetElement = true;
                        }
                        // 如果标志位true才关心这个  否则
                        // 第一次传递 false 表示需要终止
                        if (StrUtil.isEmpty(autoScheContextEnv.getAutoScheParam().getOverWorkCalendarFlag()) || StrUtil.equalsIgnoreCase(Boolean.FALSE.toString(), autoScheContextEnv.getAutoScheParam().getOverWorkCalendarFlag())) {
                            // 如果没有下一个 抛出异常提示用户
                            if (!workCalendarIterator.hasNext()) {
                                throw new ILSBootException("P-AU-10081", autoScheWorkProcessVO.getProcessName(), workCalendarVO.getStationName(), DateUtil.formatDateTime(startDate));
                            }
                        }
                        continue;
                    }
                }
            }

            // 如果计算完成后 未找到开始或者结束时间 提示用户
            if (null == workPlanTask.getPlanStartTime() || null == workPlanTask.getPlanEndTime()) {
                throw new ILSBootException("P-AU-0079", autoScheWorkstationVO.getStationName());
            }
            StopWatchUtil.stop(index + "_" + autoScheWorkProcessVO.getOrderNo() + "_第二阶段");
            StopWatchUtil.stop(index + "_" + autoScheWorkProcessVO.getOrderNo());
        }

        StopWatchUtil.stop("按班组拆分任务");

        log.info("结束, 设置值, 总任务数: {}", lstWorkPlanTask.size());
        autoScheContextEnv.setWaitSavingWorkPlanTaskList(lstWorkPlanTask);
    }


    /**
     * 消耗准备时长
     *
     * @param startDate            计算过的一次开始时间
     * @param workCalendarIterator 工作日历迭代器
     * @param totalPrepareTime     工序中的准备时长 + 换型时间
     * @param shiftDuration        班次时长
     * @param shiftMap             班次信息
     * @author niushuai
     * @date: 2021/7/16 15:24:19
     * @return: {@link Date}
     */
    protected Date consumePrepareTime(AutoScheWorkProcessVO autoScheWorkProcessVO, Date startDate,
                                      WorkCalendarVO lastWorkCalendarVO, Iterator<WorkCalendarVO> workCalendarIterator,
                                      long totalPrepareTime, long shiftDuration, Map<String, AutoScheShiftVO> shiftMap) {

        // log.info("开始使用当前班组[{}-{}]时间消耗准备时间, {}, {}",
        //         autoScheWorkProcessVO.getProcessName(), lastWorkCalendarVO.getShiftName(),
        //         printMillisecondsStr(totalPrepareTime), printMillisecondsStr(shiftDuration));

        if (totalPrepareTime < shiftDuration) {
            // 本班次内能消耗完毕准备时长 返回原先计算好的开始时间
            // log.info("消耗完毕, startDate = {}", startDate);
            return startDate;
        }

        // 本班次消耗不完, 计算出剩余未消耗的时间 继续使用迭代器往下走
        long shiftLeftTime = totalPrepareTime - shiftDuration;

        // log.info("未消耗完毕, 继续消耗, 剩余未消耗时间: {}", printMillisecondsStr(shiftLeftTime));
        // 没有下一个班次了
        if (!workCalendarIterator.hasNext()) {
            throw new ILSBootException("P-AU-0081", autoScheWorkProcessVO.getProcessName(),
                    lastWorkCalendarVO.getStationName(), DateUtil.formatDateTime(startDate));
        }

        WorkCalendarVO workCalendarVO = workCalendarIterator.next();
        BeanUtil.copyProperties(workCalendarVO, lastWorkCalendarVO, false);

        // 找到当前工位对应的班次信息
        AutoScheShiftVO shiftVO = shiftMap.get(workCalendarVO.getShiftId());
        shiftDuration = shiftVO.getShiftDuration();


        // 开始时间重置为当前工位对应班次的开始时间
        startDate = workCalendarVO.getShiftStartDateTime();

        // log.info("获取下一个工作日历:{}|{}~{}, 获取下一个班次信息: {}|{}, 重置开始时间: {}",
        //         workCalendarVO.getShiftName(), workCalendarVO.getShiftStartDateTime(), workCalendarVO.getShiftEndDateTime(),
        //         shiftVO.getShiftName(), printMillisecondsStr(shiftDuration),
        //         startDate);

        return consumePrepareTime(autoScheWorkProcessVO, startDate,
                lastWorkCalendarVO, workCalendarIterator,
                shiftLeftTime, shiftDuration, shiftMap);
    }

    /**
     * 根据班组剩余时间来计算生产数量
     *
     * @param stdProVolume
     * @param shiftDuration
     * @author niushuai
     * @date: 2021/7/19 11:20:26
     * @return: {@link BigDecimal}
     */
    private BigDecimal getCouldPlanTaskNum(ScheduleStandardProductionVolume stdProVolume, Long shiftDuration) {
        // 标准产能 毫秒值   表示 t1毫秒内能生产 qty个 生产一个需要 t1 / qty
        Long t1 = BigDecimal.valueOf(AutoTimeUtils.convertTime(stdProVolume.getTimePeriod(), stdProVolume.getTimeUnit())).divide(stdProVolume.getQty(), 0, RoundingMode.UP).longValue();

        // 总生产时间 = 标准产能 * 生产数量
        // 可生产数量 = 总生产时间 / 标准产能

        BigDecimal tmpPlanTaskNum = new BigDecimal(shiftDuration + "").divide(new BigDecimal(t1 + ""), 0, RoundingMode.UP);

        // log.info("标准产能: {} | 计算班组剩余时间：{}, 可生产数量：{}", printMillisecondsStr(t1), printMillisecondsStr(shiftDuration), tmpPlanTaskNum);
        return tmpPlanTaskNum;
    }

}
