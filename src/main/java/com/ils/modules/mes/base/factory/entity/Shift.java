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
 * @Description: 班次
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Data
@TableName("mes_shift")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_shift对象", description="班次")
public class Shift extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**班次*/
	@Excel(name = "班次", width = 15)
    @ApiModelProperty(value = "班次", position = 2)
    @TableField("shift_name")
	private String shiftName;
	/**班次开始时间*/
    @Excel(name = "班次开始时间", width = 20)
    @ApiModelProperty(value = "班次开始时间", position = 3)
    @TableField("shift_start_time")
    private String shiftStartTime;
	/**班次结束时间*/
    @Excel(name = "班次结束时间", width = 20)
    @ApiModelProperty(value = "班次结束时间", position = 4)
    @TableField("shift_end_time")
    private String shiftEndTime;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 5)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 6)
    @TableField("note")
	private String note;
}
