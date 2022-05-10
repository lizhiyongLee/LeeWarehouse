package com.ils.modules.mes.base.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ProductLine;
import com.ils.modules.mes.base.product.mapper.ProductLineMapper;
import com.ils.modules.mes.base.product.service.ProductLineService;
import com.ils.modules.mes.base.product.vo.ProductLineVO;

/**
 * @Description: 产品工艺路线明细
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class ProductLineServiceImpl extends ServiceImpl<ProductLineMapper, ProductLine> implements ProductLineService {
	
	@Autowired
	private ProductLineMapper productLineMapper;
	
	@Override
    public List<ProductLineVO> selectByProductId(String productId) {
        return productLineMapper.selectByProductId(productId);
	}
}
