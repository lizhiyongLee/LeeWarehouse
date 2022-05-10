package com.ils.modules.mes.base.material.mapper;

import org.apache.ibatis.annotations.Param;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.material.entity.Item;

/**
 * @Description: 物料定义
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemMapper extends ILSMapper<Item> {
    
    
    /**
     * 
     *  根据ID查询是否发生业务数据
     * @param itemId
     * @return
     */
    int queryBussDataByItemId(@Param("itemId") String itemId);

    /**
     *
     *  根据itemCode查询物料
     * @param itemCode
     * @return
     */
    Item queryItemByCode(String itemCode);

}
