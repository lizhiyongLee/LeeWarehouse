package com.ils.modules.mes.produce.rule.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;
import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;
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

import java.util.*;

/**
 * 按数量拆分 计算排程任务
 *
 * @author niushuai
 * @date 2021/5/7 9:51:54
 */
@Slf4j
@Service
public class TaskNumSplitRuleServiceImpl extends AbstractTaskSplitRuleService {

    /**
     * 拆分规则接口 按数量拆分
     *
     * @param autoScheContextEnv 页面参数
     */
    @Override
    public void splitTask(AutoScheContextEnv autoScheContextEnv) {
        // 有序的工序列表
        List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO = autoScheContextEnv.getLstAutoScheWorkProcessVO();

        // 重新填充序列
        this.rebuildLstAutoScheWorkProcessVO(lstAutoScheWorkProcessVO);

        // 自动排程结果
        List<WorkPlanTask> lstWorkPlanTask = new ArrayList(lstAutoScheWorkProcessVO.size());

        for (AutoScheWorkProcessVO autoScheWorkProcessVO : lstAutoScheWorkProcessVO) {
            log.info("===============================================================================================");

            // 获取可用的，最早结束的工位
            AutoScheWorkstationVO autoScheWorkstationVO = super.getMinTimeWorkstation(autoScheWorkProcessVO, autoScheContextEnv.getProcessWorkstationMap().get(autoScheWorkProcessVO.getId()));
            log.info("{} 最早结束工位: {}|{}", autoScheWorkProcessVO.getProcessName(), autoScheWorkstationVO.getStationName(), autoScheWorkstationVO.getMaxTimeStr());


            // 生成工序对应的任务
            WorkPlanTask workPlanTask = super.generatePlanTask(autoScheWorkstationVO, autoScheWorkProcessVO);
            workPlanTask.setPlanType(ProducePlanTypeEnum.TIME.getValue());

            // 获取工序对应设置准备时长
            WorkOrderLine workOrderLine = super.getWorkOrderLine(autoScheWorkProcessVO);
            Long processPrepareTime = AutoTimeUtils.convertTime(workOrderLine.getSetupTime(), workOrderLine.getSetupTimeUnit());

            // 获取标准产能
            ScheduleStandardProductionVolume stdProVolume = super.getStandardProductionVolume(autoScheWorkProcessVO, autoScheWorkstationVO);
            // 计算完成本次任务的时长
            Long taskDuration = super.calTimeDurationByStdVolume(stdProVolume, autoScheWorkProcessVO.getSchePlanQty());

            // 动态准备时间
            long prepareTime = super.getPrepareTime(autoScheWorkstationVO, autoScheWorkProcessVO.getItemId());

            // 所需时长
            long totalTime = prepareTime + taskDuration + processPrepareTime;

            // 获取根据条件修正后的基准时间
            Date scheBaseTime = super.getScheBaseTime(autoScheContextEnv, lstWorkPlanTask, autoScheWorkProcessVO, autoScheWorkstationVO);

            // 获取班次数据  所有班次数据
            Map<String, AutoScheShiftVO> shiftMap = this.getShift();

            //统计累计时长
            long calTotalTime = 0L;
            //排程开始日期, 结束日期
            Date startDate = null, endDate;


            //逐天获取工作日历
            //根据工位id查询在给定的基准时间之后的工作日历
            List<WorkCalendarVO> lstWorkCalendarVO = super.getWorkCalendarListNew(scheBaseTime, autoScheWorkstationVO.getId());

            // 基准日期当天
            if (CommonUtil.isEmptyOrNull(lstWorkCalendarVO)) {
                throw new ILSBootException("P-AU-0077", autoScheWorkstationVO.getStationName(), DateUtil.formatDateTime(scheBaseTime));
            } else {
                /*  基准时间当天

                    安排了工作日历 目的仍然是求出开始时间和结束时间
                    开始时间需要判断基准时间是否在班次时间内，
                        基准时间在班次开始时间和结束时间之内  基准时间即为开始时间
                        基准时间比班次开始时间小            班次开始时间即为基准时间
                        基准时间比班次结束时间大            下一个班次的开始时间即为基准时间
                                                            若无下一个班次 要继续查找下一天的工作日历
                    获取当前工作日历绑定的班次
                    设工序所需总时长 totalTime
                    班次累计任务时长 calTotalTime
                    找到班次之后，可以获取当前班次的持续时长 shiftDuration
                    if (totalTime(工序所需总时长) - calTotalTime(班次累计任务时长) > shiftDuration(当前班次的持续时长)) {
                        任务在当前班次无法完成，需要顺延到下一班次 可能跨天(同时 班次任务累计时长要接着累加)
                    } else {
                        任务剩余需要时长 = totalTime - calTotalTime - shiftDuration
                        任务在当前班次可以完成 计算结束时间 = 当前班次的开始时间 + 任务剩余需要时长 排程完成
                    }
                 */
                Iterator<WorkCalendarVO> workCalendarIterator = lstWorkCalendarVO.iterator();
                boolean firstItem = true;
                while (workCalendarIterator.hasNext()) {
                    WorkCalendarVO workCalendarVO = workCalendarIterator.next();

                    AutoScheShiftVO shiftVO = shiftMap.get(workCalendarVO.getShiftId());
                    Long shiftDuration = shiftVO.getShiftDuration();

                    DateTime shiftStartDateTime = workCalendarVO.getShiftStartDateTime();
                    DateTime shiftEndDateTime = workCalendarVO.getShiftEndDateTime();
                    // 解决跨天的问题，防止格式化后 结束时间比开始时间早
                    if (shiftStartDateTime.getTime() > shiftEndDateTime.getTime()) {
                        shiftEndDateTime = DateUtil.offsetDay(shiftEndDateTime, 1);
                    }

                    DateTime tmpScheBaseTime = DateTime.of(scheBaseTime);

                    // 处理开始时间
                    if (firstItem) {
                        firstItem = false;
                        //            2021-06-18 00:30:00  2021-06-18 08:00:00  2021-06-18 12:00:00
                        if (tmpScheBaseTime.isIn(shiftStartDateTime, shiftEndDateTime)) {
                            // 如果在时间之内
                            startDate = scheBaseTime;
                            // 当前班次剩余时间
                            shiftDuration = shiftEndDateTime.getTime() - scheBaseTime.getTime();
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
                            // 查询基准时间最早的班组开始时间
                            DateTime workDateMinStartDateTime = super.getMinStartDateTimeDualWorkDate(workCalendarVO.getStationId(), workCalendarVO.getWorkdate());
                            // 还是比最早的班次时间早
                            if (tmpScheBaseTime.isBefore(workDateMinStartDateTime)) {
                                // 结束时间比当前天的最早时间还早, 证明是上一个班次的遗留时间
                                WorkCalendarVO previousWorkCalendar = super.findPreviousWorkCalendar(tmpScheBaseTime, workCalendarVO);
                                startDate = this.reCalcStartDateTime(tmpScheBaseTime, previousWorkCalendar);
                                shiftDuration = previousWorkCalendar.getShiftEndDateTime().getTime() - startDate.getTime();
                            } else {
                                // 开始时间 = 班次开始时间
                                startDate = workCalendarVO.getShiftStartDateTime();
                            }
                        } else {
                            // 基准时间大于班次结束时间
                            // 如果说长度只有1 即没有下一个工位了，直接抛出异常给用户，提醒该工位没有安排工作日历
                            if (!workCalendarIterator.hasNext()) {
                                throw new ILSBootException("P-AU-0078", autoScheWorkstationVO.getStationName(), DateUtil.formatDateTime(scheBaseTime));
                            }

                            workCalendarVO = workCalendarIterator.next();
                            startDate = workCalendarVO.getShiftStartDateTime();
                            // 重新获取班次持续时长
                            shiftDuration = getShiftDuration(shiftMap, workCalendarVO);
                        }
                    }
                    // 代码走到这里的时候 开始时间已确定，只需求出结束时间即可
                    if (totalTime - calTotalTime > shiftDuration) {
                        // 无法满足时需要累加班次持续时长
                        calTotalTime = calTotalTime + shiftDuration;
                        if (!workCalendarIterator.hasNext()) {
                            throw new ILSBootException("P-AU-0081", autoScheWorkProcessVO.getProcessName(),
                                    workPlanTask.getStationName(), DateUtil.formatDateTime(startDate));
                        }
                    } else {
                        //同一时间段内多个任务时，当前日历中，下一个任务的开始时间是上一个任务的结束时间
                        if (calTotalTime == 0) {
                            workCalendarVO.setShiftStartTime(DateUtil.formatTime(startDate));
                        }
                        endDate = this.calcEndDate(totalTime, calTotalTime, workCalendarVO);
                        // 排程结束
                        this.finishWorkPlanTask(lstWorkPlanTask, workPlanTask, startDate, endDate);
                        // 更新当前工位的结束时间和开始时间
                        // 是否要设置? autoScheContextEnv.getAutoScheProcessTaskTimeMap().get(0).setMinTime(startDate.getTime());
                        autoScheWorkstationVO.setMaxTime(endDate.getTime());
                        break;
                    }
                }
            }
            // 如果计算完成后 未找到开始或者结束时间 提示用户
            if (null == workPlanTask.getPlanStartTime() || null == workPlanTask.getPlanEndTime()) {
                throw new ILSBootException("P-AU-0079", autoScheWorkstationVO.getStationName());
            }
        }

        autoScheContextEnv.setWaitSavingWorkPlanTaskList(lstWorkPlanTask);
    }

    /**
     * 根据数量填重新填充队列
     *
     * @param lstAutoScheWorkProcessVO 带有taskSum的序列
     * @author niushuai
     * @date: 2021/6/21 17:16:54
     * @return: void
     */
    protected void rebuildLstAutoScheWorkProcessVO(List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO) {

        ListIterator<AutoScheWorkProcessVO> listIterator = lstAutoScheWorkProcessVO.listIterator();
        List<AutoScheWorkProcessVO> resultList = new ArrayList<>(16);
        while (listIterator.hasNext()) {
            AutoScheWorkProcessVO next = listIterator.next();
            for (int i = 0; i < next.getTaskSum(); i++) {
                resultList.add(BeanUtil.copyProperties(next, AutoScheWorkProcessVO.class));
            }
        }
        lstAutoScheWorkProcessVO.clear();
        lstAutoScheWorkProcessVO.addAll(resultList);
    }

}
