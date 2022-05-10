package com.ils.modules.mes.execution.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.execution.entity.WorkProduceTaskEmployee;

/**
 * @Description: 执行生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
public interface WorkProduceTaskEmployeeMapper extends ILSMapper<WorkProduceTaskEmployee> {

    /**
     * 
     * 根据执行任务删除执行人员
     * 
     * @param excuteTaskId
     * @return
     * @date 2020年12月8日
     */
    public boolean deleteByExcuteTaskId(String excuteTaskId);
}
