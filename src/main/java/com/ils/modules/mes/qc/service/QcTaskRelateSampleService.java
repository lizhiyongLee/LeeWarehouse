package com.ils.modules.mes.qc.service;

import java.util.List;
import com.ils.modules.mes.qc.entity.QcTaskRelateSample;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 质检任务关联物料样本
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskRelateSampleService extends IService<QcTaskRelateSample> {

    /**
     * 添加
     * @param qcTaskRelateSample
     */
    public void saveQcTaskRelateSample(QcTaskRelateSample qcTaskRelateSample) ;
    
    /**
     * 修改
     * @param qcTaskRelateSample
     */
    public void updateQcTaskRelateSample(QcTaskRelateSample qcTaskRelateSample);
    
    /**
     * 删除
     * @param id
     */
    public void delQcTaskRelateSample (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcTaskRelateSample (List<String> idList);
}
