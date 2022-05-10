package com.ils.modules.mes.produce.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 工单进度VO
 * @author lishaojie
 * @date 2021/5/25 9:57
 */
@Data
public class WorkOrderProgressVO  extends ILSEntity {
    private Integer orderLayer;
    private String orderNo;
    private String itemName;
    private String itemCode;
    private String spec;
    private BigDecimal planQty;
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unit;
    private BigDecimal completedQty;
}
