package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.entity.MachineLog;
import lombok.*;

import java.util.List;

/**
 * 设备看板数据
 *
 * @author Anna.
 * @date 2021/6/15 15:59
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineDashBoardVO extends Machine {
    /**
     * 设备的所有维护任务
     */
    private List<MachineRepairAndMaintenanceVO> machineRepairAndMaintenanceVOList;
    /**
     * 设备日志
     */
    private List<MachineLog> machineLogList;
}
