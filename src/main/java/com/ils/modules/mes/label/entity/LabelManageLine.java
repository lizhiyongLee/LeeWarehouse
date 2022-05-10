package com.ils.modules.mes.label.entity;

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
import java.util.Date;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:53
 */
@Data
@TableName("mes_label_manage_line")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_label_manage_line对象", description = "标签管理行")
public class LabelManageLine extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 标签管理id
     */
    @Excel(name = "标签管理id", width = 15)
    @TableField("label_manage_id")
    @ApiModelProperty(value = "标签管理id", position = 2)
    private String labelManageId;
    /**
     * 标签码
     */
    @Excel(name = "标签码", width = 15)
    @TableField("qrcode")
    @ApiModelProperty(value = "标签码", position = 3)
    private String qrcode;
    /**
     * 批号
     */
    @Excel(name = "批号", width = 15)
    @TableField("batch")
    @ApiModelProperty(value = "批号", position = 4)
    private String batch;
    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 5)
    private String itemId;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 6)
    private String itemCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 7)
    private String itemName;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 8)
    private BigDecimal qty;
    /**
     * 单位id
     */
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit_id")
    @ApiModelProperty(value = "单位", position = 9)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unitId;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称", width = 15)
    @TableField("unit_name")
    @ApiModelProperty(value = "单位名称", position = 10)
    private String unitName;
    /**
     * 仓位编码
     */
    @Excel(name = "仓位编码", width = 15)
    @TableField("storage_code")
    @ApiModelProperty(value = "仓位编码", position = 11)
    private String storageCode;
    /**
     * 仓位名称
     */
    @Excel(name = "仓位名称", width = 15)
    @TableField("storage_name")
    @ApiModelProperty(value = "仓位名称", position = 12)
    private String storageName;
    /**
     * 生产日期
     */
    @Excel(name = "生产日期", width = 15)
    @TableField("produce_date")
    @ApiModelProperty(value = "生产日期", position = 13)
    private Date produceDate;
    /**
     * 有效期
     */
    @Excel(name = "有效期", width = 15)
    @TableField("valid_date")
    @ApiModelProperty(value = "有效期", position = 14)
    private Date validDate;
    /**
     * 供应商id
     */
    @Excel(name = "供应商id", width = 15)
    @TableField("supplier_id")
    @ApiModelProperty(value = "供应商id", position = 15)
    private String supplierId;
    /**
     * 供应商编码
     */
    @Excel(name = "供应商编码", width = 15)
    @TableField("supplier_code")
    @ApiModelProperty(value = "供应商编码", position = 16)
    private String supplierCode;
    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", width = 15)
    @TableField("supplier_name")
    @ApiModelProperty(value = "供应商名称", position = 17)
    private String supplierName;
    /**
     * 供应商批次
     */
    @Excel(name = "供应商批次", width = 15)
    @TableField("supplier_batch")
    @ApiModelProperty(value = "供应商批次", position = 18)
    private String supplierBatch;
    /**
     * 打印状态
     */
    @Excel(name = "打印状态：1、已打印；0、未打印。", width = 15, dicCode = "mesLabelPrintStatus")
    @TableField("print_status")
    @ApiModelProperty(value = "打印状态", position = 19)
    @Dict(dicCode = "mesLabelPrintStatus")
    private String printStatus;
    /**
     * 打印次数
     */
    @Excel(name = "打印次数", width = 15)
    @TableField("print_times")
    @ApiModelProperty(value = "打印次数", position = 20)
    private Integer printTimes;
    /**
     * 使用状态：1、未使用；2、已使用；3、已作废；
     */
    @Excel(name = "使用状态：1、未使用；2、已使用；3、已作废；", width = 15, dicCode = "mesLabelLineUseStatus")
    @TableField("use_status")
    @ApiModelProperty(value = "使用状态：1、未使用；2、已使用；3、已作废；", position = 21)
    @Dict(dicCode = "mesLabelLineUseStatus")
    private String useStatus;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 22)
    private String note;

}
