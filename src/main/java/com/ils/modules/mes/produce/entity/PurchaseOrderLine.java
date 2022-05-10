package com.ils.modules.mes.produce.entity;

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

import java.util.Date;


/**
 * @Description: 采购清单物料行
 * @Author: Tian
 * @Date:   2021-01-28
 * @Version: V1.0
 */
@Data
@TableName("mes_purchase_order_line")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_purchase_order_line对象", description="采购清单物料行")
public class PurchaseOrderLine extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**入库单表头id*/
	@Excel(name = "入库单表头id", width = 15)
    @ApiModelProperty(value = "入库单表头id", position = 2)
    @TableField("purchase_order_id")
	private String purchaseOrderId;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 3)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 4)
    @TableField("item_code")
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 5)
    @TableField("item_name")
	private String itemName;
	/**规格描述*/
	@Excel(name = "规格描述", width = 15)
    @ApiModelProperty(value = "规格描述", position = 6)
    @TableField("spec")
	private String spec;
	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
    @ApiModelProperty(value = "计划数量", position = 7)
    @TableField("plan_qty")
	private String planQty;
	/**已执行数量*/
	@Excel(name = "已执行数量", width = 15)
    @ApiModelProperty(value = "已执行数量", position = 8)
    @TableField("complete_qty")
	private String completeQty;
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "mes_unit", dicText = "unit_name", dicCode = "id")
    @ApiModelProperty(value = "单位", position = 9)
    @TableField("unit")
	@Dict(dictTable = "mes_unit",dicText = "unit_name",dicCode = "id")
	private String unit;
	/**需求时间*/
	@Excel(name = "需求时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "需求时间", position = 10)
    @TableField("required_time")
	private Date requiredTime;
	/**销售单id*/
	@Excel(name = "销售单id", width = 15)
    @ApiModelProperty(value = "销售单id", position = 11)
    @TableField("sales_order_id")
	private String salesOrderId;
	/**销售单编码*/
	@Excel(name = "销售单编码", width = 15)
    @ApiModelProperty(value = "销售单编码", position = 12)
    @TableField("sales_order_no")
	private String salesOrderNo;
	/**工单id*/
	@Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 13)
    @TableField("work_order_id")
	private String workOrderId;
	/**工单号*/
	@Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 14)
    @TableField("work_order_no")
	private String workOrderNo;
	/**1、新建；2、已申请；3、取消；4、结束；*/
	@Excel(name = "1、新建；2、已申请；3、取消；4、结束；", width = 15, dicCode = "mesPurchaseOrderStatus")
    @ApiModelProperty(value = "1、新建；2、已申请；3、取消；4、结束；", position = 15)
    @TableField("status")
	@Dict(dicCode = "mesPurchaseOrderStatus")
	private String status;
}
