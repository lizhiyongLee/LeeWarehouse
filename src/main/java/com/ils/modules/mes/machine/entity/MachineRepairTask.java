package com.ils.modules.mes.machine.entity;

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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @Description: 维修任务
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_repair_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_repair_task对象", description="维修任务")
public class MachineRepairTask extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称", position = 2)
    @TableField("task_name")
	@KeyWord
	private String taskName;
	/**任务编码*/
	@Excel(name = "任务编码", width = 15)
    @ApiModelProperty(value = "任务编码", position = 3)
    @TableField("task_code")
	@KeyWord
	private String taskCode;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
    @ApiModelProperty(value = "任务描述", position = 4)
    @TableField("task_description")
	private String taskDescription;
	/**维修目标*/
    @ApiModelProperty(value = "维修目标", position = 5)
    @TableField("repair_machine_id")
	private String repairMachineId;
	/**故障现象*/
	@Excel(name = "故障现象", width = 15,dicCode = "id",dictTable = "mes_machine_fault_appearance",dicText = "fault_appearance")
    @ApiModelProperty(value = "故障现象", position = 6)
    @TableField("fault_appearance")
	@Dict(dicCode = "id",dictTable = "mes_machine_fault_appearance",dicText = "fault_appearance")
	private String faultAppearance;
	/**故障原因*/
	@Excel(name = "faultReason", width = 15,dictTable = "mes_machine_fault_reason",dicCode = "id",dicText = "fault_reason")
    @ApiModelProperty(value = "faultReason", position = 7)
    @TableField("fault_reason")
	@Dict(dictTable = "mes_machine_fault_reason",dicCode = "id",dicText = "fault_reason")
	private String faultReason;
	/**计划开始时间*/
	@Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "计划开始时间", position = 9)
	@TableField("plan_start_time")
	private Date planStartTime;
	/**计划结束时间*/
	@Excel(name = "计划结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "计划结束时间", position = 10)
	@TableField("plan_end_time")
	private Date planEndTime;
	/**1、需要，0，不需要*/
	@Excel(name = "扫描确认", width = 15,dicCode = "mesScan")
    @ApiModelProperty(value = "1、需要，0，不需要", position = 9)
    @TableField("is_scan")
	@Dict(dicCode = "mesScan")
	private String scan;
	/**模板id*/
	@Excel(name = "模板", width = 15,dicText = "template_name",dicCode = "id",dictTable = "mes_report_template")
    @ApiModelProperty(value = "模板id", position = 10)
    @TableField("template_id")
	@Dict(dicText = "template_name",dicCode = "id",dictTable = "mes_report_template")
	private String templateId;
	/**1,需要；0，不需要*/
    @ApiModelProperty(value = "1,需要；0，不需要", position = 11)
    @TableField("is_confirm")
	private String confirm;
	/**1、提前1小时，2、提前8小时；3、提前1天；4、提前1周；5、不提醒；*/
	@Excel(name = "任务提醒", width = 15,dicCode = "mesAheadType")
    @ApiModelProperty(value = "1、提前1小时，2、提前8小时；3、提前1天；4、提前1周；5、不提醒；", position = 12)
    @TableField("ahead_type")
	@Dict(dicCode = "mesAheadType")
	private String aheadType;
	/**realExcuter*/
    @ApiModelProperty(value = "realExcuter", position = 13)
    @TableField("real_excuter")
	private String realExcuter;
	/**excuteStartTime*/
	@Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "excuteStartTime", position = 14)
    @TableField("excute_start_time")
	private Date excuteStartTime;
	/**excuteEndTime*/
	@Excel(name = "实际结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "excuteEndTime", position = 15)
    @TableField("excute_end_time")
	private Date excuteEndTime;
	/**验收人*/
	@Excel(name = "验收人", width = 15)
    @ApiModelProperty(value = "验收人", position = 16)
    @TableField("acceptor")
	@Dict(dicText = "realname",dicCode = "id",dictTable = "sys_user")
	private String acceptor;
	/**附件*/
    @ApiModelProperty(value = "附件", position = 17)
    @TableField("attach")
	private String attach;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 18)
    @TableField("note")
	private String note;
	/**状态 ：1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；*/
	@Excel(name = "状态", width = 15,dicCode = "mesRepairStatus")
    @ApiModelProperty(value = "状态 ：1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；", position = 19)
    @TableField("status")
	@Dict(dicCode = "mesRepairStatus")
	private String status;
}
