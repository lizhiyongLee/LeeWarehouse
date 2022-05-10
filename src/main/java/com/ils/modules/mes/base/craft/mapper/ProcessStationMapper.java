package com.ils.modules.mes.base.craft.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.ProcessStation;

/**
 * @Description: 工序关联工位
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessStationMapper extends ILSMapper<ProcessStation> {

    /**
     * 通过主表 Id 删除
     * 
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);
}
