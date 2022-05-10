package com.ils.modules.mes.base.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.vo.ConvertUnitVO;

/**
 * @Description: 物料转换单位
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemUnitService extends IService<ItemUnit> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<ItemUnit> selectByMainId(String mainId);

    /**
     * 
     * 转换单位
     * 
     * @param unit
     * @param itemId
     * @return
     * @date 2021年1月21日
     */
    public ConvertUnitVO selectConvertUnit(String unit, String itemId);


}
