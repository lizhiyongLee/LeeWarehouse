package com.ils.modules.mes.produce.vo;

import com.ils.modules.mes.produce.entity.PurchaseOrder;
import lombok.*;

/**
 * 采购清单VO
 * @author Anna.
 * @date 2021/2/2 10:28
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderVO extends PurchaseOrder {

    private static final long serialVersionUID = 1L;
    /**
     * 物料名称
     */
    private String itemName;
    /**
     * 物料编码
     */
    private String itemCode;
    /**
     * 进度
     */
    private String planProcess;

}
