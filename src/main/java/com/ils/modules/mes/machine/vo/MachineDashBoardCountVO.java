package com.ils.modules.mes.machine.vo;

import lombok.*;

/**
 * 设备看板统计数据
 *
 * @author Anna.
 * @date 2021/6/15 15:32
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineDashBoardCountVO {
    /**
     * 设备总数
     */
    private Integer machineCount;
    /**
     * 待维修设备数
     */
    private Integer machineRepairCount;
    /**
     * 待保养设备数
     */
    private Integer machineMaintenanceCount;
    /**
     * 待点检设备数
     */
    private Integer machineCheckCount;
}
