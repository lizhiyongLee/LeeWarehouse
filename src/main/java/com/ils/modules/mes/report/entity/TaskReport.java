package com.ils.modules.mes.report.entity;

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
 * @Description: 任务报告记录
 * @Author: Tian
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Data
@TableName("mes_task_report")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_task_report对象", description="任务报告记录")
public class TaskReport extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1,点检任务；2、保养任务；3、维修任务；4、生产任务*/
	@Excel(name = "1,点检任务；2、保养任务；3、维修任务；4、生产任务", width = 15, dicCode = "mesTaskType")
    @ApiModelProperty(value = "1,点检任务；2、保养任务；3、维修任务；4、生产任务", position = 2)
    @TableField("task_type")
	@Dict(dicCode = "mesTaskType")
	private String taskType;
	/**任务号*/
	@Excel(name = "任务号", width = 15)
    @ApiModelProperty(value = "任务号", position = 3)
    @TableField("task_id")
	private String taskId;
	/**模板id*/
	@Excel(name = "模板id", width = 15)
    @ApiModelProperty(value = "模板id", position = 4)
    @TableField("template_id")
	private String templateId;
	/**以jason形式存储：控件id,控件名称，控件值。*/
	@Excel(name = "以jason形式存储：控件id,控件名称，控件值。", width = 15)
    @ApiModelProperty(value = "以jason形式存储：控件id,控件名称，控件值。", position = 5)
    @TableField("report_context")
	private String reportContext;

	/** 关联标准作业步骤id */
	@Excel(name = "关联标准作业步骤", width = 15)
	@ApiModelProperty(value = "关联标准作业步骤id", position = 6)
	@TableField("sop_step_id")
	private String sopStepId;

	/** 关联标准作业控件id */
	@Excel(name = "关联标准作业控件", width = 15)
	@ApiModelProperty(value = "关联标准作业控件id", position = 7)
	@TableField("sop_control_id")
	private String sopControlId;
}
