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
 * @Description: 产品
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_product")
@ApiModel(value="mes_product对象", description="产品")
public class Product extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**物料清单BOMid*/
    @Excel(name = "物料清单BOMid", width = 15)
    @TableField("item_bom_id")
    @ApiModelProperty(value = "物料清单BOMid", position = 2)
	private String itemBomId;
	/**工艺路线id*/
    @Excel(name = "工艺路线id", width = 15)
    @TableField("route_id")
    @ApiModelProperty(value = "工艺路线id", position = 3)
	private String routeId;
	/**物料ID*/
    @Excel(name = "物料ID", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料ID", position = 4)
	private String itemId;
	/**物料编码*/
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 5)
	private String itemCode;
	/**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 6)
	private String itemName;
	/**规格描述*/
    @Excel(name = "规格描述", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "规格描述", position = 7)
	private String spec;
	/**版本*/
    @Excel(name = "版本", width = 15)
    @TableField("version")
    @ApiModelProperty(value = "版本", position = 8)
	private String version;
	/**数量*/
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 9)
	private BigDecimal qty;
	/**单位*/
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 10)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 11)
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 12)
	private String note;
}
