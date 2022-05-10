package com.ils.modules.mes.base.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.ItemType;

/**
 * @Description: 物料类型
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
public interface ItemTypeService extends IService<ItemType> {

    /**
     * 添加
     * @param itemType
     */
    public void saveItemType(ItemType itemType) ;
    
    /**
     * 修改
     * @param itemType
     */
    public void updateItemType(ItemType itemType);
    
    /**
     * 删除
     * @param id
     */
    public void delItemType (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchItemType (List<String> idList);
}
