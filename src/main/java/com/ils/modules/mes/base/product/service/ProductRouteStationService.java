package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ProductRouteStation;
import com.ils.modules.mes.base.product.vo.ProductRouteStationVO;

/**
 * @Description: 产品路线工序工位
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductRouteStationService extends IService<ProductRouteStation> {

    /**
     * 通过父ID查询列表
     * 
     * @param productId
     * @return
     */
    public List<ProductRouteStationVO> selectByProductId(String productId);
}
