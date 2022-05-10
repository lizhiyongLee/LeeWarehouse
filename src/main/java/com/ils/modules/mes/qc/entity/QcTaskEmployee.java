package com.ils.modules.mes.qc.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.common.system.base.entity.ILSEntity;


/**
 * @Description: 质检任务关联人员
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_task_employee")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_qc_task_employee对象", description="质检任务关联人员")
public class QcTaskEmployee extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**执行生产任务id*/
	@Excel(name = "执行生产任务id", width = 15)
    @ApiModelProperty(value = "执行生产任务id", position = 2)
    @TableField("excute_task_id")
	private String excuteTaskId;
	/**员工id*/
	@Excel(name = "员工id", width = 15)
    @ApiModelProperty(value = "员工id", position = 3)
    @TableField("employee_id")
	private String employeeId;
	/**员工编码*/
	@Excel(name = "员工编码", width = 15)
    @ApiModelProperty(value = "员工编码", position = 4)
    @TableField("employee_code")
	private String employeeCode;
	/**员工名称*/
	@Excel(name = "员工名称", width = 15)
    @ApiModelProperty(value = "员工名称", position = 5)
    @TableField("employee_name")
	private String employeeName;
	/**1,指派领取；2，自己领取；3、交接。*/
	@Excel(name = "1,指派领取；2，自己领取；3、交接。", width = 15)
    @ApiModelProperty(value = "1,指派领取；2，自己领取；3、交接。", position = 6)
    @TableField("using_type")
	private String usingType;
}
