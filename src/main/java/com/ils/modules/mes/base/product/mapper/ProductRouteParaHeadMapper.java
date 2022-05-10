package com.ils.modules.mes.base.product.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductRouteParaHead;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 10:36
 */
public interface ProductRouteParaHeadMapper extends ILSMapper<ProductRouteParaHead> {
    /**
     * 通过主表 Id 删除
     * @param productId
     * @return
     */
    public boolean deleteByMainId(String productId);
}
