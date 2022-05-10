package com.ils.modules.mes.base.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ProductBomSubstitute;
import com.ils.modules.mes.base.product.mapper.ProductBomSubstituteMapper;
import com.ils.modules.mes.base.product.service.ProductBomSubstituteService;
import com.ils.modules.mes.base.product.vo.ProductBomSubstituteVO;

/**
 * @Description: 产品BOM替代料
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class ProductBomSubstituteServiceImpl extends ServiceImpl<ProductBomSubstituteMapper, ProductBomSubstitute> implements ProductBomSubstituteService {
	
	@Autowired
	private ProductBomSubstituteMapper productBomSubstituteMapper;
	
	@Override
    public List<ProductBomSubstituteVO> selectByProductId(String productId) {
        return productBomSubstituteMapper.selectByProductId(productId);
	}
}
