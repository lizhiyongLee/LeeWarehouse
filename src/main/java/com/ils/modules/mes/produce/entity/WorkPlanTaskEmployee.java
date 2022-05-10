package com.ils.modules.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 派工计划生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Data
@TableName("mes_work_plan_task_employee")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_plan_task_employee对象", description="派工计划生产任务关联计划执行人员")
public class WorkPlanTaskEmployee extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**任务单id*/
	@Excel(name = "任务单id", width = 15)
    @ApiModelProperty(value = "任务单id", position = 2)
    @TableField("task_id")
	private String taskId;
	/**员工id*/
	@Excel(name = "员工id", width = 15)
    @ApiModelProperty(value = "员工id", position = 3)
    @TableField("employee_id")
	private String employeeId;
	/**员工编码*/
	@Excel(name = "员工编码", width = 15)
    @ApiModelProperty(value = "员工编码", position = 4)
    @TableField("employee_code")
	private String employeeCode;
	/**员工名称*/
	@Excel(name = "员工名称", width = 15)
    @ApiModelProperty(value = "员工名称", position = 5)
    @TableField("employee_name")
	private String employeeName;
}
