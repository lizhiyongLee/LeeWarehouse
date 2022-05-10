package com.ils.modules.mes.machine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @Description: 设备关联停机时间
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_stop_time")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_stop_time对象", description="设备关联停机时间")
public class MachineStopTime extends ILSEntity implements Comparable<MachineStopTime>{
	private static final long serialVersionUID = 1L;
    
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id", position = 2)
    @TableField("machine_id")
	@Dict(dicText = "machine_name",dicCode = "id",dictTable = "mes_machine")
	private String machineId;
	/**1、计划性停机；2、非计划性停机；*/
	@Excel(name = "1、计划性停机；2、非计划性停机；", width = 15)
    @ApiModelProperty(value = "1、计划性停机；2、非计划性停机；", position = 3)
    @TableField("stop_type")
	@Dict(dicCode = "mesReasonType")
	private String stopType;
	/**停机原因*/
	@Excel(name = "停机原因", width = 15)
    @ApiModelProperty(value = "停机原因", position = 4)
    @TableField("stop_reason_id")
	@Dict(dicCode = "id",dictTable = "mes_machine_stop_reason",dicText = "stop_reason")
	private String stopReasonId;
	/**停机开始时间*/
	@Excel(name = "停机开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "停机开始时间", position = 5)
    @TableField("start_time")
	private Date startTime;
	/**停机结束时间*/
	@Excel(name = "停机结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "停机结束时间", position = 6)
    @TableField("end_time")
	private Date endTime;

	@Override
	public int compareTo(MachineStopTime machineStopTime) {
		return this.getStartTime().compareTo(machineStopTime.getStartTime());
	}
}
