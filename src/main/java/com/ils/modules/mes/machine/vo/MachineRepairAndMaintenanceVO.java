package com.ils.modules.mes.machine.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/23 13:48
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineRepairAndMaintenanceVO extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 维护类型
     */
    @Excel(name = "维护类型",dicCode = "mesMaintainType")
    @Dict(dicCode = "mesMaintainType")
    private String type;
    /**
     * 任务号
     */
    @Excel(name = "任务号")
    private String taskCode;
    /**
     * 任务名称
     */
    @Excel(name = "任务名称")
    private String taskName;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    @Dict(dictTable = "mes_machine",dicCode = "id",dicText = "machine_name")
    private String machineId;
    /**
     * 实际执行人
     */
    @Excel(name = "实际执行人",dicCode = "id",dicText = "realname",dictTable = "sys_user")
    @Dict(dicCode = "id",dicText = "realname",dictTable = "sys_user")
    private String realExcuter;
    /**
     * 计划执行人
     */
    @Excel(name = "计划执行人")
    private String planExcuter;
    /**
     * 计划结束时间
     */
    @Excel(name = "计划结束时间",format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String planEndTime;
    /**
     * 计划开始时间
     */
    @Excel(name = "计划开始时间",format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String planStartTime;
    /**
     * 状态
     */
    @Excel(name = "状态",dicCode = "mesRepairStatus")
    @Dict(dicCode = "mesRepairStatus")
    private String status;
}
