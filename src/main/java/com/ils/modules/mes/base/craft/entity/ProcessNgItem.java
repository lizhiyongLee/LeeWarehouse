package com.ils.modules.mes.base.craft.entity;

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
 * @Description: 工序关联不良项
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_process_ng_item")
@ApiModel(value="mes_process_ng_item对象", description="工序关联不良项")
public class ProcessNgItem extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工序id*/
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 2)
	private String processId;
	/**不良项分类名称*/
    @Excel(name = "不良项分类名称", width = 15)
    @TableField("ng_item_type_name")
    @ApiModelProperty(value = "不良项分类名称", position = 3)
	private String ngItemTypeName;
	/**不良项分类Id*/
    @Excel(name = "不良项分类Id", width = 15)
    @TableField("ng_item_type_id")
    @ApiModelProperty(value = "不良项分类Id", position = 4)
	private String ngItemTypeId;
	/**不良项名称*/
    @Excel(name = "不良项名称", width = 15)
    @TableField("ng_item_name")
    @ApiModelProperty(value = "不良项名称", position = 5)
	private String ngItemName;
	/**不良项id*/
    @Excel(name = "不良项id", width = 15)
    @TableField("ng_item_id")
    @ApiModelProperty(value = "不良项id", position = 6)
	private String ngItemId;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 7)
	private String note;
}
