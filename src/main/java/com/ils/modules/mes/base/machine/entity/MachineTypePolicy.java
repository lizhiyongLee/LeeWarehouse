package com.ils.modules.mes.base.machine.entity;

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
 * @Description: 设备类型关联策略组
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_type_policy")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_type_policy对象", description="设备类型关联策略组")
public class MachineTypePolicy extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备类型id*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型id", position = 2)
    @TableField("machine_type_id")
	private String machineTypeId;
	/**策略组id*/
	@Excel(name = "策略组", width = 15)
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
	/**1、点检；2、保养；*/
	@Excel(name = "策略类型", width = 15)
    @ApiModelProperty(value = "1、点检；2、保养；", position = 8)
    @TableField("policy_type")
	@Dict(dicCode = "mesPolicyTypeWithType")
	private String policyType;
	/**1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建*/
	@Excel(name = "策略规则", width = 15)
    @ApiModelProperty(value = "1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建", position = 9)
    @TableField("policy_rule")
	@Dict(dicCode = "mesPolicyRule")
	private String policyRule;
	/**用度名*/
	@Excel(name = "用度名", width = 15)
    @ApiModelProperty(value = "用度名", position = 10)
    @TableField("rule_name")
	@Dict(dictTable = "mes_machine_dataconfig" ,dicCode = "data_name",dicText = "data_name")
	private String ruleName;
	/**qty*/
	@Excel(name = "周期/用度", width = 15)
    @ApiModelProperty(value = "qty", position = 11)
    @TableField("qty")
	private Integer qty;
	/**1、分钟；2、小时；3、日；4、周；5、月*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "1、分钟；2、小时；3、日；4、周；5、月", position = 12)
    @TableField("unit")
	@Dict(dicCode = "mesUnit")
	private String unit;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称", position = 13)
    @TableField("task_name")
	private String taskName;
	/**任务描述*/
	@Excel(name = "任务描述", width = 15)
    @ApiModelProperty(value = "任务描述", position = 14)
    @TableField("task_description")
	private String taskDescription;
	/**计划工时*/
	@Excel(name = "计划工时", width = 15)
    @ApiModelProperty(value = "计划工时", position = 15)
    @TableField("plan_time")
	private Integer planTime;
	/**1、分钟；2、小时；*/
	@Excel(name = "工时单位", width = 15)
    @ApiModelProperty(value = "1、分钟；2、小时；", position = 16)
    @TableField("time_unit")
	@Dict(dicCode = "mesTimeUnit")
	private String timeUnit;
	/**模板id*/
	@Excel(name = "模板", width = 15)
    @ApiModelProperty(value = "模板id", position = 17)
    @TableField("template_id")
	private String templateId;
	/**1、需要，0，不需要*/
    @ApiModelProperty(value = "1、需要，0，不需要", position = 18)
    @TableField("is_scan")
	private String scan;
	/**1,需要；0，不需要*/
    @ApiModelProperty(value = "1,需要；0，不需要", position = 19)
    @TableField("is_confirm")
	@Dict(dicCode = "confirm")
	private String confirm;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 12)
	@TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
	/**附件*/
    @ApiModelProperty(value = "附件", position = 20)
    @TableField("attach")
	private String attach;

}
