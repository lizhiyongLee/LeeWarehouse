package com.ils.modules.mes.material.entity;

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
 * @date 2021/10/26 14:35
 */
@Data
@TableName("mes_item_container_load_item_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_item_container_load_item_record对象", description = "载具转移记录")
public class ItemContainerLoadItemRecord extends ILSEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 载具关联物料主表id
     */
    @Excel(name = "载具关联物料主表id", width = 15)
    @ApiModelProperty(value = "载具关联物料主表id", position = 2)
    @TableField("container_manage_id")
    private String containerManageId;

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
    @ApiModelProperty(value = "载具编码", position = 2)
    @TableField("container_code")
    private String containerCode;
    /**
     * 载具名称
     */
    @Excel(name = "载具名称", width = 15)
    @ApiModelProperty(value = "载具名称", position = 2)
    @TableField("container_name")
    private String containerName;
    /**
     * 载具标签码
     */
    @Excel(name = "载具标签码", width = 15)
    @ApiModelProperty(value = "载具标签码", position = 3)
    @TableField("container_qrcode")
    private String containerQrcode;
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
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格", position = 9)
    @TableField("spec")
    @KeyWord
    private String spec;
    /**
     * 物料单元id
     */
    @Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 2)
    @TableField("item_cell_id")
    private String itemCellId;
    /**
     * 物料标签码
     */
    @Excel(name = "物料标签码", width = 15)
    @ApiModelProperty(value = "物料标签码", position = 2)
    @TableField("item_cell_qrcode")
    private String itemCellQrcode;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 10)
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 单位，以生成时进入表中时第一次业务单位存储。
     */
    @Excel(name = "单位ID", width = 15)
    @ApiModelProperty(value = "单位，以生成时进入表中时第一次业务单位存储。", position = 11)
    @TableField("unit_id")
    private String unitId;
    /**
     * 单位名称，以生成时进入表中时第一次业务单位存储。
     */
    @Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称，以生成时进入表中时第一次业务单位存储。", position = 12)
    @TableField("unit_name")
    private String unitName;
    /**
     * 1、装载；2、卸载。
     */
    @Excel(name = "1、装载；2、卸载。", width = 15)
    @ApiModelProperty(value = "1、装载；2、卸载。", position = 12)
    @TableField("load_type")
    @Dict(dicCode = "mesContainerLoadType")
    private String loadType;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 45)
    @TableField("attach")
    private String attach;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 46)
    @TableField("note")
    private String note;
}
