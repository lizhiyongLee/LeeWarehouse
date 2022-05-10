package com.ils.modules.mes.base.material.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.vo.ConvertUnitVO;

/**
 * @Description: 物料转换单位
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemUnitMapper extends ILSMapper<ItemUnit> {
    
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
	public boolean deleteByMainId(String mainId);
    
    /**
     * 通过主表 Id 查询
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
    public ConvertUnitVO selectConvertUnit(@Param("unit") String unit, @Param("itemId") String itemId);
}
