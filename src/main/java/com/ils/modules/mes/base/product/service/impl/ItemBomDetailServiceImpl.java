package com.ils.modules.mes.base.product.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.product.entity.ItemBomDetail;
import com.ils.modules.mes.base.product.mapper.ItemBomDetailMapper;
import com.ils.modules.mes.base.product.service.ItemBomDetailService;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.base.product.vo.ItemBomUnitVO;

/**
 * @Description: 物料BOM明细表
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Service
public class ItemBomDetailServiceImpl extends ServiceImpl<ItemBomDetailMapper, ItemBomDetail> implements ItemBomDetailService {
	
	@Autowired
	private ItemBomDetailMapper itemBomDetailMapper;
	
	@Override
    public List<ItemBomDetailVO> selectByMainId(String mainId) {
		return itemBomDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<ItemBomUnitVO> selectDetailInfoByMainId(String mainId) {
        List<ItemBomUnitVO> lstBomUnit = itemBomDetailMapper.selectDetailInfoByMainId(mainId);
        for (ItemBomUnitVO itemUnit : lstBomUnit) {
            if (!itemUnit.getConvertUnit().equals(itemUnit.getMainUnit())) {
                BigDecimal mainTotalQty =
                    BigDecimalUtils.divide(BigDecimalUtils.multiply(itemUnit.getQty(), itemUnit.getMainUnitQty()),
                        itemUnit.getConvertQty(), 6);
                itemUnit.setQty(mainTotalQty);
                itemUnit.setUnit(itemUnit.getMainUnit());
            }
        }
        return lstBomUnit;
    }

    @Override
    public List<ItemBomDetail> selectByBomId(String bomId){
        return itemBomDetailMapper.selectByBomId(bomId);
    }
}
