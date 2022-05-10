package com.ils.modules.mes.qc.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcTaskRelateTotal;

/**
 * @Description: 质检任务关联物料整体
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskRelateTotalMapper extends ILSMapper<QcTaskRelateTotal> {
    /**
     * 根据质检任务id删除样本
     * @param taskId
     */
    public void delByQcTaskId(String taskId);
}
