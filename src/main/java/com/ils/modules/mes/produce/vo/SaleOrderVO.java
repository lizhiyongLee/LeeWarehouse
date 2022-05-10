package com.ils.modules.mes.produce.vo;

import java.util.ArrayList;
import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.produce.entity.SaleOrder;
import com.ils.modules.mes.produce.entity.SaleOrderLine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 销售订单
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SaleOrderVO extends SaleOrder {

    private static final long serialVersionUID = 1L;

    @ExcelCollection(name = "销售订单物料行", orderNum = "10", type = ArrayList.class)
    private List<SaleOrderLine> saleOrderLineList;

}
