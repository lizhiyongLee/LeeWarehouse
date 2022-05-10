package com.ils.modules.mes.produce.entity;

import java.math.BigDecimal;

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
 * @Description: 工单工序任务
 * @Author: fengyi
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Data
@TableName("mes_work_process_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_process_task对象", description="工单工序任务")
public class WorkProcessTask extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**工单id*/
	@Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 2)
    @TableField("order_id")
	private String orderId;
	/**工单号*/
	@Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 3)
    @TableField("order_no")
	private String orderNo;
	/**生产批次*/
	@Excel(name = "生产批次", width = 15)
    @ApiModelProperty(value = "生产批次", position = 4)
    @TableField("batch_no")
	private String batchNo;
	/**成品物料id*/
	@Excel(name = "成品物料id", width = 15)
    @ApiModelProperty(value = "成品物料id", position = 5)
    @TableField("item_id")
	private String itemId;
	/**成品物料编码*/
	@Excel(name = "成品物料编码", width = 15)
    @ApiModelProperty(value = "成品物料编码", position = 6)
    @TableField("item_code")
	private String itemCode;
	/**成品物料名称*/
	@Excel(name = "成品物料名称", width = 15)
    @ApiModelProperty(value = "成品物料名称", position = 7)
    @TableField("item_name")
	private String itemName;
	/**成品物料规格*/
	@Excel(name = "成品物料规格", width = 15)
    @ApiModelProperty(value = "成品物料规格", position = 8)
    @TableField("spec")
	private String spec;
	/**工序id*/
	@Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 9)
    @TableField("process_id")
	private String processId;
	/**工序*/
	@Excel(name = "工序", width = 15)
    @ApiModelProperty(value = "工序", position = 10)
    @TableField("process_name")
	private String processName;
	/**工序编码*/
	@Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 11)
    @TableField("process_code")
	private String processCode;
	/**工序*/
	@Excel(name = "工序", width = 15)
    @ApiModelProperty(value = "工序", position = 12)
    @TableField("seq")
	private Integer seq;
    /** 上道工序编码 */
    @Excel(name = "上道工序编码", width = 15, dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_name")
    @TableField("prior_code")
    @ApiModelProperty(value = "上道工序编码", position = 8)
	@Dict(dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_name")
    private String priorCode;
    /** 下道工序编码 */
    @Excel(name = "下道工序编码", width = 15, dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_name")
    @TableField("next_code")
    @ApiModelProperty(value = "下道工序编码", position = 9)
	@Dict(dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_name")
    private String nextCode;
    /** 1,前续开始后续可开始2，前续结束后续可开始 */
	@Excel(name = "工艺接续方式:1,前续开始后续可开始2，前续结束后续可开始", width = 15, dicCode = "mesLinkType")
	@TableField("link_type")
	@ApiModelProperty(value = "1,前续开始后续可开始2，前续结束后续可开始", position = 10)
	@Dict(dicCode = "mesLinkType")
	private String linkType;
	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
    @ApiModelProperty(value = "计划数量", position = 13)
    @TableField("plan_qty")
	private BigDecimal planQty;
	/**排程数量，排程后回写；*/
	@Excel(name = "排程数量，排程后回写；", width = 15)
    @ApiModelProperty(value = "排程数量，排程后回写；", position = 14)
    @TableField("scheduled_qty")
	private BigDecimal scheduledQty;
	/**下发后回写；*/
	@Excel(name = "下发后回写；", width = 15)
    @ApiModelProperty(value = "下发后回写；", position = 15)
    @TableField("publish_qty")
	private BigDecimal publishQty;
	/**完成数量，报工后回写。*/
	@Excel(name = "完成数量，报工后回写。", width = 15)
    @ApiModelProperty(value = "完成数量，报工后回写。", position = 16)
    @TableField("completed_qty")
	private BigDecimal completedQty;
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @ApiModelProperty(value = "单位", position = 17)
    @TableField("unit")
	@Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;
}
