package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/30 14:35
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineMaintenanceTaskVO extends MachineMaintenanceTask {
    private static final long serialVersionUID = 1L;
    /** 计划执行人*/
    private String planExcuter;
    /**
     * 设备编码
     */
    private String machineCode;
}
