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
 * @Description: 计量单位
 * @Author: fengyi
 * @Date: 2020-10-16
 * @Version: V1.0
 */
@Data
@TableName("mes_unit")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_unit对象", description="计量单位")
public class Unit extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称", position = 2)
    @TableField("unit_name")
	private String unitName;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 3)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 4)
    @TableField("note")
	private String note;
}
