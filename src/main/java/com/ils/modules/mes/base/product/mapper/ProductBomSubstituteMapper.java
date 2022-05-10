package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductBomSubstitute;
import com.ils.modules.mes.base.product.vo.ProductBomSubstituteVO;

/**
 * @Description: 产品BOM替代料
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductBomSubstituteMapper extends ILSMapper<ProductBomSubstitute> {
    
    /**
     * 通过主表 Id 删除
     * 
     * @param productId
     * @return
     */
    public boolean deleteByProductId(String productId);
    
    /**
     * 通过主表 Id 查询
     * 
     * @param productId
     * @return
     */
    public List<ProductBomSubstituteVO> selectByProductId(String productId);
}
