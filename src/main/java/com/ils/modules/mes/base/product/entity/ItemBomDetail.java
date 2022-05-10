package com.ils.modules.mes.base.product.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 物料BOM明细表
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_bom_detail")
@ApiModel(value="mes_item_bom_detail对象", description="物料BOM明细表")
public class ItemBomDetail extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**bomid*/
    @TableField("bom_id")
    @ApiModelProperty(value = "bomid", position = 2)
	private String bomId;
	/**行号*/
    @Excel(name = "行号", width = 15)
    @TableField("seq")
    @ApiModelProperty(value = "行号", position = 3)
	private Integer seq;
	/**物料id*/
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 4)
	private String itemId;
	/**物料id*/
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 5)
    @KeyWord
	private String itemName;
	/**物料id*/
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 6)
    @KeyWord
	private String itemCode;
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

    /** 单位名称 */
    @Excel(name = "单位名称", width = 15)
    @TableField("unit_name")
    @ApiModelProperty(value = "单位名称", position = 8)
    private String unitName;
	/**耗损率*/
    @Excel(name = "耗损率", width = 15)
    @TableField("loss_rate")
    @ApiModelProperty(value = "耗损率", position = 9)
	private BigDecimal lossRate;
	/**投料管控:1,管控；0，不管控；*/
    @Excel(name = "投料管控:1,管控；0，不管控；", width = 15)
    @TableField("is_control")
    @ApiModelProperty(value = "投料管控:1,管控；0，不管控；", position = 10)
	private String control;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 11)
	private String note;
}
