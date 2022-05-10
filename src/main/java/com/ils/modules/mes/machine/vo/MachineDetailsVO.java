package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.*;
import lombok.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/23 14:06
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineDetailsVO extends Machine {

    private static final long serialVersionUID = 1L;
    /**
     * 维护占用
     */
    private List<MachineRepairAndMaintenanceVO> machineRepairAndMaintenanceVOList;
    /**
     * 维护策略
     */
    private List<MachinePolicy> machinePolicyList;
    /**
     * 设备停机
     */
    private List<MachineStopTime> machineStopTimeList;
    /**
     * 设备日志
     */
    private List<MachineLog> machineLogList;
    /**
     * 设备关联读数
     */
    private List<MachineData> machineDataList;
}
