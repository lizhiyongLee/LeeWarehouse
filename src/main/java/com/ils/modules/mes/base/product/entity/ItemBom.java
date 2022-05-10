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
 * @Description: 物料BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_bom")
@ApiModel(value="mes_item_bom对象", description="物料BOM")
public class ItemBom extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**产品编码*/
    @Excel(name = "产品id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "产品id", position = 2)
	private String itemId;
	/**产品编码*/
    @Excel(name = "产品编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "产品编码", position = 3)
	private String itemCode;
	/**产品编码*/
    @Excel(name = "产品名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "产品名称", position = 4)
	private String itemName;
	/**规格描述*/
    @Excel(name = "规格描述", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "规格描述", position = 5)
	private String spec;
	/**版本*/
    @Excel(name = "版本", width = 15)
    @TableField("version")
    @ApiModelProperty(value = "版本", position = 6)
	private String version;
	/**数量*/
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 7)
	private BigDecimal qty;
	/**单位*/
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 8)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 9)
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 10)
	private String note;
}
