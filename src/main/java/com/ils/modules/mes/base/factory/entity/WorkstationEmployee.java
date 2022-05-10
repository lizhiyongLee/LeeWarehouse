package com.ils.modules.mes.base.factory.entity;

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
 * @Description: 车位人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_workstation_employee")
@ApiModel(value="mes_workstation_employee对象", description="车位人员")
public class WorkstationEmployee extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工位id*/
    @TableField("workstation_id")
    @ApiModelProperty(value = "工位id", position = 2)
	private String workstationId;
	/**员工Id*/
    @Excel(name = "员工Id", width = 15)
    @TableField("employee_id")
    @ApiModelProperty(value = "员工Id", position = 3)
	private String employeeId;
	/**员工名称*/
    @Excel(name = "员工名称", width = 15)
    @TableField("employee_name")
    @ApiModelProperty(value = "员工名称", position = 4)
	private String employeeName;
	/**员工编码*/
    @Excel(name = "员工编码", width = 15)
    @TableField("employee_code")
    @ApiModelProperty(value = "员工编码", position = 5)
	private String employeeCode;
	/**岗位名称*/
    @Excel(name = "岗位名称", width = 15)
    @TableField("position_name")
    @ApiModelProperty(value = "岗位名称", position = 6)
	private String positionName;
	/**岗位id*/
    @Excel(name = "岗位id", width = 15)
    @TableField("position_id")
    @ApiModelProperty(value = "岗位id", position = 7)
	private String positionId;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 8)
	private String note;
}
