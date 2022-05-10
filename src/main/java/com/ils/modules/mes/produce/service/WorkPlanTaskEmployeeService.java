package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkPlanTaskEmployee;

/**
 * @Description: 派工计划生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
public interface WorkPlanTaskEmployeeService extends IService<WorkPlanTaskEmployee> {

    /**
     * 添加
     * @param workPlanTaskEmployee
     */
    public void saveWorkPlanTaskEmployee(WorkPlanTaskEmployee workPlanTaskEmployee) ;
    
    /**
     * 修改
     * @param workPlanTaskEmployee
     */
    public void updateWorkPlanTaskEmployee(WorkPlanTaskEmployee workPlanTaskEmployee);
    
    /**
     * 删除
     * @param id
     */
    public void delWorkPlanTaskEmployee (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWorkPlanTaskEmployee (List<String> idList);
}
