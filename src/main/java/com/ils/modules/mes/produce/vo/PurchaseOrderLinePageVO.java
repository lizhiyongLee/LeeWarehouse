package com.ils.modules.mes.produce.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/2/4 9:49
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderLinePageVO extends PurchaseOrderLine {
    private static final long serialVersionUID = 1L;
    /**
     * 采购单编码
     */
    private String purchaseCode;
    /**
     * 供应商
     */
    private String supplierName;
    /**
     * 处理人
     */
    @Dict(dicCode = "id", dicText = "realname", dictTable = "sys_user")
    private String handpersonId;
    /**
     * 备注
     */
    private String note;
}
