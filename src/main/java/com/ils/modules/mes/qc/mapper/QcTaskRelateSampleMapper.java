package com.ils.modules.mes.qc.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcTaskRelateSample;
import com.ils.modules.mes.qc.vo.QcTaskRecodTraceVO;

import java.util.List;

/**
 * @Description: 质检任务关联物料样本
 * @Author: Tian
 * @Date: 2021-03-04
 * @Version: V1.0
 */
public interface QcTaskRelateSampleMapper extends ILSMapper<QcTaskRelateSample> {
    /**
     * 根据质检任务id删除样本
     *
     * @param taskId
     */
    public void delByQcTaskId(String taskId);

    /**
     * 记录查询
     * @param code
     * @param codeValue
     * @return
     */
    public List<QcTaskRecodTraceVO> qcTaskItemTraceRecord(String code, String codeValue);
}
