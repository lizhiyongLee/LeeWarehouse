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
 * @Description: 产品路线工序工位
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_product_route_station")
@ApiModel(value="mes_product_route_station对象", description="产品路线工序工位")
public class ProductRouteStation extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**产品id*/
    @TableField("product_id")
    @ApiModelProperty(value = "产品id", position = 2)
	private String productId;
	/**产品LineId*/
    @Excel(name = "产品LineId", width = 15)
    @TableField("product_line_id")
    @ApiModelProperty(value = "产品LineId", position = 3)
	private String productLineId;
	/**工位id*/
    @Excel(name = "工位id", width = 15)
    @TableField("station_id")
    @ApiModelProperty(value = "工位id", position = 4)
	private String stationId;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 5)
	private String note;
}
