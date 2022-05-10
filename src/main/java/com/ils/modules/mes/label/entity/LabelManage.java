package com.ils.modules.mes.label.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:52
 */
@Data
@TableName("mes_label_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_label_manage对象", description = "标签管理")
public class LabelManage extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 标签来源
     */
    @Excel(name = "标签来源,1、手工创建；2、导入；3、其他。", width = 15, dicCode = "mesLabelFrom")
    @TableField("label_from")
    @ApiModelProperty(value = "标签来源", position = 2)
    @Dict(dicCode = "mesLabelFrom")
    private String labelFrom;
    /**
     * 预留为对应某个单据编号
     */
    @Excel(name = "预留为对应某个单据编号", width = 15)
    @TableField("receipt_code")
    @ApiModelProperty(value = "预留为对应某个单据编号", position = 3)
    private String receiptCode;
    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 4)
    private String itemId;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 5)
    private String itemCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 6)
    private String itemName;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "规格", position = 7)
    private String spec;
    /**
     * 如果未使用规则则使用系统默认规则，选了规则即使用对应规则。
     */
    @Excel(name = "如果未使用规则则使用系统默认规则，选了规则即使用对应规则。", width = 15, dictTable = "ils_code_generator", dicCode = "generator_code", dicText = "generator_name")
    @TableField("label_rule")
    @ApiModelProperty(value = "如果未使用规则则使用系统默认规则，选了规则即使用对应规则。", position = 8)
    @Dict(dictTable = "ils_code_generator", dicCode = "generator_code", dicText = "generator_name")
    private String labelRule;
    /**
     * 生成张数
     */
    @Excel(name = "生成张数", width = 15)
    @TableField("label_count")
    @ApiModelProperty(value = "生成张数", position = 9)
    private Integer labelCount;
    /**
     * 批号
     */
    @Excel(name = "批号", width = 15)
    @TableField("batch")
    @ApiModelProperty(value = "批号", position = 10)
    private String batch;
    /**
     * 供应商id
     */
    @Excel(name = "供应商id", width = 15)
    @TableField("supplier_id")
    @ApiModelProperty(value = "供应商id", position = 11)
    private String supplierId;
    /**
     * 供应商编码
     */
    @Excel(name = "供应商编码", width = 15)
    @TableField("supplier_code")
    @ApiModelProperty(value = "供应商编码", position = 12)
    private String supplierCode;
    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", width = 15)
    @TableField("supplier_name")
    @ApiModelProperty(value = "供应商名称", position = 13)
    private String supplierName;
    /**
     * 供应商批次
     */
    @Excel(name = "供应商批次", width = 15)
    @TableField("supplier_batch")
    @ApiModelProperty(value = "供应商批次", position = 14)
    private String supplierBatch;
    /**
     * 仓位编码
     */
    @Excel(name = "仓位编码", width = 15)
    @TableField("storage_code")
    @ApiModelProperty(value = "仓位编码", position = 15)
    private String storageCode;
    /**
     * 仓位名称
     */
    @Excel(name = "仓位名称", width = 15)
    @TableField("storage_name")
    @ApiModelProperty(value = "仓位名称", position = 16)
    private String storageName;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 17)
    private String note;
    /**
     * 状态：1、新建；2、已打印；3、已使用；4、完结。
     */
    @Excel(name = "状态：1、新建；2、已打印；3、已使用；4、完结。", width = 15, dicCode = "mesLabelReceiptStatus")
    @TableField("receipt_status")
    @ApiModelProperty(value = "状态：1、新建；2、已打印；3、已使用；4、完结。", position = 18)
    @Dict(dicCode = "mesLabelReceiptStatus")
    private String receiptStatus;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 19)
    private BigDecimal qty;
    /**
     * 单位id
     */
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit_id")
    @ApiModelProperty(value = "单位", position = 20)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unitId;
    /**
     * 生产日期
     */
    @Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("produce_date")
    @ApiModelProperty(value = "生产日期", position = 21)
    private Date produceDate;
    /**
     * 有效期
     */
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("valid_date")
    @ApiModelProperty(value = "有效期", position = 22)
    private Date validDate;
}
