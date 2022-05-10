package com.ils.modules.mes.base.product.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ItemBom;

import java.util.List;

/**
 * @Description: 物料BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
public interface ItemBomMapper extends ILSMapper<ItemBom> {
    /**
     * 通过物料 Id 查询
     * @param itemId
     * @return
     */
    public List<ItemBom> selectByItemIdAndOrderByUpdateTime(String itemId);
}
