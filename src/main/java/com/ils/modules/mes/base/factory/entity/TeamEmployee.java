package com.ils.modules.mes.base.factory.entity;

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
 * @Description: 班组人员
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_team_employee")
@ApiModel(value="mes_team_employee对象", description="班组人员")
public class TeamEmployee extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**班组id*/
    @TableField("team_id")
    @ApiModelProperty(value = "班组id", position = 2)
	private String teamId;
	/**用户ID*/
    @Excel(name = "用户ID", width = 15)
    @TableField("employee_id")
    @ApiModelProperty(value = "用户ID", position = 3)
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
	/**岗位id*/
    @Excel(name = "岗位id", width = 15)
    @TableField("position_id")
    @ApiModelProperty(value = "岗位id", position = 6)
	private String positionId;
	/**岗位名称*/
    @Excel(name = "岗位名称", width = 15)
    @TableField("position_name")
    @ApiModelProperty(value = "岗位名称", position = 7)
	private String positionName;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 8)
	private String status;
	/**1，是组长；0，非组长；*/
    @Excel(name = "1，是组长；0，非组长；", width = 15, dicCode = "mesYesZero")
    @TableField("is_leader")
    @ApiModelProperty(value = "1，是组长；0，非组长；", position = 9)
    @Dict(dicCode = "mesYesZero")
	private String leader;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 10)
	private String note;
}
