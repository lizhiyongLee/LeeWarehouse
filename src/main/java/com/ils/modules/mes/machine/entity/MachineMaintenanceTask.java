package com.ils.modules.mes.machine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
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
 * @Description: 维保任务
 * @Author: Tian
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_maintenance_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_machine_maintenance_task对象", description = "维保任务")
public class MachineMaintenanceTask extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 1,点检；2、保养；
     */
    @Excel(name = "维护类型", width = 15, dicCode = "mesPolicyTypeWithType")
    @ApiModelProperty(value = "1,点检；2、保养；", position = 2)
    @TableField("maintenance_type")
    @Dict(dicCode = "mesPolicyTypeWithType")
    private String maintenanceType;
    /**
     * 维保任务号
     */
    @Excel(name = "任务编码", width = 15)
    @ApiModelProperty(value = "维保任务号", position = 3)
    @TableField("task_code")
    @KeyWord
    private String taskCode;
    /**
     * 维保任务标题
     */
    @Excel(name = "任务标题", width = 15)
    @ApiModelProperty(value = "维保任务标题", position = 4)
    @TableField("task_title")
    @KeyWord
    private String taskTitle;
    /**
     * policyId
     */
    @ApiModelProperty(value = "policyId", position = 5)
    @TableField("policy_id")
    private String policyId;
    /**
     * 策略编码
     */
    @Excel(name = "策略编码", width = 15)
    @ApiModelProperty(value = "策略编码", position = 6)
    @TableField("policy_code")
    private String policyCode;
    /**
     * 策略名称
     */
    @Excel(name = "策略名称", width = 15)
    @ApiModelProperty(value = "策略名称", position = 7)
    @TableField("policy_name")
    private String policyName;
    /**
     * 维修目标
     */
    @ApiModelProperty(value = "维修目标", position = 8)
    @TableField("maintain_machine_id")
    @Dict(dicText = "machine_name", dictTable = "mes_machine", dicCode = "id")
    private String maintainMachineId;
    /**
     * 模板id
     */
    @Excel(name = "模板", width = 15, dictTable = "mes_report_template", dicCode = "id", dicText = "template_name")
    @ApiModelProperty(value = "模板id", position = 9)
    @TableField("template_id")
    @Dict(dictTable = "mes_report_template", dicCode = "id", dicText = "template_name")
    private String templateId;
    /**
     * 计划开始时间
     */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 10)
    @TableField("plan_start_time")
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    @Excel(name = "计划结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划结束时间", position = 11)
    @TableField("plan_end_time")
    private Date planEndTime;
    /**
     * 实际开始时间
     */
    @Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际开始时间", position = 12)
    @TableField("real_start_time")
    private Date realStartTime;
    /**
     * 实际结束时间
     */
    @Excel(name = "实际结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际结束时间", position = 13)
    @TableField("real_end_time")
    private Date realEndTime;
    /**
     * acceptor
     */
    @Excel(name = "验收人", width = 15)
    @ApiModelProperty(value = "acceptor", position = 14)
    @TableField("acceptor")
    private String acceptor;
    /**
     * realExcuter
     */
    @Excel(name = "实际执行人", width = 15,dicCode = "id", dictTable = "sys_user", dicText = "realname")
    @ApiModelProperty(value = "realExcuter", position = 15)
    @TableField("real_excuter")
    @Dict(dicCode = "id", dictTable = "sys_user", dicText = "realname")
    private String realExcuter;
    /**
     * 附件
     */
    @ApiModelProperty(value = "附件", position = 16)
    @TableField("attach")
    private String attach;
    /**
     * 状态 ：1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；
     */
    @Excel(name = "状态", width = 15, dicCode = "mesRepairStatus")
    @ApiModelProperty(value = "状态 ：1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；", position = 17)
    @TableField("status")
    @Dict(dicCode = "mesRepairStatus")
    private String status;
}
