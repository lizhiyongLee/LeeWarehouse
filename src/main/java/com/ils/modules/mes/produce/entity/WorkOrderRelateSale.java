package com.ils.modules.mes.produce.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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


/**
 * @Description: 工单关联销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Data
@TableName("mes_work_order_relate_sale")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_order_relate_sale对象", description="工单关联销售订单物料行")
public class WorkOrderRelateSale extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**工单id*/
	@Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 2)
    @TableField("order_id")
	private String orderId;
	/**工单*/
	@Excel(name = "工单", width = 15)
    @ApiModelProperty(value = "工单", position = 3)
    @TableField("order_no")
	private String orderNo;
	/**销售订单id*/
	@Excel(name = "销售订单id", width = 15)
    @ApiModelProperty(value = "销售订单id", position = 4)
    @TableField("sale_order_id")
	private String saleOrderId;
	/**销售订单*/
	@Excel(name = "销售订单", width = 15)
    @ApiModelProperty(value = "销售订单", position = 5)
    @TableField("sale_order_no")
	private String saleOrderNo;
	/**物料行id*/
	@Excel(name = "物料行id", width = 15)
    @ApiModelProperty(value = "物料行id", position = 6)
    @TableField("sale_order_line_id")
	private String saleOrderLineId;
	/**行号*/
	@Excel(name = "行号", width = 15)
    @ApiModelProperty(value = "行号", position = 7)
    @TableField("line_number")
	private Integer lineNumber;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 8)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 9)
    @TableField("item_code")
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 10)
    @TableField("item_name")
	private String itemName;
	/**物料规格*/
	@Excel(name = "物料规格", width = 15)
    @ApiModelProperty(value = "物料规格", position = 11)
    @TableField("spec")
	private String spec;
	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
    @ApiModelProperty(value = "计划数量", position = 12)
    @TableField("plan_qty")
	private BigDecimal planQty;

    /** 单位 */
	@Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 14)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unit;

	/**需求时间*/
	@Excel(name = "需求时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "需求时间", position = 13)
    @TableField("required_date")
	private Date requiredDate;
}
