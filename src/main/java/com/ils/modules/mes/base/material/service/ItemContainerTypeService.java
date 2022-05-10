package com.ils.modules.mes.base.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.ItemContainerType;

import java.util.List;

/**
 * @Description: 物料类型
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
public interface ItemContainerTypeService extends IService<ItemContainerType> {

    /**
     * 添加
     * @param itemContainerType
     */
    public void saveItemContainerType(ItemContainerType itemContainerType) ;

    /**
     * 修改
     * @param itemContainerType
     */
    public void updateItemContainerType(ItemContainerType itemContainerType);

    /**
     * 删除
     * @param id
     */
    public void delItemContainerType (String id);

    /**
     * 批量删除
     * @param idList
     */
    public void delBatchItemContainerType (List<String> idList);
}
