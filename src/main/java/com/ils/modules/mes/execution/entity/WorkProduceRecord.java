package com.ils.modules.mes.execution.entity;

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

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description: 产出记录
 * @Author: fengyi
 */
@Data
@TableName("mes_work_produce_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_produce_record对象", description="产出记录")
public class WorkProduceRecord extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**如果为空，则采取的是流转卡的生产方式；否则是生产任务的形式。*/
	@Excel(name = "如果为空，则采取的是流转卡的生产方式；否则是生产任务的形式。", width = 15)
    @ApiModelProperty(value = "如果为空，则采取的是流转卡的生产方式；否则是生产任务的形式。", position = 2)
    @TableField("produce_task_id")
	private String produceTaskId;
	/**工单id*/
	@Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 3)
    @TableField("order_id")
	private String orderId;
	/**工单号*/
	@Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 4)
    @TableField("order_no")
	private String orderNo;
	/**1自动报工 2手工报工3有生产任务的报工4无生产任务，流转卡形式报工*/
	@Excel(name = "1自动报工 2手工报工3有生产任务的报工4无生产任务，流转卡形式报工", width = 15, dicCode = "mesReportType")
    @ApiModelProperty(value = "1自动报工 2手工报工3有生产任务的报工4无生产任务，流转卡形式报工", position = 5)
    @TableField("report_type")
	@Dict(dicCode = "mesReportType")
    private String reportType;
	/**工序id*/
	@Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 6)
    @TableField("process_id")
	private String processId;
	/**工序编码*/
	@Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 7)
    @TableField("process_code")
	private String processCode;
	/**工序名称*/
	@Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称", position = 8)
    @TableField("process_name")
	private String processName;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
    @ApiModelProperty(value = "产品id", position = 9)
    @TableField("item_id")
	private String itemId;
	/**产品编码*/
	@Excel(name = "产品编码", width = 15)
    @ApiModelProperty(value = "产品编码", position = 10)
    @TableField("item_code")
	private String itemCode;
	/**产品编码*/
	@Excel(name = "产品编码", width = 15)
    @ApiModelProperty(value = "产品编码", position = 11)
    @TableField("item_name")
	private String itemName;
	/**物料单元id*/
	@Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 12)
    @TableField("item_cell_state_id")
	private String itemCellStateId;
	/**对某个租户内，唯一存在*/
	@Excel(name = "对某个租户内，唯一存在", width = 15)
    @ApiModelProperty(value = "对某个租户内，唯一存在", position = 13)
    @TableField("qrcode")
	private String qrcode;
    /** 生产批号 */
    @Excel(name = "生产批号", width = 15)
    @ApiModelProperty(value = "生产批号", position = 14)
    @TableField("batch_no")
	private String batchNo;
    /** 客户批号 */
    @Excel(name = "客户批号", width = 15)
    @ApiModelProperty(value = "客户批号", position = 15)
    @TableField("customer_batch")
	private String customerBatch;
	/**序列码可以用来区分相同批次不同箱数*/
	@Excel(name = "序列码可以用来区分相同批次不同箱数", width = 15)
    @ApiModelProperty(value = "序列码可以用来区分相同批次不同箱数", position = 16)
    @TableField("sequence_code")
	private String sequenceCode;
	/**报工数量*/
	@Excel(name = "报工数量", width = 15)
    @ApiModelProperty(value = "报工数量", position = 17)
    @TableField("submit_qty")
	private BigDecimal submitQty;
	/**单位*/
	@Excel(name = "单位", width = 15, dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @ApiModelProperty(value = "单位", position = 18)
    @TableField("unit")
	@Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
	private String unit;
	/**1、合格；2、待检；3、不合格；*/
	@Excel(name = "1、合格；2、待检；3、不合格；", width = 15)
    @ApiModelProperty(value = "1、合格；2、待检；3、不合格；", position = 19)
    @TableField("qc_status")
	@Dict(dicCode = "mesQcStatus")
	private String qcStatus;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期", position = 20)
    @TableField("produce_date")
	private Date produceDate;
	/**归属日期，某些工厂，例如夜班时间2020.10.8 0：00：00到6：00：00,虽然日期上是10.8日，但是他们产量归属于10.7日，所以增加这个字段便于扩展。*/
	@Excel(name = "归属日期，某些工厂，例如夜班时间2020.10.8 0：00：00到6：00：00,虽然日期上是10.8日，但是他们产量归属于10.7日，所以增加这个字段便于扩展。", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "归属日期，某些工厂，例如夜班时间2020.10.8 0：00：00到6：00：00,虽然日期上是10.8日，但是他们产量归属于10.7日，所以增加这个字段便于扩展。", position = 21)
    @TableField("owner_date")
	private Date ownerDate;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", position = 22)
    @TableField("start_time")
	private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", position = 23)
    @TableField("end_time")
	private Date endTime;
	/**生产用时*/
	@Excel(name = "生产用时", width = 15)
    @ApiModelProperty(value = "生产用时", position = 24)
    @TableField("product_time")
	private BigDecimal productTime;
	/**timeUnit*/
	@Excel(name = "timeUnit", width = 15)
    @ApiModelProperty(value = "timeUnit", position = 25)
    @TableField("time_unit")
	private String timeUnit;
	/**员工IDs*/
	@Excel(name = "员工IDs", width = 15)
    @ApiModelProperty(value = "员工IDs", position = 26)
    @TableField("employee_id")
	private String employeeId;
	/**员工编码*/
	@Excel(name = "员工编码", width = 15)
    @ApiModelProperty(value = "员工编码", position = 27)
    @TableField("employee_code")
	private String employeeCode;
	/**员工名称*/
	@Excel(name = "员工名称", width = 15)
    @ApiModelProperty(value = "员工名称", position = 28)
    @TableField("employee_name")
	private String employeeName;
	/**班组id*/
	@Excel(name = "班组id", width = 15)
    @ApiModelProperty(value = "班组id", position = 29)
    @TableField("team_id")
	private String teamId;
	/**工位id*/
	@Excel(name = "工位id", width = 15)
    @ApiModelProperty(value = "工位id", position = 30)
    @TableField("station_id")
	private String stationId;
	/**工位编码*/
	@Excel(name = "工位编码", width = 15)
    @ApiModelProperty(value = "工位编码", position = 31)
    @TableField("station_code")
	private String stationCode;
	/**工位名称*/
	@Excel(name = "工位名称", width = 15)
    @ApiModelProperty(value = "工位名称", position = 32)
    @TableField("station_name")
	private String stationName;
	/** 关联标准作业步骤id */
	@Excel(name = "关联标准作业步骤", width = 15)
	@ApiModelProperty(value = "关联标准作业步骤id", position = 18)
	@TableField("sop_step_id")
	private String sopStepId;

	/** 关联标准作业控件id */
	@Excel(name = "关联标准作业控件", width = 15)
	@ApiModelProperty(value = "关联标准作业控件id", position = 18)
	@TableField("sop_control_id")
	private String sopControlId;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 33)
    @TableField("attach")
	private String attach;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 34)
    @TableField("note")
	private String note;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
	@ApiModelProperty(value = "单位名称", position = 35)
	@TableField("unit_name")
	private String unitName;
	/**1、主产出；2、联产出。*/
	@Excel(name = "1、主产出；2、联产出。", width = 15)
	@ApiModelProperty(value = "1、主产出；2、联产出。", position = 36)
	@TableField("product_type")
	private String productType;
}
