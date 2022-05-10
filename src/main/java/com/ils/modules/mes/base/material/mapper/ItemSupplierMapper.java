package com.ils.modules.mes.base.material.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.material.entity.ItemSupplier;
import com.ils.modules.mes.base.material.vo.ItemSupplierVO;

/**
 * @Description: 物料关联供应商
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemSupplierMapper extends ILSMapper<ItemSupplier> {
    
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
    public List<ItemSupplierVO> selectByMainId(String mainId);
}
