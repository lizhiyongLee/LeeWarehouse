package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ProductRouteMethod;
import com.ils.modules.mes.base.product.vo.ProductRouteMethodVO;

/**
 * @Description: 产品路线工序质检方案
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductRouteMethodService extends IService<ProductRouteMethod> {

    /**
     * 通过父ID查询列表
     * 
     * @param productId
     * @return
     */
    public List<ProductRouteMethodVO> selectByProductId(String productId);
}
