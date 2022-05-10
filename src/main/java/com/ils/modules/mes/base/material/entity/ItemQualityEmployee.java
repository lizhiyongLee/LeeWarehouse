package com.ils.modules.mes.base.material.entity;

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
 * @Description: 物料关联质检人员
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_quality_employee")
@ApiModel(value="mes_item_quality_employee对象", description="物料关联质检人员")
public class ItemQualityEmployee extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**物料id*/
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 2)
	private String itemId;
	/**质检人员id*/
    @Excel(name = "质检人员id", width = 15)
    @TableField("employee_id")
    @ApiModelProperty(value = "质检人员id", position = 3)
	private String employeeId;
	/**质检人员id*/
    @Excel(name = "质检人员姓名", width = 15)
    @TableField("employee_name")
    @ApiModelProperty(value = "质检人员姓名", position = 4)
	private String employeeName;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 5)
	private String note;
}
