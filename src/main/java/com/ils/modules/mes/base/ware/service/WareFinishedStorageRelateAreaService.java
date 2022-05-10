package com.ils.modules.mes.base.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.ware.entity.WareFinishedStorageRelateArea;

import java.util.List;

/**
 * @Description: 完工仓位
 * @Author: Tian
 * @Date:   2020-12-04
 * @Version: V1.0
 */
public interface WareFinishedStorageRelateAreaService extends IService<WareFinishedStorageRelateArea> {

    /**
     * 添加
     * @param wareFinishedStorageRelateArea
     */
    public void saveWareFinishedStorageRelateArea(WareFinishedStorageRelateArea wareFinishedStorageRelateArea) ;
    
    /**
     * 修改
     * @param wareFinishedStorageRelateArea
     */
    public void updateWareFinishedStorageRelateArea(WareFinishedStorageRelateArea wareFinishedStorageRelateArea);
    
    /**
     * 删除
     * @param id
     */
    public void delWareFinishedStorageRelateArea (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWareFinishedStorageRelateArea (List<String> idList);
}
