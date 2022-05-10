package com.ils.modules.mes.base.qc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.qc.entity.QcItemType;

/**
 * @Description: 质检项分类
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcItemTypeService extends IService<QcItemType> {

    /**
     * 添加
     * @param qcItemType
     */
    public void saveQcItemType(QcItemType qcItemType) ;
    
    /**
     * 修改
     * @param qcItemType
     */
    public void updateQcItemType(QcItemType qcItemType);
    
    /**
     * 删除
     * @param id
     */
    public void delQcItemType (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcItemType (List<String> idList);
}
