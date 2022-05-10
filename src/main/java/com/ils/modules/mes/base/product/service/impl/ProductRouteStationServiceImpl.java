package com.ils.modules.mes.base.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ProductRouteStation;
import com.ils.modules.mes.base.product.mapper.ProductRouteStationMapper;
import com.ils.modules.mes.base.product.service.ProductRouteStationService;
import com.ils.modules.mes.base.product.vo.ProductRouteStationVO;

/**
 * @Description: 产品路线工序工位
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class ProductRouteStationServiceImpl extends ServiceImpl<ProductRouteStationMapper, ProductRouteStation> implements ProductRouteStationService {
	
	@Autowired
	private ProductRouteStationMapper productRouteStationMapper;
	
	@Override
    public List<ProductRouteStationVO> selectByProductId(String productId) {
        return productRouteStationMapper.selectByProductId(productId);
	}
}
