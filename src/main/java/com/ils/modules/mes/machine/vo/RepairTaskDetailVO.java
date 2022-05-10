package com.ils.modules.mes.machine.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import com.ils.modules.mes.machine.entity.MachineTaskLog;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * TODO 类描述
 * 维修任务详情VO类
 * @author Anna.
 * @date 2020/11/25 12:01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RepairTaskDetailVO extends MachineRepairTask {
    private static final long serialVersionUID = 1L;
    /**设备维修日志 */
    private List<MachineTaskLog> machineTaskLogList;
    /**  目标类别 */
    @Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    private String labelType;
    /**  设备名称 */
    private String machineName;
    /**设备类型 */
    @Dict(dictTable = "mes_machine_type", dicCode = "id", dicText = "type_name")
    private String machineTypeId;
    /** 设备编码*/
    private String machineCode;
    /** d电子标签*/
    private String qrcode;
    /** 车间*/
    @Dict(dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
    private String workshopId;
    /** 工位*/
    private String workStation;
    /** 任务类型 */
    @Dict(dicCode = "mesMaintainType")
    private String taskType;
    /** 首次启用日期*/
    private Date firstTurnOnDate;
    /** 计划执行人*/
    private String planExcuter;
    /**实际执行人名字 */
    private String realExcuterName;
}
