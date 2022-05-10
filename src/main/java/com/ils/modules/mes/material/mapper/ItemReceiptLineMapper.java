package com.ils.modules.mes.material.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemReceiptLine;

/**
 * @Description: 收货单行
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface ItemReceiptLineMapper extends ILSMapper<ItemReceiptLine> {
    
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
	public List<ItemReceiptLine> selectByMainId(String mainId);
}
