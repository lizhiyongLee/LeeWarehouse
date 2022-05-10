package com.ils.modules.mes.base.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.machine.entity.SparePartsHouse;

/**
 * @Description: 备件库
 * @Author: Tian
 * @Date:   2021-02-23
 * @Version: V1.0
 */
public interface SparePartsHouseService extends IService<SparePartsHouse> {

    /**
     * 添加
     * @param sparePartsHouse
     */
    public void saveSparePartsHouse(SparePartsHouse sparePartsHouse) ;
    
    /**
     * 修改
     * @param sparePartsHouse
     */
    public void updateSparePartsHouse(SparePartsHouse sparePartsHouse);

}
