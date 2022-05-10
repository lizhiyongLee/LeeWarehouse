package com.ils.modules.mes.base.material.entity;

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

import java.math.BigDecimal;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 9:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_container_qty")
@ApiModel(value = "mes_item_container_qty对象", description = "载具关联物料")
public class ItemContainerQty extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 载具id
     */
    @Excel(name = "载具id", width = 15)
    @ApiModelProperty(value = "载具id", position = 2)
    @TableField("container_id")
    private String containerId;
    /**
     * 载具编码
     */
    @Excel(name = "载具编码", width = 15)
    @ApiModelProperty(value = "载具编码", position = 3)
    @TableField("container_code")
    private String containerCode;
    /**
     * 载具名称
     */
    @Excel(name = "载具名称", width = 15)
    @ApiModelProperty(value = "载具名称", position = 4)
    @TableField("container_name")
    private String containerName;
    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 5)
    @KeyWord
    private String itemId;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 6)
    @KeyWord
    private String itemCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 7)
    @KeyWord
    private String itemName;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 8)
    private BigDecimal qty;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15, dicCode = "id", dictTable = "mes_unit", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "unit", position = 9)
    @Dict(dicCode = "id", dictTable = "mes_unit", dicText = "unit_name")
    private String unit;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 10)
    private String note;
}
