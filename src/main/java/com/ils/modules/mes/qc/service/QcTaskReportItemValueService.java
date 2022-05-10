package com.ils.modules.mes.qc.service;

import java.util.List;
import com.ils.modules.mes.qc.entity.QcTaskReportItemValue;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 质检报告记录值
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskReportItemValueService extends IService<QcTaskReportItemValue> {

    /**
     * 添加
     * @param qcTaskReportItemValue
     */
    public void saveQcTaskReportItemValue(QcTaskReportItemValue qcTaskReportItemValue) ;
    
    /**
     * 修改
     * @param qcTaskReportItemValue
     */
    public void updateQcTaskReportItemValue(QcTaskReportItemValue qcTaskReportItemValue);
    
    /**
     * 删除
     * @param id
     */
    public void delQcTaskReportItemValue (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcTaskReportItemValue (List<String> idList);
}
