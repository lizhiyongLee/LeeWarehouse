package com.ils.modules.mes.execution.entity;

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
 * @Description: 执行生产任务
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
@Data
@TableName("mes_work_produce_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_produce_task对象", description="执行生产任务")
public class WorkProduceTask extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**执行生产任务编码：拷贝计划生产任务编码赋值*/
	@Excel(name = "执行生产任务编码：拷贝计划生产任务编码赋值", width = 15)
    @ApiModelProperty(value = "执行生产任务编码：拷贝计划生产任务编码赋值", position = 2)
    @TableField("task_code")
    @KeyWord
	private String taskCode;


	/**计划生产任务id*/
	@Excel(name = "计划生产任务id", width = 15)
    @ApiModelProperty(value = "计划生产任务id", position = 3)
    @TableField("plan_task_id")
	private String planTaskId;
	/**工单id*/
	@Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 4)
    @TableField("order_id")
	private String orderId;
	/**工单号*/
	@Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 5)
    @TableField("order_no")
    @KeyWord
	private String orderNo;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号", position = 6)
    @TableField("batch_no")
	private String batchNo;
	/**工序id*/
	@Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 7)
    @TableField("process_id")
	private String processId;
	/**工序编码*/
	@Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 8)
    @TableField("process_code")
	private String processCode;
	/**工序名称*/
	@Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称", position = 9)
    @TableField("process_name")
	private String processName;

    /** 工序 */
    @Excel(name = "工序", width = 15)
    @ApiModelProperty(value = "工序", position = 12)
    @TableField("seq")
    private Integer seq;

	/**成品物料id*/
	@Excel(name = "成品物料id", width = 15)
    @ApiModelProperty(value = "成品物料id", position = 10)
    @TableField("item_id")
	private String itemId;
	/**成品物料编码*/
	@Excel(name = "成品物料编码", width = 15)
    @ApiModelProperty(value = "成品物料编码", position = 11)
    @TableField("item_code")
	private String itemCode;
	/**成品物料名称*/
	@Excel(name = "成品物料名称", width = 15)
    @ApiModelProperty(value = "成品物料名称", position = 12)
    @TableField("item_name")
	private String itemName;
	/**执行工位id*/
	@Excel(name = "执行工位id", width = 15)
    @ApiModelProperty(value = "执行工位id", position = 13)
    @TableField("station_id")
	private String stationId;
	/**执行工位编码*/
	@Excel(name = "执行工位编码", width = 15)
    @ApiModelProperty(value = "执行工位编码", position = 14)
    @TableField("station_code")
	private String stationCode;
	/**执行工位名称*/
	@Excel(name = "执行工位名称", width = 15)
    @ApiModelProperty(value = "执行工位名称", position = 15)
    @TableField("station_name")
	private String stationName;
	/**排程数量*/
	@Excel(name = "排程数量", width = 15)
    @ApiModelProperty(value = "排程数量", position = 16)
    @TableField("plan_qty")
	private BigDecimal planQty;
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @ApiModelProperty(value = "单位", position = 17)
    @TableField("unit")
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;
	/**计划日期*/
	@Excel(name = "计划日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期", position = 18)
    @TableField("plan_date")
	private Date planDate;
	/**班次*/
	@Excel(name = "班次", width = 15)
    @ApiModelProperty(value = "班次", position = 19)
    @TableField("shift_id")
	private String shiftId;
	/**执行班组id*/
	@Excel(name = "执行班组id", width = 15)
    @ApiModelProperty(value = "执行班组id", position = 20)
    @TableField("team_id")
	private String teamId;
	/**计划开始时间*/
	@Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 21)
    @TableField("plan_start_time")
	private Date planStartTime;
	/**计划结束时间*/
	@Excel(name = "计划结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划结束时间", position = 22)
    @TableField("plan_end_time")
	private Date planEndTime;
	/**实际开始时间*/
	@Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际开始时间", position = 23)
    @TableField("real_start_time")
	private Date realStartTime;
	/**实际结束时间*/
	@Excel(name = "实际结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际结束时间", position = 24)
    @TableField("real_end_time")
	private Date realEndTime;
	/**总产量*/
	@Excel(name = "总产量", width = 15)
    @ApiModelProperty(value = "总产量", position = 25)
    @TableField("total_qty")
	private BigDecimal totalQty;
	/**报工数量*/
	@Excel(name = "报工数量", width = 15)
    @ApiModelProperty(value = "报工数量", position = 26)
    @TableField("good_qty")
	private BigDecimal goodQty;
	/**报工不合格数量*/
	@Excel(name = "报工不合格数量", width = 15)
    @ApiModelProperty(value = "报工不合格数量", position = 27)
    @TableField("bad_qty")
	private BigDecimal badQty;
	/**1、未开始；2、生产；3、暂停；4、结束；5、取消；*/
	@Excel(name = "1、未开始；2、生产；3、暂停；4、结束；5、取消；", width = 15,dicCode = "mesPlanTaskExeStatus")
    @ApiModelProperty(value = "1、未开始；2、生产；3、暂停；4、结束；5、取消；", position = 28)
    @TableField("exe_status")
    @Dict(dicCode = "mesPlanTaskExeStatus")
	private String exeStatus;
	/**生产报告模板ID；*/
	@Excel(name = "生产报告模板ID", width = 15, dictTable = "mes_report_template", dicCode = "id", dicText = "template_name")
	@ApiModelProperty(value = "生产报告模板ID", position = 29)
	@TableField("template_id")
	@Dict(dictTable = "mes_report_template", dicCode = "id", dicText = "template_name")
	private String templateId;
	/**是否是sop流程中的任务； 1：是，0：否*/
	@Excel(name = "是否是sop流程中的任务", width = 15, dicCode = "mesYesZero")
	@ApiModelProperty(value = "是否是sop流程中的任务", position = 30)
	@TableField("is_sop")
	@Dict(dicCode = "mesYesZero")
	private String sop;

}
