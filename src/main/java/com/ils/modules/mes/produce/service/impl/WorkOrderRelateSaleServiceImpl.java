package com.ils.modules.mes.produce.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkOrderRelateSale;
import com.ils.modules.mes.produce.mapper.WorkOrderRelateSaleMapper;
import com.ils.modules.mes.produce.service.WorkOrderRelateSaleService;

/**
 * @Description: 工单关联销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Service
public class WorkOrderRelateSaleServiceImpl extends ServiceImpl<WorkOrderRelateSaleMapper, WorkOrderRelateSale> implements WorkOrderRelateSaleService {

    @Override
    public void saveWorkOrderRelateSale(WorkOrderRelateSale workOrderRelateSale) {
         baseMapper.insert(workOrderRelateSale);
    }

    @Override
    public void updateWorkOrderRelateSale(WorkOrderRelateSale workOrderRelateSale) {
        baseMapper.updateById(workOrderRelateSale);
    }

    @Override
    public void delWorkOrderRelateSale(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchWorkOrderRelateSale(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
