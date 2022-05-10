package com.ils.modules.mes.base.ware.service;

import java.util.List;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 投料仓位
 * @Author: Tian
 * @Date:   2020-12-04
 * @Version: V1.0
 */
public interface WareFeedingStorageRelateAreaService extends IService<WareFeedingStorageRelateArea> {

    /**
     * 添加
     * @param wareFeedingStorageRelateArea
     */
    public void saveWareFeedingStorageRelateArea(WareFeedingStorageRelateArea wareFeedingStorageRelateArea) ;
    
    /**
     * 修改
     * @param wareFeedingStorageRelateArea
     */
    public void updateWareFeedingStorageRelateArea(WareFeedingStorageRelateArea wareFeedingStorageRelateArea);
    
    /**
     * 删除
     * @param id
     */
    public void delWareFeedingStorageRelateArea (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWareFeedingStorageRelateArea (List<String> idList);
}
