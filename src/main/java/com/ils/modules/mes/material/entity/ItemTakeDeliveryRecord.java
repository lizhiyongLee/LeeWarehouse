package com.ils.modules.mes.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
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
 * @Description: 收货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@TableName("mes_item_take_delivery_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_item_take_delivery_record对象", description="收货记录")
public class ItemTakeDeliveryRecord extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**收货类型：1、按采购订单收；2、普通收货；3、按收货单收货；3、退货接收*/
	@Excel(name = "收货类型：1、按采购订单收；2、普通收货；3、按收货单收货；3、退货接收", width = 15, dicCode = "mesReceiveType")
    @ApiModelProperty(value = "收货类型：1、按采购订单收；2、普通收货；3、按收货单收货；3、退货接收", position = 2)
    @TableField("receive_type")
	@Dict(dicCode = "mesReceiveType")
	private String receiveType;
	/**物料单元id*/
	@Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 3)
    @TableField("item_cell_id")
	private String itemCellId;
	/**标签码*/
	@Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 4)
    @TableField("qrcode")
	@KeyWord
	private String qrcode;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号", position = 5)
    @TableField("batch")
	@KeyWord
	private String batch;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 6)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 7)
    @TableField("item_code")
	@KeyWord
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 8)
    @TableField("item_name")
	@KeyWord
	private String itemName;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 9)
    @TableField("qty")
	private BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 10)
    @TableField("unit_id")
	private String unitId;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称", position = 11)
    @TableField("unit_name")
	private String unitName;
	/**仓位编码*/
	@Excel(name = "仓位编码", width = 15)
    @ApiModelProperty(value = "仓位编码", position = 12)
    @TableField("storage_code")
	@KeyWord
	private String storageCode;
	/**仓位名称*/
	@Excel(name = "仓位名称", width = 15)
    @ApiModelProperty(value = "仓位名称", position = 13)
    @TableField("storage_name")
	@KeyWord
	private String storageName;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期", position = 14)
    @TableField("produce_date")
	private Date produceDate;
	/**有效期*/
	@Excel(name = "有效期", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期", position = 15)
    @TableField("valid_date")
	private Date validDate;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
    @ApiModelProperty(value = "供应商id", position = 16)
    @TableField("supplier_id")
	private String supplierId;
	/**供应商编码*/
	@Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "供应商编码", position = 17)
    @TableField("supplier_code")
	private String supplierCode;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称", position = 18)
    @TableField("supplier_name")
	@KeyWord
	private String supplierName;
	/**供应商批次*/
	@Excel(name = "供应商批次", width = 15)
    @ApiModelProperty(value = "供应商批次", position = 19)
    @TableField("supplier_batch")
	private String supplierBatch;
	/**质量状态*/
	@Excel(name = "质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "质量状态", position = 20)
    @TableField("quality_status")
	@Dict(dicCode = "mesQcStatus")
	private String qualityStatus;
	/**收货单据*/
	@Excel(name = "收货单据", width = 15)
    @ApiModelProperty(value = "收货单据", position = 21)
    @TableField("reciept_id")
	private String recieptId;
	/**收货单据编码*/
	@Excel(name = "收货单据编码", width = 15)
    @ApiModelProperty(value = "收货单据编码", position = 22)
    @TableField("reciept_code")
	private String recieptCode;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 23)
    @TableField("attach")
	private String attach;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 24)
    @TableField("note")
	private String note;
}
