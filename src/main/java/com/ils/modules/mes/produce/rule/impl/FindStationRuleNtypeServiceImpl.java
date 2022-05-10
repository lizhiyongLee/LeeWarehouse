package com.ils.modules.mes.produce.rule.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.SpringContextUtils;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.enums.PlanTaskExeStatusEnum;
import com.ils.modules.mes.enums.WorkflowTypeEnum;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.mapper.WorkOrderMapper;
import com.ils.modules.mes.produce.rule.FindStationRuleService;
import com.ils.modules.mes.produce.service.WorkOrderLineService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.produce.util.AutoScheContextEnv;
import com.ils.modules.mes.produce.vo.AutoScheProcessTaskTime;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkstationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * N型搜索排位
 *
 * @author niushuai
 * @date 2021/5/7 10:35:43
 */
@Service
public class FindStationRuleNtypeServiceImpl implements FindStationRuleService {
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderLineService workOrderLineService;
    /**
     * 初始化工位 赋值 workstationIds workstationMap processWorkstationMap
     *
     * @param autoScheContextEnv 所有可用工位
     * @author niushuai
     * @date: 2021/5/7 10:32:40
     * @return: {@link AutoScheWorkstationVO}
     */
    @Override
    public void initWorkstation(AutoScheContextEnv autoScheContextEnv) {

        // 所有待排程的工位ID
        Set<String> workstationIds = new HashSet<String>(32);

        // 工序任务对应的工位ID
        /*
            计划工作单           工序任务                    工序          工序关联工位              工位
            mes_work_order -> mes_work_process_task -> mes_process -> mes_process_station -> mes_workstation
            一个工序是可以绑定多个工位的
            mes_process <-> mes_workstation === mes_process_station
         */
        // Map<工序id, List<工序绑定的工位id>>
        Map<String, List<String>> processStationIds = new HashMap<>(4);

        List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO = autoScheContextEnv.getLstAutoScheWorkProcessVO();
        // 根据工序任务中关联的工序id 查询该工序关联的工位集合
        for (AutoScheWorkProcessVO autoScheWorkProcessVO : lstAutoScheWorkProcessVO) {
            List<String> stationIds = this.getProcessWorkstationIds(autoScheWorkProcessVO);

            Optional.ofNullable(stationIds).orElseThrow(() -> new ILSBootException("P-AU-1004", autoScheWorkProcessVO.getOrderNo(), autoScheWorkProcessVO.getFullProcess()));

            // 各个工序id对应的工位id集合
            processStationIds.put(autoScheWorkProcessVO.getId(), stationIds);
            //总集合 set 去重
            workstationIds.addAll(stationIds);
        }
        // 本次用户选择的工序所关联的所有工位id集合
        autoScheContextEnv.setWorkstationIds(workstationIds);

        // 获取所有工位 Map<工位ID, 工位对象>
        Map<String, AutoScheWorkstationVO> workstationMap = new HashMap<>(4);
        // 根据工位id 查询工位详细信息
        List<Workstation> lstWorkStation = SpringContextUtils.getBean(WorkstationService.class).queryWorkStationByIds(workstationIds);
        // 所有工位集合
        List<AutoScheWorkstationVO> lstAutoScheWorkstationVO = new ArrayList<AutoScheWorkstationVO>(lstWorkStation.size());
        for (Workstation workstation : lstWorkStation) {
            AutoScheWorkstationVO autoScheWorkstationVO = new AutoScheWorkstationVO();
            BeanUtils.copyProperties(workstation, autoScheWorkstationVO);
            // 该工位上的最晚空闲时间
            autoScheWorkstationVO.setMaxTime(-1L);
            lstAutoScheWorkstationVO.add(autoScheWorkstationVO);
            // 工位id为key 工位自身为value
            workstationMap.put(workstation.getId(), autoScheWorkstationVO);
        }
        autoScheContextEnv.setWorkstationMap(workstationMap);

        // Map<工序ID, List<可排程工位>>
        Map<String, List<AutoScheWorkstationVO>> processWorkstation = new HashMap<>(4);

        // 遍历所有工序 从所有工位集合中取出与当前工序相关的工位作为新的集合作为值 存储到工序与工位集合映射map - processWorkstation中
        for (Map.Entry<String, List<String>> processStationId : processStationIds.entrySet()) {
            // 工序id
            String processId = processStationId.getKey();
            // 工序id 对应的工位id集合
            List<String> processStationList = processStationId.getValue();
            // 从所有工位集合中取出与当前工序有关的工位
            List<AutoScheWorkstationVO> processWorkStationVO = lstAutoScheWorkstationVO.stream()
                    .filter(workstation -> processStationList.contains(workstation.getId()))
                    .collect(Collectors.toList());

            processWorkstation.put(processId, processWorkStationVO);
        }

        autoScheContextEnv.setProcessWorkstationMap(processWorkstation);
    }

    /**
     * 获取所有工位上在基准时间之后的任务 赋值 autoScheProcessTaskTimeMap
     *
     * @param autoScheContextEnv 所有可用工位
     * @author niushuai
     * @date: 2021/5/7 10:32:40
     * @return: {@link AutoScheWorkstationVO}
     */
    @Override
    public void initWorkPlanTask(AutoScheContextEnv autoScheContextEnv) {
        // 用户输入基准时间
        Date baseTime = autoScheContextEnv.getAutoScheParam().getBaseTime();

        // 获取当前所涉及到的工位在基准时间之后的所有任务
        List<WorkPlanTask> lstWorkPlanTask = this.queryWorkPlanTask(baseTime, autoScheContextEnv.getWorkstationIds());

        // 所有工位 Map<工位ID, 工位对象>
        Map<String, AutoScheWorkstationVO> autoScheWorkstationMap = autoScheContextEnv.getWorkstationMap();
        //
        Map<String, AutoScheProcessTaskTime> autoScheProcessTaskTimeMap = new HashMap(16);
        // 基准时间毫秒值 便于比较计算
        Long lBaseTime = baseTime.getTime();
        // 循环查询出的所有工序任务 - 已排程
        for (WorkPlanTask workPlan : lstWorkPlanTask) {
            // 当前任务的开始和结束时间
            Long planStartTime = workPlan.getPlanStartTime().getTime();
            Long planEndTime = workPlan.getPlanEndTime().getTime();
            // 从map中根据工位id获取工位对象
            AutoScheWorkstationVO autoScheWorkstationVO = autoScheWorkstationMap.get(workPlan.getStationId());
            // 查找出工位最晚的结束时间和生产的产品id
            if (planEndTime > autoScheWorkstationVO.getMaxTime()) {
                autoScheWorkstationVO.setMaxTime(planEndTime);
                autoScheWorkstationVO.setItemId(workPlan.getItemId());
            }

            // 查找每一个工位的最早开始时间和最晚结束时间
            AutoScheProcessTaskTime autoScheProcessTaskTime = autoScheProcessTaskTimeMap.get(workPlan.getProcessTaskId());
            if (autoScheProcessTaskTime == null) {
                autoScheProcessTaskTime = new AutoScheProcessTaskTime();
                // 如果缓存的不存在 将当前任务的结束时间作为最晚结束时间
                autoScheProcessTaskTime.setMaxTime(planEndTime);
                // 开始时间和基准时间做比较 开始时间比基准时间大就使用开始时间作为最小时间
                // 否则使用基准时间作为最小时间
                if (planStartTime > lBaseTime) {
                    autoScheProcessTaskTime.setMinTime(planStartTime);
                } else {
                    autoScheProcessTaskTime.setMinTime(lBaseTime);
                }
                autoScheProcessTaskTimeMap.put(workPlan.getProcessTaskId(), autoScheProcessTaskTime);
            } else {
                // 如果缓存数据已存在 将当前任务结束时间和最晚时间做比较 较大的一方作为新的最晚时间
                if (planEndTime > autoScheProcessTaskTime.getMaxTime()) {
                    autoScheProcessTaskTime.setMaxTime(planEndTime);
                }
                // 开始时间和最小时间做比较 开始时间比最小时间小的就使用开始时间最作为最小时间
                // 否则使用基准时间作为最小时间
                if (planStartTime < autoScheProcessTaskTime.getMinTime()) {
                    if (planStartTime > lBaseTime) {
                        autoScheProcessTaskTime.setMinTime(planStartTime);
                    } else {
                        autoScheProcessTaskTime.setMinTime(lBaseTime);
                    }
                }
            }
        }
        for (Map.Entry<String, AutoScheProcessTaskTime> entry : autoScheProcessTaskTimeMap.entrySet()) {
            entry.getValue().parseDate();
        }
        autoScheContextEnv.setAutoScheProcessTaskTimeMap(autoScheProcessTaskTimeMap);
    }


    /**
     * 获取每一道工序的可用的工位
     *
     * @param autoScheWorkProcessVO
     * @return
     */
    private List<String> getProcessWorkstationIds(AutoScheWorkProcessVO autoScheWorkProcessVO) {
        String workflowType = autoScheWorkProcessVO.getWorkflowType();
        WorkOrder workOrder = workOrderMapper.selectById(autoScheWorkProcessVO.getOrderId());
        QueryWrapper<WorkOrderLine> workOrderLineQueryWrapper = new QueryWrapper<>();
        workOrderLineQueryWrapper.eq("process_id", autoScheWorkProcessVO.getProcessId());
        workOrderLineQueryWrapper.eq("seq", autoScheWorkProcessVO.getSeq());
        workOrderLineQueryWrapper.eq("order_id", workOrder.getId());
        WorkOrderLine one = workOrderLineService.getOne(workOrderLineQueryWrapper);

        // 工位处理
        List<String> stationIds = SpringContextUtils.getBean(WorkProcessTaskService.class).getWorkstationIds(one.getId(), workflowType, autoScheWorkProcessVO.getProcessId(), autoScheWorkProcessVO.getSeq());
        return stationIds;
    }


    /**
     * 根据工位查询基准时间之后的任务
     *
     * @param baseTime
     * @param stationIds
     * @return
     */
    private List<WorkPlanTask> queryWorkPlanTask(Date baseTime, Collection<String> stationIds) {
        QueryWrapper<WorkPlanTask> queryWrapper = new QueryWrapper();
        // 查询取消状态之外的数据
        queryWrapper.ne("exe_status", PlanTaskExeStatusEnum.CANCEL.getValue());
        queryWrapper.ge("plan_end_time", baseTime);
        queryWrapper.in("station_id", stationIds);
        List<WorkPlanTask> lstWorkPlanTask = SpringContextUtils.getBean(WorkPlanTaskService.class).list(queryWrapper);
        return lstWorkPlanTask;
    }

}
