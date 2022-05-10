package com.ils.modules.mes.base.material.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.material.entity.ItemQualityEmployee;
import com.ils.modules.mes.base.material.mapper.ItemQualityEmployeeMapper;
import com.ils.modules.mes.base.material.service.ItemQualityEmployeeService;

/**
 * @Description: 物料关联质检人员
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Service
public class ItemQualityEmployeeServiceImpl extends ServiceImpl<ItemQualityEmployeeMapper, ItemQualityEmployee> implements ItemQualityEmployeeService {
	
	@Autowired
	private ItemQualityEmployeeMapper itemQualityEmployeeMapper;
	
	@Override
	public List<ItemQualityEmployee> selectByMainId(String mainId) {
		return itemQualityEmployeeMapper.selectByMainId(mainId);
	}
}
