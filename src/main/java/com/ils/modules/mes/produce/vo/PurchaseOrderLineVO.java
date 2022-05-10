package com.ils.modules.mes.produce.vo;

import com.ils.modules.mes.produce.entity.PurchaseOrder;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import lombok.*;

import java.util.List;

/**
 * 采购清单物料行VO
 *
 * @author Anna.
 * @date 2021/2/3 13:42
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderLineVO extends PurchaseOrder {
    private static final long serialVersionUID = 1L;

    /**
     * 采购清单物料行集合
     */
    private List<PurchaseOrderLine> purchaseOrderLineList;
}
