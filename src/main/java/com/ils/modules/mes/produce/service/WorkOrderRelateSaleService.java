package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkOrderRelateSale;

/**
 * @Description: 工单关联销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
public interface WorkOrderRelateSaleService extends IService<WorkOrderRelateSale> {

    /**
     * 添加
     * @param workOrderRelateSale
     */
    public void saveWorkOrderRelateSale(WorkOrderRelateSale workOrderRelateSale) ;
    
    /**
     * 修改
     * @param workOrderRelateSale
     */
    public void updateWorkOrderRelateSale(WorkOrderRelateSale workOrderRelateSale);
    
    /**
     * 删除
     * @param id
     */
    public void delWorkOrderRelateSale (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWorkOrderRelateSale (List<String> idList);
}
