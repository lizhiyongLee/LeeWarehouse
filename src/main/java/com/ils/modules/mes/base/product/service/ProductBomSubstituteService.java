package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ProductBomSubstitute;
import com.ils.modules.mes.base.product.vo.ProductBomSubstituteVO;

/**
 * @Description: 产品BOM替代料
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductBomSubstituteService extends IService<ProductBomSubstitute> {

    /**
     * 通过父ID查询列表
     * 
     * @param productId
     * @return
     */
    public List<ProductBomSubstituteVO> selectByProductId(String productId);
}
