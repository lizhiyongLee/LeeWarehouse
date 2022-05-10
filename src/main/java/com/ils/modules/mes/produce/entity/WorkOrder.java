package com.ils.modules.mes.produce.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.ils.common.aspect.annotation.KeyWord;
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
 * @Description: 工单
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_order")
@ApiModel(value = "mes_work_order对象", description = "工单")
public class WorkOrder extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 15)
    @TableField("order_no")
    @ApiModelProperty(value = "工单号", position = 2)
    @KeyWord
    private String orderNo;

    @TableField("father_order_id")
    @ApiModelProperty(value = "父工单id", position = 3)
    private String fatherOrderId;

    @Excel(name = "父工单号", width = 15)
    @TableField("father_order_no")
    @ApiModelProperty(value = "父工单号", position = 4)
    private String fatherOrderNo;

    @TableField("seq")
    @ApiModelProperty(value = "对应父工单工序序号", position = 5)
    private Integer seq;

    @TableField("process_id")
    @ApiModelProperty(value = "对应父工单工序id", position = 6)
    private String processId;

    @TableField("process_code")
    @ApiModelProperty(value = "对应父工单工序编码", position = 7)
    private String processCode;

    @TableField("process_name")
    @ApiModelProperty(value = "对应父工单工序名称", position = 8)
    private String processName;

    @Excel(name = "外部单据号", width = 15)
    @TableField("external_order_no")
    @ApiModelProperty(value = "外部单据号", position = 9)
    private String externalOrderNo;

    /**
     * 1，2，3，4，5；分别代表第1，2，3，4，5层；创建的工单默认为1，即父工单。
     */
    @Excel(name = "1，2，3，4，5；分别代表第1，2，3，4，5层；创建的工单默认为1，即父工单。", width = 15)
    @TableField("order_layer")
    @ApiModelProperty(value = "1，2，3，4，5；分别代表第1，2，3，4，5层；创建的工单默认为1，即父工单。", position = 10)
    private Integer orderLayer;

    /**
     * 1、面向库存；2、面向订单
     */
    @Excel(name = "1、面向库存；2、面向订单", width = 15, dicCode = "mesProductType")
    @TableField("product_type")
    @ApiModelProperty(value = "1、面向库存；2、面向订单", position = 11)
    @Dict(dicCode = "mesProductType")
    private String productType;

    /**
     * 销售订单号
     */
    @Excel(name = "销售订单号", width = 15)
    @TableField("sale_order_id")
    @ApiModelProperty(value = "销售订单号", position = 12)
    private String saleOrderId;

    /**
     * 1、正常工单2、返工工单3、委外工单、4、打样工单
     */
    @Excel(name = "1、正常工单2、返工工单3、委外工单、4、打样工单", width = 15, dicCode = "mesOrderType")
    @TableField("order_type")
    @ApiModelProperty(value = "1、正常工单2、返工工单3、委外工单、4、打样工单", position = 13)
    @Dict(dicCode = "mesOrderType")
    private String orderType;

    /**
     * 1、手动生成；2、按规则生成；
     */
    @Excel(name = "1、手动生成；2、按规则生成；", width = 15, dicCode = "mesBatchType")
    @TableField("batch_type")
    @ApiModelProperty(value = "1、手动生成；2、按规则生成；", position = 14)
    @Dict(dicCode = "mesBatchType")
    private String batchType;

    /**
     * 生产批号
     */
    @Excel(name = "生产批号", width = 15)
    @TableField("batch_no")
    @ApiModelProperty(value = "生产批号", position = 15)
    private String batchNo;

    /**
     * 产品ID
     */
    @Excel(name = "产品ID", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "产品ID", position = 16)
    private String itemId;

    /**
     * 产品编码
     */
    @Excel(name = "产品编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "产品编码", position = 17)
    private String itemCode;
    /**
     * 产品名称
     */
    @Excel(name = "产品名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "产品名称", position = 18)
    private String itemName;
    /**
     * 产品规格
     */
    @Excel(name = "产品规格", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "产品规格", position = 19)
    private String spec;
    /**
     * 计划数量
     */
    @Excel(name = "计划数量", width = 15)
    @TableField("plan_qty")
    @ApiModelProperty(value = "计划数量", position = 20)
    private BigDecimal planQty;
    /**
     * 完成数量
     */
    @Excel(name = "完成数量", width = 15)
    @TableField("completed_qty")
    @ApiModelProperty(value = "完成数量", position = 21)
    private BigDecimal completedQty;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 22)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unit;
    /**
     * 等级
     */
    @Excel(name = "等级", width = 15)
    @TableField("level")
    @ApiModelProperty(value = "等级", position = 23)
    private Integer level;
    /**
     * 以jason形式存储计划员对应的 id 及姓名；
     */
    @Excel(name = "计划员", width = 15, dictTable = "sys_user", dicCode = "id", dicText = "username", multiReplace = true)
    @TableField("pmc")
    @ApiModelProperty(value = "以jason形式存储计划员对应的 id 及姓名；", position = 24)
    private String pmc;
    /**
     * 以jason形式存储生产主管对应的 id 及姓名；
     */
    @Excel(name = "生产主管", width = 15, dictTable = "sys_user", dicCode = "id", dicText = "username", multiReplace = true)
    @TableField("director")
    @ApiModelProperty(value = "以jason形式存储生产主管对应的 id 及姓名；", position = 25)
    private String director;
    /**
     * 计划开始时间
     */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("plan_start_time")
    @ApiModelProperty(value = "计划开始时间", position = 26)
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    @Excel(name = "计划结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("plan_end_time")
    @ApiModelProperty(value = "计划结束时间", position = 27)
    private Date planEndTime;
    /**
     * 实际开始时间
     */
    @Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("real_start_time")
    @ApiModelProperty(value = "实际开始时间", position = 28)
    private Date realStartTime;
    /**
     * 实际结束时间
     */
    @Excel(name = "实际结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("real_end_time")
    @ApiModelProperty(value = "实际结束时间", position = 29)
    private Date realEndTime;
    /**
     * 1、工艺路线；2、工艺路线+物料清单；3、产品BOM
     */
    @Excel(name = "1、工艺路线；2、工艺路线+物料清单；3、产品BOM", width = 15, dicCode = "mesWorkflowType")
    @TableField("workflow_type")
    @ApiModelProperty(value = "1、工艺路线；2、工艺路线+物料清单；3、产品BOM", position = 30)
    @Dict(dicCode = "mesWorkflowType")
    private String workflowType;
    /**
     * 引用的物料清单id
     */
    @Excel(name = "引用的物料清单id", width = 15, dictTable = "mes_item_bom", dicCode = "id", dicText = "version")
    @TableField("bom_id")
    @ApiModelProperty(value = "引用的物料清单id", position = 31)
    @Dict(dictTable = "mes_item_bom", dicCode = "id", dicText = "version")
    private String bomId;
    /**
     * 工艺路线id
     */
    @Excel(name = "工艺路线id", width = 15, dictTable = "mes_route", dicCode = "id", dicText = "route_name")
    @TableField("route_id")
    @ApiModelProperty(value = "工艺路线id", position = 32)
    @Dict(dictTable = "mes_route", dicCode = "id", dicText = "route_name")
    private String routeId;
    /**
     * 产品bomid
     */
    @Excel(name = "产品bomid", width = 15, dictTable = "mes_product", dicCode = "id", dicText = "version")
    @TableField("product_id")
    @ApiModelProperty(value = "产品bomid", position = 33)
    @Dict(dictTable = "mes_product", dicCode = "id", dicText = "version")
    private String productId;
    /**
     * 审批流id，关联审批流表。
     */
    @Excel(name = "审批流id，关联审批流表。", width = 15)
    @TableField("flow_id")
    @ApiModelProperty(value = "审批流id，关联审批流表。", position = 34)
    private String flowId;
    /**
     * 审批状态
     */
    @Excel(name = "审批状态", width = 15, dicCode = "mesAuditStatus")
    @TableField("audit_status")
    @ApiModelProperty(value = "审批状态", position = 35)
    @Dict(dicCode = "mesAuditStatus")
    private String auditStatus;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @TableField("attach")
    @ApiModelProperty(value = "附件", position = 36)
    private String attach;
    /**
     * 1、计划2、已排程3、已下发4、开工、5、完工6、取消
     */
    @Excel(name = "1、计划2、已排程3、已下发4、开工、5、完工6、取消", width = 15, dicCode = "mesWorkOrderStatus")
    @TableField("status")
    @ApiModelProperty(value = "1、计划2、已排程3、已下发4、开工、5、完工6、取消", position = 37)
    @Dict(dicCode = "mesWorkOrderStatus")
    private String status;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 38)
    private String note;
}
