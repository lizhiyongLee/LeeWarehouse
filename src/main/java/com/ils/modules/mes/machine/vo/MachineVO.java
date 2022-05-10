package com.ils.modules.mes.machine.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.machine.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备vo类
 *
 * @author Anna.
 * @date 2020/11/16 18:10
 */
@Data
public class MachineVO extends Machine {
    private static final long serialVersionUID = 1L;
    /** 维护占用 */
    private List<MachineRepairAndMaintenanceVO> machineRepairAndMaintenanceVOList;
    /** 维护策略 */
    @ExcelCollection(name = "维护策略",type = ArrayList.class,orderNum="19")
    private List<MachinePolicy> machinePolicyList;
    /** 设备停机 */
    private List<MachineStopTime> machineStopTimeList;
    /** 设备日志 */
    private List<MachineLog> machineLogList;
    /** 设备关联读数 */
    @ExcelCollection(name = "设备读数",type = ArrayList.class,orderNum="20")
    private List<MachineData> machineDataList;
    /** 设备关联工位 */
    private List<Workstation> workstationList;
    /** 设备参数相关数据*/
    private List<MachineParaVO> machineParaVOList;
}
