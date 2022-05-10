package com.ils.modules.mes.base.material.entity;

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

import java.math.BigDecimal;

/**
 * @Description: 物料转换单位
 * @Author: hezhigang
 * @Date:   2020-10-23
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_unit")
@ApiModel(value="mes_item_unit对象", description="物料转换单位")
public class ItemUnit extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**物料id*/
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 2)
	private String itemId;
	/**主单位数量*/
    @Excel(name = "主单位数量", width = 15, dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @TableField("main_unit_qty")
    @ApiModelProperty(value = "主单位数量", position = 3)
	private BigDecimal mainUnitQty;
	/**主单位名称*/
    @Excel(name = "主单位", width = 15, dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @TableField("main_unit")
    @ApiModelProperty(value = "主单位", position = 4)
    @Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
	private String mainUnit;
	/**转换单位数量*/
    @Excel(name = "转换单位数量", width = 15)
    @TableField("convert_qty")
    @ApiModelProperty(value = "转换单位数量", position = 5)
	private BigDecimal convertQty;
	/**转换单位名称*/
    @Excel(name = "转换单位", width = 15, dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @TableField("convert_unit")
    @ApiModelProperty(value = "转换单位", position = 6)
    @Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
	private String convertUnit;
	/**转换单位精度*/
    @Excel(name = "转换单位精度", width = 15)
    @TableField("accuracy")
    @ApiModelProperty(value = "转换单位精度", position = 7)
	private Integer accuracy;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 8)
	private String note;
	/**序号*/
    @Excel(name = "序号", width = 15)
    @TableField("seq")
    @ApiModelProperty(value = "序号", position = 9)
	private Integer seq;
    /**状态*/
    @Excel(name = "状态", width = 15)
    @TableField("status")
    @ApiModelProperty(value = "状态", position = 10)
    @Dict(dicCode = "mesStatus")
	private String status;
}
