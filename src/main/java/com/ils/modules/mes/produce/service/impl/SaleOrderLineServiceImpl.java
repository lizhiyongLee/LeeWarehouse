package com.ils.modules.mes.produce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.mapper.SaleOrderLineMapper;
import com.ils.modules.mes.produce.service.SaleOrderLineService;
import com.ils.modules.mes.produce.vo.SaleOrderLineVO;

/**
 * @Description: 销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Service
public class SaleOrderLineServiceImpl extends ServiceImpl<SaleOrderLineMapper, SaleOrderLine> implements SaleOrderLineService {
	
	@Autowired
	private SaleOrderLineMapper saleOrderLineMapper;
	
	@Override
	public List<SaleOrderLine> selectByMainId(String mainId) {
		return saleOrderLineMapper.selectByMainId(mainId);
	}

    @Override
    public IPage<SaleOrderLineVO> saleOrderLinePage(Page<SaleOrderLineVO> page,
        QueryWrapper<SaleOrderLineVO> queryWrapper) {
        return saleOrderLineMapper.saleOrderLinePage(page, queryWrapper);
    }

}
