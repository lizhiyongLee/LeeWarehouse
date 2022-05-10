package com.ils.modules.mes.base.craft.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;

/**
 * @Description: 工序关联不良项
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessNgItemMapper extends ILSMapper<ProcessNgItem> {
    
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
	public List<ProcessNgItem> selectByMainId(String mainId);
}
