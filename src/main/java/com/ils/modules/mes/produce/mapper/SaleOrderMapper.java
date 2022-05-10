package com.ils.modules.mes.produce.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.SaleOrder;

/**
 * @Description: 销售订单
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
public interface SaleOrderMapper extends ILSMapper<SaleOrder> {

    /**
     * 根据订单号查询订单
     * @param no 订单号
     * @return
     */
    SaleOrder selectByNo(String no);
}
