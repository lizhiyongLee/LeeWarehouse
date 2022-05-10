package com.ils.modules.mes.base.material.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.material.entity.ItemSupplier;
import com.ils.modules.mes.base.material.mapper.ItemSupplierMapper;
import com.ils.modules.mes.base.material.service.ItemSupplierService;
import com.ils.modules.mes.base.material.vo.ItemSupplierVO;

/**
 * @Description: 物料关联供应商
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Service
public class ItemSupplierServiceImpl extends ServiceImpl<ItemSupplierMapper, ItemSupplier> implements ItemSupplierService {
	
	@Autowired
	private ItemSupplierMapper itemSupplierMapper;
	
	@Override
    public List<ItemSupplierVO> selectByMainId(String mainId) {
		return itemSupplierMapper.selectByMainId(mainId);
	}
}
