package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.MachineRepairTask;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/25 15:55
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RepairTaskWithEmployeeVO extends MachineRepairTask {
    private static final long serialVersionUID = 1L;
    /** 计划维修人员Id*/
    private String rapairTaskEmployeeId;
}
