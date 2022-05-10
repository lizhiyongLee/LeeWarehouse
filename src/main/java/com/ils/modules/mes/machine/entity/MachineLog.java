package com.ils.modules.mes.machine.entity;

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
 * @Description: 设备日志
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_log对象", description="设备日志")
public class MachineLog extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id", position = 2)
    @TableField("machine_id")
	private String machineId;
	/**记录关键动作的操作；*/
	@Excel(name = "记录关键动作的操作；", width = 15)
    @ApiModelProperty(value = "记录关键动作的操作；", position = 3)
    @TableField("oprate_type")
	@Dict(dicCode = "mesMachineLogType")
	private String oprateType;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述", position = 4)
    @TableField("description")
	private String description;
}
