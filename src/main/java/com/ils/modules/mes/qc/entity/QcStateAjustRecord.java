package com.ils.modules.mes.qc.entity;

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
 * @Description: 质量调整记录
 * @Author: Tian
 * @Date: 2021-03-04
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_state_ajust_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_qc_state_ajust_record对象", description = "质量调整记录")
public class QcStateAjustRecord extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 物料单元id
     */
    @Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 2)
    @TableField("item_cell_id")
    private String itemCellId;
    /**
     * 标签码
     */
    @Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 3)
    @TableField("qrcode")
    private String qrcode;
    /**
     * 入库批号/生产批号
     */
    @Excel(name = "入库批号/生产批号", width = 15)
    @ApiModelProperty(value = "入库批号/生产批号", position = 4)
    @TableField("batch")
    private String batch;
    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 5)
    @TableField("item_id")
    private String itemId;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 6)
    @TableField("item_code")
    private String itemCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 7)
    @TableField("item_name")
    private String itemName;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格", position = 8)
    @TableField("spec")
    private String spec;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 9)
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 单位，以生成时进入表中时第一次业务单位存储。
     */
    @Excel(name = "单位，以生成时进入表中时第一次业务单位存储。", width = 15)
    @ApiModelProperty(value = "单位，以生成时进入表中时第一次业务单位存储。", position = 10)
    @TableField("unit_id")
    private String unitId;
    /**
     * 单位名称，以生成时进入表中时第一次业务单位存储。
     */
    @Excel(name = "单位名称，以生成时进入表中时第一次业务单位存储。", width = 15)
    @ApiModelProperty(value = "单位名称，以生成时进入表中时第一次业务单位存储。", position = 11)
    @TableField("unit_name")
    private String unitName;
    /**
     * 仓位id
     */
    @Excel(name = "仓位id", width = 15)
    @ApiModelProperty(value = "仓位id", position = 12)
    @TableField("storage_id")
    private String storageId;
    /**
     * 仓位编码
     */
    @Excel(name = "仓位编码", width = 15)
    @ApiModelProperty(value = "仓位编码", position = 13)
    @TableField("storage_code")
    private String storageCode;
    /**
     * 仓位名称
     */
    @Excel(name = "仓位名称", width = 15)
    @ApiModelProperty(value = "仓位名称", position = 14)
    @TableField("storage_name")
    private String storageName;
    /**
     * 位置一级仓位编码
     */
    @Excel(name = "位置一级仓位编码", width = 15)
    @ApiModelProperty(value = "位置一级仓位编码", position = 15)
    @TableField("area_code")
    private String areaCode;
    /**
     * 位置一级仓位名称
     */
    @Excel(name = "位置一级仓位名称", width = 15)
    @ApiModelProperty(value = "位置一级仓位名称", position = 16)
    @TableField("area_name")
    private String areaName;
    /**
     * 位置仓库编码
     */
    @Excel(name = "位置仓库编码", width = 15)
    @ApiModelProperty(value = "位置仓库编码", position = 17)
    @TableField("house_code")
    private String houseCode;
    /**
     * 位置仓库名称
     */
    @Excel(name = "位置仓库名称", width = 15)
    @ApiModelProperty(value = "位置仓库名称", position = 18)
    @TableField("house_name")
    private String houseName;
    /**
     * 调整前质量状态
     */
    @Excel(name = "调整前质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "调整前质量状态", position = 19)
    @TableField("qc_status_before")
    @Dict(dicCode = "mesQcStatus")
    private String qcStatusBefore;
    /**
     * 调整后质量状态
     */
    @Excel(name = "调整后质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "调整后质量状态", position = 20)
    @TableField("qc_status_after")
    @Dict(dicCode = "mesQcStatus")
    private String qcStatusAfter;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 21)
    @TableField("note")
    private String note;
}
