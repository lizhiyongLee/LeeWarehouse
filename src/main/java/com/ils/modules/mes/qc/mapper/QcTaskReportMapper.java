package com.ils.modules.mes.qc.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcTaskReport;

/**
 * @Description: 质检任务报告
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskReportMapper extends ILSMapper<QcTaskReport> {
    /**
     * 通过主表id删除
     * @param taskId
     */
    public void delByQcTaskId(String taskId);


}
