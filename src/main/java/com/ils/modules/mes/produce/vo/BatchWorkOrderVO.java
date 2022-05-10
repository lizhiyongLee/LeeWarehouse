package com.ils.modules.mes.produce.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 批量新增工单VO
 * @author: fengyi
 * @date: 2021年1月26日 下午1:46:46
 */
@Setter
@Getter
@ToString(callSuper = true)
public class BatchWorkOrderVO extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /** 工单号 */
    @ApiModelProperty(value = "工单号", position = 2)
    private String orderNo;
    /** 1，2，3，4，5；分别代表第1，2，3，4，5层；创建的工单默认为1，即父工单。 */
    @ApiModelProperty(value = "1，2，3，4，5；分别代表第1，2，3，4，5层；创建的工单默认为1，即父工单。", position = 3)
    private Integer orderLayer;
    /** 1、面向库存；2、面向订单 */
    @ApiModelProperty(value = "1、面向库存；2、面向订单", position = 4)
    @Dict(dicCode = "mesProductType")
    private String productType;
    /** 销售订单ID */
    @ApiModelProperty(value = "销售订单号ID", position = 5)
    private String saleOrderId;
    /** 销售订单号 */
    @ApiModelProperty(value = "销售订单号号", position = 5)
    private String saleOrderNo;
    /** 物料行id */
    private String saleOrderLineId;
    /** 物料行号 */
    private Integer lineNumber;
    /** 1、正常工单2、返工工单3、委外工单、4、打样工单 */
    @ApiModelProperty(value = "1、正常工单2、返工工单3、委外工单、4、打样工单", position = 6)
    @Dict(dicCode = "mesOrderType")
    private String orderType;
    /** 1、手动生成；2、按规则生成； */
    @Excel(name = "1、手动生成；2、按规则生成；", width = 15)
    @TableField("batch_type")
    @ApiModelProperty(value = "1、手动生成；2、按规则生成；", position = 7)
    @Dict(dicCode = "mesBatchType")
    private String batchType;
    /** 生产批号 */
    @Excel(name = "生产批号", width = 15)
    @TableField("batch_no")
    @ApiModelProperty(value = "生产批号", position = 8)
    private String batchNo;
    /** 产品ID */
    @Excel(name = "产品ID", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "产品ID", position = 9)
    private String itemId;
    /** 产品编码 */
    @Excel(name = "产品编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "产品编码", position = 10)
    private String itemCode;
    /** 产品名称 */
    @Excel(name = "产品名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "产品名称", position = 11)
    private String itemName;
    /** 产品规格 */
    @Excel(name = "产品规格", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "产品规格", position = 12)
    private String spec;
    /** 计划数量 */
    @ApiModelProperty(value = "计划数量", position = 13)
    private BigDecimal planQty;

    /** 单位 */
    @Excel(name = "单位", width = 15)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unit;
    /** 等级 */
    @ApiModelProperty(value = "等级", position = 16)
    private Integer level;
    /** 以jason形式存储计划员对应的 id 及姓名； */
    @ApiModelProperty(value = "以jason形式存储计划员对应的 id 及姓名；", position = 17)
    private String pmc;
    /** 以jason形式存储生产主管对应的 id 及姓名； */
    @ApiModelProperty(value = "以jason形式存储生产主管对应的 id 及姓名；", position = 18)
    private String director;
    /** 计划开始时间 */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("plan_start_time")
    @ApiModelProperty(value = "计划开始时间", position = 19)
    private Date planStartTime;
    /** 计划结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("plan_end_time")
    @ApiModelProperty(value = "计划结束时间", position = 20)
    private Date planEndTime;
    /** 实际开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("real_start_time")
    @ApiModelProperty(value = "实际开始时间", position = 21)
    private Date realStartTime;
    /** 实际结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际结束时间", position = 22)
    private Date realEndTime;
    /** 1、工艺路线；2、工艺路线+物料清单；3、产品BOM */
    @ApiModelProperty(value = "1、工艺路线；2、工艺路线+物料清单；3、产品BOM", position = 23)
    @Dict(dicCode = "mesWorkflowType")
    private String workflowType;
    /** 引用的物料清单id */
    @ApiModelProperty(value = "引用的物料清单id", position = 24)
    private String bomId;
    /** 工艺路线id */
    @ApiModelProperty(value = "工艺路线id", position = 25)
    private String routeId;
    /** 产品bomid */
    @ApiModelProperty(value = "产品bomid", position = 26)
    private String productId;
    /** 备注 */
    @ApiModelProperty(value = "备注", position = 31)
    private String note;
}
