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
 * @Description: 物料关联质检方案
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_quality")
@ApiModel(value="mes_item_quality对象", description="物料关联质检方案")
public class ItemQuality extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**物料id*/
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 2)
	private String itemId;
	/**质检方案id*/
    @Excel(name = "质检方案id", width = 15)
    @TableField("quality_id")
    @ApiModelProperty(value = "质检方案id", position = 3)
	private String qualityId;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 4)
	private String note;
}
