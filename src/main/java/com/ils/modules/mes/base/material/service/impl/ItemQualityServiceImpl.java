package com.ils.modules.mes.base.material.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.material.entity.ItemQuality;
import com.ils.modules.mes.base.material.mapper.ItemQualityMapper;
import com.ils.modules.mes.base.material.service.ItemQualityService;
import com.ils.modules.mes.base.material.vo.ItemQualityVO;

/**
 * @Description: 物料关联质检方案
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Service
public class ItemQualityServiceImpl extends ServiceImpl<ItemQualityMapper, ItemQuality> implements ItemQualityService {
	
	@Autowired
	private ItemQualityMapper itemQualityMapper;
	
	@Override
    public List<ItemQualityVO> selectByMainId(String mainId) {
		return itemQualityMapper.selectByMainId(mainId);
	}
}
