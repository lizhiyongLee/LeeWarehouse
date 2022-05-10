package com.ils.modules.mes.produce.entity;

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

/**
 * @Description: 销售订单
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_sale_order")
@ApiModel(value="mes_sale_order对象", description="销售订单")
public class SaleOrder extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**销售订单号*/
    @Excel(name = "销售订单号", width = 15)
    @TableField("sale_order_no")
    @ApiModelProperty(value = "销售订单号", position = 2)
	private String saleOrderNo;
	/**外部单据号*/
    @Excel(name = "外部单据号", width = 15)
    @TableField("external_sale_order_no")
    @ApiModelProperty(value = "外部单据号", position = 3)
	private String externalSaleOrderNo;
	/**客户id*/
    @Excel(name = "客户id", width = 15, dictTable = "mes_customer", dicCode = "id", dicText = "customer_name")
    @TableField("customer_id")
    @ApiModelProperty(value = "客户id", position = 4)
    @Dict(dictTable = "mes_customer", dicCode = "id", dicText = "customer_name")
    private String customerId;
	/**客户名称*/
    @Excel(name = "客户名称", width = 15)
    @TableField("customer_name")
    @ApiModelProperty(value = "客户名称", position = 5)
    private String customerName;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 6)
	private String note;
	/**1、新建；2、完成；3、结束*/
    @Excel(name = "1、新建；2、完成；3、结束", width = 15, dicCode = "mesSaleOrderStatus")
    @TableField("status")
    @ApiModelProperty(value = "1、新建；2、完成；3、结束", position = 7)
    @Dict(dicCode = "mesSaleOrderStatus")
	private String status;
	/**审批流id，关联审批流表。*/
    @Excel(name = "审批流id，关联审批流表。", width = 15)
    @TableField("flow_id")
    @ApiModelProperty(value = "审批流id，关联审批流表。", position = 8)
	private String flowId;
	/**审批状态*/
    @Excel(name = "审批状态", width = 15, dicCode = "mesAuditStatus")
    @TableField("audit_status")
    @ApiModelProperty(value = "审批状态", position = 9)
    @Dict(dicCode = "mesAuditStatus")
	private String auditStatus;
}
