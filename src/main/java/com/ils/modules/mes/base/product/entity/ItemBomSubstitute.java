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
 * @Description: 物料BOM替代料
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Data
@TableName("mes_item_bom_substitute")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_item_bom_substitute对象", description="物料BOM替代料")
public class ItemBomSubstitute extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**bomid*/
	@Excel(name = "bomid", width = 15)
    @ApiModelProperty(value = "bomid", position = 2)
    @TableField("bom_id")
	private String bomId;
	/**被替代料id*/
	@Excel(name = "被替代料id", width = 15)
    @ApiModelProperty(value = "被替代料id", position = 3)
    @TableField("item_id")
	private String itemId;
	/**替代料id*/
	@Excel(name = "替代料id", width = 15)
    @ApiModelProperty(value = "替代料id", position = 4)
    @TableField("subtitute_item_id")
	private String subtituteItemId;
	/**被替代料数量*/
	@Excel(name = "被替代料数量", width = 15)
    @ApiModelProperty(value = "被替代料数量", position = 5)
    @TableField("let_subtitute_qty")
	private BigDecimal letSubtituteQty;
	/**被替代料单位*/
	@Excel(name = "被替代料单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @ApiModelProperty(value = "被替代料单位", position = 6)
    @TableField("let_subtitute_unit")
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String letSubtituteUnit;

    /** 被替代料单位名称 */
    @Excel(name = "被替代料单位名称", width = 15)
    @ApiModelProperty(value = "被替代料单位名称", position = 6)
    @TableField("let_subtitute_unit_name")
    private String letSubtituteUnitName;

	/**替代料数量*/
	@Excel(name = "替代料数量", width = 15)
    @ApiModelProperty(value = "替代料数量", position = 7)
    @TableField("subtitute_qty")
	private BigDecimal subtituteQty;
	/**替代料单位*/
	@Excel(name = "替代料单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @ApiModelProperty(value = "替代料单位", position = 8)
    @TableField("subtitute_unit")
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String subtituteUnit;

    /** 替代料单位名称 */
    @Excel(name = "替代料单位名称", width = 15)
    @ApiModelProperty(value = "替代料单位名称", position = 8)
    @TableField("subtitute_unit_name")
    private String subtituteUnitName;
}
