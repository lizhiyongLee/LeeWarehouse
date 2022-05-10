package com.ils.modules.mes.base.product.vo;

import java.math.BigDecimal;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产品 BOM
 * @author: fengyi
 * @date: 2020年11月13日 上午11:10:00
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductLineBomVO extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /** 产品bomid */
    @ApiModelProperty(value = "产品bomid", position = 3)
    private String productId;
    /** 产品工艺路线明细id */
    @ApiModelProperty(value = "产品工艺路线明细id", position = 4)
    private String productLineId;
    /** 工序序号 */
    @ApiModelProperty(value = "工序序号", position = 5)
    private Integer seq;
    /** 工序id */
    @ApiModelProperty(value = "工序id", position = 6)
    private String processId;
    /** 工序编码 */
    @ApiModelProperty(value = "工序编码", position = 7)
    private String processCode;
    /** 工序名称 */
    @ApiModelProperty(value = "工序名称", position = 8)
    private String processName;
    /** 物料id */
    @ApiModelProperty(value = "物料id", position = 9)
    private String itemId;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码", position = 10)
    private String itemCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称", position = 11)
    private String itemName;
    /** 数量 */
    @ApiModelProperty(value = "数量", position = 12)
    private BigDecimal qty;
    /** 单位 */
    @ApiModelProperty(value = "单位", position = 13)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unit;
    /** 耗损率 */
    @ApiModelProperty(value = "耗损率", position = 14)
    private BigDecimal lossRate;
    /** 投料管控 */
    @ApiModelProperty(value = "投料管控", position = 15)
    private String control;
    /** 备注 */
    @ApiModelProperty(value = "备注", position = 16)
    private String note;

    /** 主单位数量 */
    private BigDecimal mainUnitQty;
    /** 主单位 */
    private String mainUnit;
    /** 转换单位数量 */
    private BigDecimal convertQty;
    /** 转换单位 */
    private String convertUnit;

}
