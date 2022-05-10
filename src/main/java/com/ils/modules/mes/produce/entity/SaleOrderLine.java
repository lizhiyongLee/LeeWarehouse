package com.ils.modules.mes.produce.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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

/**
 * @Description: 销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_sale_order_line")
@ApiModel(value="mes_sale_order_line对象", description="销售订单物料行")
public class SaleOrderLine extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**销售订单id*/
    @TableField("sale_order_id")
    @ApiModelProperty(value = "销售订单id", position = 2)
	private String saleOrderId;
	/**销售订单*/
    @Excel(name = "销售订单", width = 15)
    @TableField("sale_order_no")
    @ApiModelProperty(value = "销售订单", position = 3)
    @KeyWord
	private String saleOrderNo;
	/**行号*/
    @Excel(name = "行号", width = 15)
    @TableField("line_number")
    @ApiModelProperty(value = "行号", position = 4)
	private Integer lineNumber;
	/**物料id*/
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 5)
	private String itemId;
	/**物料编码*/
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 6)
    @KeyWord
	private String itemCode;
	/**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 7)
	private String itemName;
	/**物料规格*/
    @Excel(name = "物料规格", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "物料规格", position = 8)
	private String spec;
	/**下单数量*/
    @Excel(name = "下单数量", width = 15)
    @TableField("sale_order_qty")
    @ApiModelProperty(value = "下单数量", position = 9)
	private BigDecimal saleOrderQty;
	/**交付数量*/
    @Excel(name = "交付数量", width = 15)
    @TableField("sale_order_complete_qty")
    @ApiModelProperty(value = "交付数量", position = 10)
	private BigDecimal saleOrderCompleteQty;
	/**returnQty*/
    @Excel(name = "returnQty", width = 15)
    @TableField("return_qty")
    @ApiModelProperty(value = "returnQty", position = 11)
	private BigDecimal returnQty;
	/**计划数量*/
    @Excel(name = "计划数量", width = 15)
    @TableField("plan_qty")
    @ApiModelProperty(value = "计划数量", position = 12)
	private BigDecimal planQty;
	/**已完成数量*/
    @Excel(name = "已完成数量", width = 15)
    @TableField("complete_qty")
    @ApiModelProperty(value = "已完成数量", position = 13)
	private BigDecimal completeQty;
	/**单位*/
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 14)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;

    /** 主单位下单数量 */
    @Excel(name = "主单位下单数量", width = 15)
    @TableField("sale_order_main_qty")
    @ApiModelProperty(value = "主单位下单数量", position = 15)
    private BigDecimal saleOrderMainQty;
    /** 主单位交付数量 */
    @Excel(name = "主单位交付数量", width = 15)
    @TableField("sale_order_complete_main_qty")
    @ApiModelProperty(value = "主单位交付数量", position = 16)
    private BigDecimal saleOrderCompleteMainQty;
    /** 主单位退货数量 */
    @Excel(name = "主单位退货数量", width = 15)
    @TableField("return_main_qty")
    @ApiModelProperty(value = "主单位退货数量", position = 17)
    private BigDecimal returnMainQty;
    /** 主单位计划数量 */
    @Excel(name = "主单位计划数量", width = 15)
    @TableField("plan_main_qty")
    @ApiModelProperty(value = "主单位计划数量", position = 18)
    private BigDecimal planMainQty;
    /** 主单位已完成数量 */
    @Excel(name = "主单位已完成数量", width = 15)
    @TableField("complete_main_qty")
    @ApiModelProperty(value = "主单位已完成数量", position = 19)
    private BigDecimal completeMainQty;
    /** 主单位 */
    @Excel(name = "主单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("main_unit")
    @ApiModelProperty(value = "主单位", position = 20)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String mainUnit;

	/**需求时间*/
	@Excel(name = "需求时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("required_date")
    @ApiModelProperty(value = "需求时间", position = 15)
	private Date requiredDate;
	/**1、新建；2、完成；3、结束*/
    @Excel(name = "1、新建；2、完成；3、结束", width = 15, dicCode = "mesSaleOrderStatus")
    @TableField("status")
    @ApiModelProperty(value = "1、新建；2、完成；3、结束", position = 16)
    @Dict(dicCode = "mesSaleOrderStatus")
	private String status;
}
