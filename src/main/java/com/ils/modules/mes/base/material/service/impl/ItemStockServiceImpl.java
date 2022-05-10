package com.ils.modules.mes.base.material.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.material.entity.ItemStock;
import com.ils.modules.mes.base.material.mapper.ItemStockMapper;
import com.ils.modules.mes.base.material.service.ItemStockService;

/**
 * @Description: 物料关联库存
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Service
public class ItemStockServiceImpl extends ServiceImpl<ItemStockMapper, ItemStock> implements ItemStockService {
	
	@Autowired
	private ItemStockMapper itemStockMapper;
	
	@Override
	public List<ItemStock> selectByMainId(String mainId) {
		return itemStockMapper.selectByMainId(mainId);
	}
}
