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
import java.util.Date;


/**
 * @Description: 发货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@TableName("mes_item_delivery_goods_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_item_delivery_goods_record对象", description="发货记录")
public class ItemDeliveryGoodsRecord extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**物料单元id*/
	@Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 2)
    @TableField("item_cell_id")
	private String itemCellId;
	/**发货类型：1、按销售订单发货；2、普通发货；3、退料发货*/
	@Excel(name = "发货类型：1、按销售订单发货；2、普通发货；3、退料发货", width = 15, dicCode = "mesDeliverType")
    @ApiModelProperty(value = "发货类型：1、按销售订单发货；2、普通发货；3、退料发货", position = 3)
    @TableField("deliver_type")
	@Dict(dicCode = "mesDeliverType")
	private String deliverType;
	/**标签码*/
	@Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 4)
    @TableField("qrcode")
	@KeyWord
	private String qrcode;
	/**父标签码*/
	@Excel(name = "父标签码", width = 15)
    @ApiModelProperty(value = "父标签码", position = 5)
    @TableField("father_qrcode")
	private String fatherQrcode;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号", position = 6)
    @TableField("batch")
	@KeyWord
	private String batch;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 7)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 8)
    @TableField("item_code")
	@KeyWord
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 9)
    @TableField("item_name")
	@KeyWord
	private String itemName;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 10)
    @TableField("qty")
	private BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 11)
    @TableField("unit_id")
	private String unitId;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称", position = 12)
    @TableField("unit_name")
	private String unitName;
	/**等级：1,一等品；2、二等品；3、三等品*/
	@Excel(name = "等级：1,一等品；2、二等品；3、三等品", width = 15, dicCode = "mesItemLevel")
    @ApiModelProperty(value = "等级：1,一等品；2、二等品；3、三等品", position = 13)
    @TableField("item_level")
	@Dict(dicCode = "mesItemLevel")
	private String itemLevel;
	/**工单号*/
	@Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 14)
    @TableField("work_order_code")
	@KeyWord
	private String workOrderCode;
	/**质量状态*/
	@Excel(name = "质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "质量状态", position = 15)
    @TableField("quality_status")
	@Dict(dicCode = "mesQcStatus")
	private String qualityStatus;
	/**发货仓位编码*/
	@Excel(name = "发货仓位编码", width = 15)
    @ApiModelProperty(value = "发货仓位编码", position = 16)
    @TableField("storage_code")
	private String storageCode;
	/**发货仓位名称*/
	@Excel(name = "发货仓位名称", width = 15)
    @ApiModelProperty(value = "发货仓位名称", position = 17)
    @TableField("storage_name")
	private String storageName;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15)
    @ApiModelProperty(value = "生产日期", position = 18)
    @TableField("produce_date")
	private Date produceDate;
	/**有效期*/
	@Excel(name = "有效期", width = 15)
    @ApiModelProperty(value = "有效期", position = 19)
    @TableField("valid_date")
	private Date validDate;
	/**发货单据*/
	@Excel(name = "发货单据", width = 15)
    @ApiModelProperty(value = "发货单据", position = 20)
    @TableField("deliver_bill_code")
	private String deliverBillCode;
	/**客户id*/
	@Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id", position = 21)
    @TableField("customer_id")
	private String customerId;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称", position = 22)
    @TableField("customer_name")
	private String customerName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 23)
    @TableField("note")
	private String note;
}
