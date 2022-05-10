package com.ils.modules.mes.sop.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.sop.entity.SopControl;

/**
 * @Description: 标准作业任务步骤控件
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
public interface SopControlMapper extends ILSMapper<SopControl> {
    /**
     * 根据执行任务删除步骤
     * @param relatedTaskId
     * @return
     */
    boolean deleteByRelatedTaskId(String relatedTaskId);
}
