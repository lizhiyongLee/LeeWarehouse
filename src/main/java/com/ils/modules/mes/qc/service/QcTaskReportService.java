package com.ils.modules.mes.qc.service;

import java.util.List;
import com.ils.modules.mes.qc.entity.QcTaskReport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 质检任务报告
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskReportService extends IService<QcTaskReport> {

    /**
     * 添加
     * @param qcTaskReport
     */
    public void saveQcTaskReport(QcTaskReport qcTaskReport) ;
    
    /**
     * 修改
     * @param qcTaskReport
     */
    public void updateQcTaskReport(QcTaskReport qcTaskReport);
    
    /**
     * 删除
     * @param id
     */
    public void delQcTaskReport (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcTaskReport (List<String> idList);
}
