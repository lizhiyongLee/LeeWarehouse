package com.ils.modules.mes.base.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ProductRouteMethod;
import com.ils.modules.mes.base.product.mapper.ProductRouteMethodMapper;
import com.ils.modules.mes.base.product.service.ProductRouteMethodService;
import com.ils.modules.mes.base.product.vo.ProductRouteMethodVO;

/**
 * @Description: 产品路线工序质检方案
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class ProductRouteMethodServiceImpl extends ServiceImpl<ProductRouteMethodMapper, ProductRouteMethod> implements ProductRouteMethodService {
	
	@Autowired
	private ProductRouteMethodMapper productRouteMethodMapper;
	
	@Override
    public List<ProductRouteMethodVO> selectByProductId(String productId) {
        return productRouteMethodMapper.selectByProductId(productId);
	}
}
