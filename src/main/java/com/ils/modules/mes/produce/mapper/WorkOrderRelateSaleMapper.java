package com.ils.modules.mes.produce.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkOrderRelateSale;

/**
 * @Description: 工单关联销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
public interface WorkOrderRelateSaleMapper extends ILSMapper<WorkOrderRelateSale> {

    /**
     * 通过主表 Id 删除
     * 
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);

    /**
     * 通过主表 Id 查询
     * 
     * @param mainId
     * @return
     */
    public List<WorkOrderRelateSale> selectByMainId(String mainId);

}
