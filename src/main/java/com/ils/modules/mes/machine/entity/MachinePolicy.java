package com.ils.modules.mes.machine.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.ils.common.aspect.annotation.Dict;
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
 * @Description: 设备关联策略
 * @Author: Tian
 * @Date:   2020-11-16
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_policy")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_policy对象", description="设备关联策略")
public class MachinePolicy extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备id*/
    @ApiModelProperty(value = "设备id", position = 2)
    @TableField("machine_id")
	private String machineId;
	/**策略组id*/
	@Excel(name = "策略组", width = 15,dictTable = "mes_machine_maintain_policy" ,dicCode = "id",dicText = "data_name")
    @ApiModelProperty(value = "策略组id", position = 3)
    @TableField("policy_group_id")
	@Dict(dictTable = "mes_machine_maintain_policy" ,dicCode = "id",dicText = "data_name")
	private String policyGroupId;
	/**策略名称*/
	@Excel(name = "策略名称", width = 15)
    @ApiModelProperty(value = "策略名称", position = 4)
    @TableField("policy_name")
	private String policyName;
	/**策略描述*/
	@Excel(name = "策略描述", width = 15)
    @ApiModelProperty(value = "策略描述", position = 5)
    @TableField("policy_description")
	private String policyDescription;
	/**策略开始时间*/
	@Excel(name = "策略开始时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "策略开始时间", position = 6)
    @TableField("start_time")
	private Date startTime;
	/**策略结束时间*/
	@Excel(name = "策略结束时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "策略结束时间", position = 7)
    @TableField("end_time")
	private Date endTime;
	/**执行基准时间*/
	@Excel(name = "执行基准时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行基准时间", position = 8)
    @TableField("excute_base_time")
	private Date excuteBaseTime;
	/**上次执行时间*/
	@Excel(name = "上次执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上次执行时间", position = 9)
    @TableField("last_excute_time")
	private Date lastExcuteTime;
	/**1、点检；2、保养；*/
	@Excel(name = "策略类型", width = 15,dicCode = "mesPolicyTypeWithType")
    @ApiModelProperty(value = "1、点检；2、保养；", position = 10)
    @TableField("policy_type")
	@Dict(dicCode = "mesPolicyTypeWithType")
	private String policyType;
	/**1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建*/
	@Excel(name = "策略规则", width = 15,dicCode = "mesPolicyRule")
    @ApiModelProperty(value = "1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建", position = 11)
    @TableField("policy_rule")
	@Dict(dicCode = "mesPolicyRule")
	private String policyRule;
	/**用度名*/
	@Excel(name = "用度名", width = 15,dictTable = "mes_machine_dataconfig" ,dicCode = "data_name",dicText = "data_name")
    @ApiModelProperty(value = "用度名", position = 12)
    @TableField("rule_name")
	@Dict(dictTable = "mes_machine_dataconfig" ,dicCode = "data_name",dicText = "data_name")
	private String ruleName;
	/**qty*/
	@Excel(name = "值", width = 15)
    @ApiModelProperty(value = "qty", position = 13)
    @TableField("qty")
	private Integer qty;
	/**1、分钟；2、小时；3、日；4、周；5、月*/
	@Excel(name = "单位", width = 15,dicCode = "mesUnit")
    @ApiModelProperty(value = "1、分钟；2、小时；3、日；4、周；5、月", position = 14)
    @TableField("unit")
	@Dict(dicCode = "mesUnit")
	private String unit;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称", position = 15)
    @TableField("task_name")
	private String taskName;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
    @ApiModelProperty(value = "任务描述", position = 16)
    @TableField("task_description")
	private String taskDescription;
	/**计划工时*/
	@Excel(name = "计划工时", width = 15)
    @ApiModelProperty(value = "计划工时", position = 17)
    @TableField("plan_time")
	private Integer planTime;
	/**1、分钟；2、小时；*/
	@Excel(name = "工时单位", width = 15,dicCode = "mesTimeUnit")
    @ApiModelProperty(value = "1、分钟；2、小时；", position = 18)
    @TableField("time_unit")
	@Dict(dicCode = "mesTimeUnit")
	private String timeUnit;
	/**模板id*/
	@Excel(name = "模板", width = 15,dicText = "template_name",dicCode = "id",dictTable = "mes_report_template")
    @ApiModelProperty(value = "模板id", position = 19)
    @TableField("template_id")
	private String templateId;
	/**1、需要，0，不需要*/
    @ApiModelProperty(value = "1、需要，0，不需要", position = 20)
    @TableField("is_scan")
	private String scan;
	/**1,需要；0，不需要*/
    @ApiModelProperty(value = "1,需要；0，不需要", position = 21)
    @TableField("is_confirm")
	@Dict(dicCode = "confirm")
	private String confirm;
	/**附件*/
    @ApiModelProperty(value = "附件", position = 22)
    @TableField("attach")
	private String attach;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态", width = 15,dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 23)
    @TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
}
