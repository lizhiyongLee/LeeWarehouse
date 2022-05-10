package com.ils.modules.mes.base.qc.entity;

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
 * @Description: 质检方案关联物料
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_qc_method_item")
@ApiModel(value="mes_qc_method_item对象", description="质检方案关联物料")
public class QcMethodItem extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**质检方案id*/
    @TableField("mehtod_id")
    @ApiModelProperty(value = "质检方案id", position = 2)
	private String mehtodId;
	/**物料id*/
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 3)
	private String itemId;
	/**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 4)
	private String itemName;
	/**物料编码*/
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 5)
	private String itemCode;
}
