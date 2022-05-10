package com.ils.modules.mes.material.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 收货单行
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_receipt_line")
@ApiModel(value="mes_item_receipt_line对象", description="收货单行")
public class ItemReceiptLine extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**入库单表头id*/
    @TableField("head_id")
    @ApiModelProperty(value = "入库单表头id", position = 2)
	private String headId;
	/**物料id*/
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 3)
	private String itemId;
	/**物料编码*/
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 4)
	private String itemCode;
	/**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 5)
	private String itemName;
	/**计划数量*/
    @Excel(name = "计划数量", width = 15)
    @TableField("plan_qty")
    @ApiModelProperty(value = "计划数量", position = 6)
	private BigDecimal planQty;
	/**收货数*/
    @Excel(name = "收货数", width = 15)
    @TableField("complete_qty")
    @ApiModelProperty(value = "收货数", position = 7)
	private BigDecimal completeQty;
	/**退库数*/
    @Excel(name = "退库数", width = 15)
    @TableField("refund_qty")
    @ApiModelProperty(value = "退库数", position = 8)
	private BigDecimal refundQty;
	/**收货进度*/
    @Excel(name = "收货进度", width = 15)
    @TableField("complete_ratio")
    @ApiModelProperty(value = "收货进度", position = 9)
	private BigDecimal completeRatio;
	/**单位*/
    @Excel(name = "单位", width = 15)
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 10)
	private String unit;
	/**批号*/
    @Excel(name = "批号", width = 15)
    @TableField("batch")
    @ApiModelProperty(value = "批号", position = 11)
	private String batch;
	/**收货区域*/
    @Excel(name = "收货区域", width = 15)
    @TableField("storage_area")
    @ApiModelProperty(value = "收货区域", position = 12)
	private String storageArea;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("produce_date")
    @ApiModelProperty(value = "生产日期", position = 13)
	private Date produceDate;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("valid_date")
    @ApiModelProperty(value = "有效期", position = 14)
	private Date validDate;
	/**供应商id*/
    @Excel(name = "供应商id", width = 15)
    @TableField("supplier_id")
    @ApiModelProperty(value = "供应商id", position = 15)
	private String supplierId;
	/**供应商编码*/
    @Excel(name = "供应商编码", width = 15)
    @TableField("supplier_code")
    @ApiModelProperty(value = "供应商编码", position = 16)
	private String supplierCode;
	/**供应商名称*/
    @Excel(name = "供应商名称", width = 15)
    @TableField("supplier_name")
    @ApiModelProperty(value = "供应商名称", position = 17)
	private String supplierName;
	/**供应商批次*/
    @Excel(name = "供应商批次", width = 15)
    @TableField("supplier_batch")
    @ApiModelProperty(value = "供应商批次", position = 18)
	private String supplierBatch;
}
