package com.ils.modules.mes.produce.vo;

import com.ils.modules.mes.produce.entity.PurchaseOrder;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import lombok.*;

import java.util.List;

/**
 * 采购订单详情VO
 * @author Anna.
 * @date 2021/2/2 11:52
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderDetailVO extends PurchaseOrder{
    private static final long serialVersionUID = 1L;
    /**
     * 采购订单行
     */
    private List<PurchaseOrderLine> purchaseOrderLineList;
}
