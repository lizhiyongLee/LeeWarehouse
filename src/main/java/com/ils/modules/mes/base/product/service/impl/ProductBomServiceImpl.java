package com.ils.modules.mes.base.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ProductBom;
import com.ils.modules.mes.base.product.mapper.ProductBomMapper;
import com.ils.modules.mes.base.product.service.ProductBomService;
import com.ils.modules.mes.base.product.vo.ProductBomVO;

/**
 * @Description: 产品BOM
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class ProductBomServiceImpl extends ServiceImpl<ProductBomMapper, ProductBom> implements ProductBomService {
	
	@Autowired
	private ProductBomMapper productBomMapper;
	
	@Override
    public List<ProductBomVO> selectByProductId(String productId) {
        return productBomMapper.selectByProductId(productId);
	}
}
