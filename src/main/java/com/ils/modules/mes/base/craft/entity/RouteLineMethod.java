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
 * @Description: 工序关联质检方案
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_route_line_method")
@ApiModel(value="mes_route_line_method对象", description="工序关联质检方案")
public class RouteLineMethod extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工艺id*/
    @TableField("route_id")
    @ApiModelProperty(value = "工艺id", position = 2)
	private String routeId;
	/**路线id*/
    @Excel(name = "路线id", width = 15)
    @TableField("route_line_id")
    @ApiModelProperty(value = "路线id", position = 3)
	private String routeLineId;
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
