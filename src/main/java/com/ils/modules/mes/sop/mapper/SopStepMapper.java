package com.ils.modules.mes.sop.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.sop.entity.SopStep;

/**
 * @Description: 标准作业任务步骤
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
public interface SopStepMapper extends ILSMapper<SopStep> {
    /**
     * 根据执行任务删除步骤
     * @param relatedTaskId
     * @return
     */
     boolean deleteByRelatedTaskId(String relatedTaskId);
}
