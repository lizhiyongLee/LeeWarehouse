package com.ils.modules.mes.base.product.entity;

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
 * @Description: 产品路线工序质检方案
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_product_route_method")
@ApiModel(value="mes_product_route_method对象", description="产品路线工序质检方案")
public class ProductRouteMethod extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**产品id*/
    @TableField("product_id")
    @ApiModelProperty(value = "产品id", position = 2)
	private String productId;
	/**产线Lineid*/
    @Excel(name = "产线Lineid", width = 15)
    @TableField("product_line_id")
    @ApiModelProperty(value = "产线Lineid", position = 3)
	private String productLineId;
	/**质检方案id*/
    @Excel(name = "质检方案id", width = 15)
    @TableField("qc_method_id")
    @ApiModelProperty(value = "质检方案id", position = 4)
	private String qcMethodId;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 5)
	private String note;
}
