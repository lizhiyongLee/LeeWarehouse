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
 * @Description: 工序关联工位
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_route_line_station")
@ApiModel(value="mes_route_line_station对象", description="工序关联工位")
public class RouteLineStation extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工艺id*/
    @TableField("route_id")
    @ApiModelProperty(value = "工艺id", position = 2)
	private String routeId;
	/**路线id*/
    @TableField("route_line_id")
    @ApiModelProperty(value = "路线id", position = 3)
	private String routeLineId;
	/**工位id*/
    @Excel(name = "工位编码", width = 15,dicCode = "id",dictTable = "mes_workstation",dicText = "station_code")
    @TableField("station_id")
    @ApiModelProperty(value = "工位id", position = 4)
	private String stationId;
	/**备注*/
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 5)
	private String note;
}
