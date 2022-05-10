package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductBom;
import com.ils.modules.mes.base.product.vo.ProductBomVO;

/**
 * @Description: 产品BOM
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductBomMapper extends ILSMapper<ProductBom> {
    
    /**
     * 通过主表 productId 删除
     * 
     * @param productId
     * @return
     */
    public boolean deleteByProductId(String productId);
    
    /**
     * 通过主表 productId 查询
     * 
     * @param productId
     * @return
     */
    public List<ProductBomVO> selectByProductId(String productId);
}
