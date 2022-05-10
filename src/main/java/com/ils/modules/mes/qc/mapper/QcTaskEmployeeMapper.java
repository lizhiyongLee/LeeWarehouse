package com.ils.modules.mes.qc.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcTaskEmployee;

/**
 * @Description: 质检任务关联人员
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskEmployeeMapper extends ILSMapper<QcTaskEmployee> {
    /**
     * 通过任务id删除任务计划执行人
     * @param excuteTaskId
     */
    public void delByExcuteTaskId(String excuteTaskId);

}
