package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/28 11:58
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MaintenancePageListWithPhoneVO extends MachineMaintenanceTask {
    private static final long serialVersionUID = 1L;
    /**
     * 设备名称
     */
    private String machineName;
    /**
     * 设备编码
     */
    private String machineCode;
    /**
     * 计划执行人
     */
    private String planExcuter;
    /**
     * 实际执行人
     */
    private String realExcuterName;
}
