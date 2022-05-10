package com.ils.modules.mes.base.qc.entity;

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
 * @Description: 不良类型
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Data
@TableName("mes_ng_item_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_ng_item_type对象", description="不良类型")
public class NgItemType extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**不良类型名称*/
	@Excel(name = "不良类型名称", width = 15)
    @ApiModelProperty(value = "不良类型名称", position = 2)
    @TableField("ng_type_name")
	private String ngTypeName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 3)
    @TableField("note")
	private String note;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 4)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
}
