package com.ils.modules.mes.qc.service;

import java.util.List;
import com.ils.modules.mes.qc.entity.QcTaskItemStandard;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 质检任务关联质检标准
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskItemStandardService extends IService<QcTaskItemStandard> {

    /**
     * 添加
     * @param qcTaskItemStandard
     */
    public void saveQcTaskItemStandard(QcTaskItemStandard qcTaskItemStandard) ;
    
    /**
     * 修改
     * @param qcTaskItemStandard
     */
    public void updateQcTaskItemStandard(QcTaskItemStandard qcTaskItemStandard);
    
    /**
     * 删除
     * @param id
     */
    public void delQcTaskItemStandard (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcTaskItemStandard (List<String> idList);
}
