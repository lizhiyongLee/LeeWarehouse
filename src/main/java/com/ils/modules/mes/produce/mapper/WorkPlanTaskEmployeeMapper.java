package com.ils.modules.mes.produce.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkPlanTaskEmployee;

/**
 * @Description: 派工计划生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
public interface WorkPlanTaskEmployeeMapper extends ILSMapper<WorkPlanTaskEmployee> {

    /**
     * 通过主表 Id 删除
     * 
     * @param taskId
     * @return
     */
    public boolean deleteBytaskId(String taskId);
}
