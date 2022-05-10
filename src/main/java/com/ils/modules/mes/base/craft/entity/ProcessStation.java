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
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Data
@TableName("mes_process_station")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_process_station对象", description="工序关联工位")
public class ProcessStation extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**工序id*/
	@Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 2)
    @TableField("process_id")
	private String processId;
	/**工位id*/
	@Excel(name = "工位id", width = 15)
    @ApiModelProperty(value = "工位id", position = 3)
    @TableField("station_id")
	private String stationId;
	/**工位编码*/
	@Excel(name = "工位编码", width = 15)
    @ApiModelProperty(value = "工位编码", position = 4)
    @TableField("station_code")
	private String stationCode;
	/**工位名称*/
	@Excel(name = "工位名称", width = 15)
    @ApiModelProperty(value = "工位名称", position = 5)
    @TableField("station_name")
	private String stationName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 6)
    @TableField("note")
	private String note;
}
