package com.ils.modules.mes.produce.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkOrderLineStation;

import java.util.List;

/**
 * @Description: 工单工序对应工位
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderLineStationMapper extends ILSMapper<WorkOrderLineStation> {
    
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
	public List<WorkOrderLineStation> selectByMainId(String mainId);
}
