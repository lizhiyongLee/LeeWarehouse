package com.ils.modules.mes.machine.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * 备件详情
 *
 * @author Anna.
 * @date 2021/2/26 11:21
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SparePartsReceiptOrSendOrderVO extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 备件id
     */
    private String sparePartsId;
    /**
     * 出入库单号
     */
    @KeyWord
    private String orderCode;
    /**
     * 订单日期
     */
    private String orderDate;
    /**
     * 库存位置
     */
    @KeyWord
    private String storage;
    /**
     * 出入库方向-出库or入库
     */
    @Dict(dicCode = "mesStorageFlowType")
    private String flowType;
    /**
     * 订单类型，领用or采购or盘点。。。
     */
    @Dict(dicCode = "mesSparePartsStorageType")
    private String oderType;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 备注
     */
    private String note;
}
