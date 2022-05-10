package com.ils.modules.mes.base.product.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 产品BOM替代料
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_product_bom_substitute")
@ApiModel(value="mes_product_bom_substitute对象", description="产品BOM替代料")
public class ProductBomSubstitute extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**产品id*/
    @TableField("product_id")
    @ApiModelProperty(value = "产品id", position = 2)
	private String productId;
	/**bomid*/
    @Excel(name = "bomid", width = 15)
    @TableField("product_bom_id")
    @ApiModelProperty(value = "bomid", position = 3)
	private String productBomId;
	/**被替代料id*/
    @Excel(name = "被替代料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "被替代料id", position = 4)
	private String itemId;
	/**替代料id*/
    @Excel(name = "替代料id", width = 15)
    @TableField("subtitute_item_id")
    @ApiModelProperty(value = "替代料id", position = 5)
	private String subtituteItemId;
	/**被替代料数量*/
    @Excel(name = "被替代料数量", width = 15)
    @TableField("let_subtitute_qty")
    @ApiModelProperty(value = "被替代料数量", position = 6)
	private BigDecimal letSubtituteQty;
	/**被替代料单位*/
    @Excel(name = "被替代料单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("let_subtitute_unit")
    @ApiModelProperty(value = "被替代料单位", position = 7)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String letSubtituteUnit;

    /** 被替代料单位名称 */
    @Excel(name = "被替代料单位名称", width = 15)
    @TableField("let_subtitute_unit_name")
    @ApiModelProperty(value = "被替代料单位名称", position = 7)
    private String letSubtituteUnitName;
	/**替代料数量*/
    @Excel(name = "替代料数量", width = 15)
    @TableField("subtitute_qty")
    @ApiModelProperty(value = "替代料数量", position = 8)
	private BigDecimal subtituteQty;
	/**替代料单位*/
    @Excel(name = "替代料单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("subtitute_unit")
    @ApiModelProperty(value = "替代料单位", position = 9)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String subtituteUnit;

    /** 替代料单位名称 */
    @Excel(name = "替代料单位名称", width = 15)
    @TableField("subtitute_unit_name")
    @ApiModelProperty(value = "替代料单位名称", position = 9)
    private String subtituteUnitName;
}
