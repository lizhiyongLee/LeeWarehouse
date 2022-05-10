package com.ils.modules.mes.produce.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.vo.AutoScheParam;
import com.ils.modules.mes.produce.vo.AutoScheProcessTaskTime;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkstationVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 自动排程环境参数
 * @author: fengyi
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheContextEnv {
    /** 自动排程页面参数 */
    private AutoScheParam autoScheParam;
    /** 有序的工序任务 */
    private List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO;
    /** 所有待排程的工位ID */
    private Set<String> workstationIds;
    /** 工位ID 对应的工位VO */
    private Map<String, AutoScheWorkstationVO> workstationMap;
    /** 工序对应的工位VO */
    private Map<String, List<AutoScheWorkstationVO>> processWorkstationMap;
    /** 工序任务时间 */
    private Map<String, AutoScheProcessTaskTime> autoScheProcessTaskTimeMap;

    /**
     * 等待保存入库的工序任务
     */
    private List<WorkPlanTask> waitSavingWorkPlanTaskList;

    // work_plan_task  work_process_task

}
