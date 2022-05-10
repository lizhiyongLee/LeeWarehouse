package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductLine;
import com.ils.modules.mes.base.product.vo.ProductLineBomVO;
import com.ils.modules.mes.base.product.vo.ProductLineVO;

/**
 * @Description: 产品工艺路线明细
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductLineMapper extends ILSMapper<ProductLine> {
    
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
    public List<ProductLineVO> selectByProductId(String productId);

    /**
     * 
     * 查询产品 工艺以及物料 BOM
     * 
     * @param id
     * @return ProductLineBomVO
     * @date 2020年11月13日
     */
    public List<ProductLineBomVO> queryProductLineBomById(String id);
}
