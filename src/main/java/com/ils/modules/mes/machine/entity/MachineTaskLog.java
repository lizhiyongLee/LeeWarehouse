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
 * @Description: 设备维修日志
 * @Author: Tian
 * @Date:   2020-11-25
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_task_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_task_log对象", description="设备维修日志")
public class MachineTaskLog extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1、维修任务；2、点检任务；3、保养任务；*/
	@Excel(name = "1、维修任务；2、点检任务；3、保养任务；", width = 15)
    @ApiModelProperty(value = "1、维修任务；2、点检任务；3、保养任务；", position = 2)
    @TableField("task_type")
	@Dict(dicCode = "mesTaskType")
	private String taskType;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id", position = 3)
    @TableField("task_id")
	private String taskId;
	/**1、创建任务；2、领取任务；3、暂停任务；4、恢复任务；5、结束任务；6、取消任务；*/
	@Excel(name = "1、创建任务；2、领取任务；3、暂停任务；4、恢复任务；5、结束任务；6、取消任务；", width = 15)
    @ApiModelProperty(value = "1、创建任务；2、领取任务；3、暂停任务；4、恢复任务；5、结束任务；6、取消任务；", position = 4)
    @TableField("oprate_type")
	@Dict(dicCode = "mesOprateType")
	private String oprateType;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述", position = 5)
    @TableField("description")
	private String description;
}
