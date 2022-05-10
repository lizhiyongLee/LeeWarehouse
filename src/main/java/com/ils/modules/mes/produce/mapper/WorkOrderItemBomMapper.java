package com.ils.modules.mes.produce.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkOrderItemBom;

/**
 * @Description: 工单物料清单明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderItemBomMapper extends ILSMapper<WorkOrderItemBom> {
    
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
	public List<WorkOrderItemBom> selectByMainId(String mainId);
}
