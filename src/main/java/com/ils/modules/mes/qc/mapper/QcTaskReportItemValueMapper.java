package com.ils.modules.mes.qc.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcTaskReportItemValue;

import java.util.List;

/**
 * @Description: 质检报告记录值
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskReportItemValueMapper extends ILSMapper<QcTaskReportItemValue> {
    /**
     * 通过主表id删除质检记录值
     * @param taskId
     */
    public void delByQcTaskId(String taskId);

    /**
     * 通过任务id和序号删除质检报告记录值
     * @param taskId
     * @param seqList
     */
    public void delBySeqAndTaskId(String taskId, List<Integer> seqList);
}
