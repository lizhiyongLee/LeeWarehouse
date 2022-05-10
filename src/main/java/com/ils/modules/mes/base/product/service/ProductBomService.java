package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ProductBom;
import com.ils.modules.mes.base.product.vo.ProductBomVO;

/**
 * @Description: 产品BOM
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductBomService extends IService<ProductBom> {

    /**
     * 通过父productId查询列表
     * 
     * @param productId
     * @return
     */
    public List<ProductBomVO> selectByProductId(String productId);
}
