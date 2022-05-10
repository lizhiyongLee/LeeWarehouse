package com.ils.modules.mes.qc.service;

import java.util.List;
import com.ils.modules.mes.qc.entity.QcTaskRelateTotal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 质检任务关联物料整体
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskRelateTotalService extends IService<QcTaskRelateTotal> {

    /**
     * 添加
     * @param qcTaskRelateTotal
     */
    public void saveQcTaskRelateTotal(QcTaskRelateTotal qcTaskRelateTotal) ;
    
    /**
     * 修改
     * @param qcTaskRelateTotal
     */
    public void updateQcTaskRelateTotal(QcTaskRelateTotal qcTaskRelateTotal);
    
    /**
     * 删除
     * @param id
     */
    public void delQcTaskRelateTotal (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcTaskRelateTotal (List<String> idList);
}
