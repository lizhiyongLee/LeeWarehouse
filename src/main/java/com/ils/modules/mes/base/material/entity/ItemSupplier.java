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
 * @Description: 物料关联供应商
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_supplier")
@ApiModel(value="mes_item_supplier对象", description="物料关联供应商")
public class ItemSupplier extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**物料id*/
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 2)
	private String itemId;
	/**供应商id*/
    @Excel(name = "供应商id", width = 15)
    @TableField("supplier_id")
    @ApiModelProperty(value = "供应商id", position = 3)
	private String supplierId;

    /** 供应商id */
    @Excel(name = "供应商物料编码", width = 15)
    @TableField("supplier_code")
    @ApiModelProperty(value = "供应商物料编码", position = 3)
    private String supplierCode;

	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 4)
	private String note;
}
