package com.ils.modules.mes.base.material.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.material.entity.ItemQualityEmployee;

/**
 * @Description: 物料关联质检人员
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemQualityEmployeeMapper extends ILSMapper<ItemQualityEmployee> {
    
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
	public List<ItemQualityEmployee> selectByMainId(String mainId);
}
