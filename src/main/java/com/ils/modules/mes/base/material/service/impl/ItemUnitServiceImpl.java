package com.ils.modules.mes.base.material.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.mapper.ItemUnitMapper;
import com.ils.modules.mes.base.material.service.ItemUnitService;
import com.ils.modules.mes.base.material.vo.ConvertUnitVO;

/**
 * @Description: 物料转换单位
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Service
public class ItemUnitServiceImpl extends ServiceImpl<ItemUnitMapper, ItemUnit> implements ItemUnitService {
	
	@Autowired
	private ItemUnitMapper itemUnitMapper;
	
	@Override
	public List<ItemUnit> selectByMainId(String mainId) {
		return itemUnitMapper.selectByMainId(mainId);
	}

    @Override
    public ConvertUnitVO selectConvertUnit(String unit, String itemId) {
        return itemUnitMapper.selectConvertUnit(unit, itemId);
    }

}
