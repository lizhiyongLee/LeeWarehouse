package com.ils.modules.mes.base.ware.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 投料仓返回查询
 * @author: fengyi
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProduceWareStorageVO implements Serializable {
    /** 仓位Id */
    private String storageId;
    /** 仓位编码 */
    @ApiModelProperty(value = "仓位编码", position = 3)
    private String storageCode;
    /** 仓位名称 */
    @ApiModelProperty(value = "仓位名称", position = 4)
    private String storageName;
    /** 合格数量 */
    private BigDecimal qualifiedQty;
    /** 待检数量 */
    private BigDecimal waitQty;
    /** 物料ID */
    private String itemId;
    /** 物料单元单元 */
    private String unit;
    /** 物料单元名称 */
    private String unitName;
}
