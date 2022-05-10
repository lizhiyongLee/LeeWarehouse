package com.ils.modules.mes.machine.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/26 13:18
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MaintenanceTaskVO extends MachineMaintenanceTask {
    private static final long serialVersionUID = 1L;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    private String machineName;
    /**
     * 设备编码
     */
    @Excel(name = "设备编码", width = 15)
    private String machineCode;
    /**
     * 设备类型
     */
    @Excel(name = "设备类型", width = 15,dictTable = "mes_machine_type", dicCode = "id", dicText = "type_name")
    @Dict(dictTable = "mes_machine_type", dicCode = "id", dicText = "type_name")
    private String machineTypeId;
    /**
     * 设备标注
     */
    @Excel(name = "设备标注", width = 15,dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    @Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    private String labelId;
    /**
     * 设备等级
     */
    @Excel(name = "设备等级", width = 15,dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    @Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    private String levelId;
    /**
     * 车间
     */
    @Excel(name = "车间", width = 15,dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
    @Dict(dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
    private String workShopId;
    /**
     * 工位
     */
    @Excel(name = "工位", width = 15,dictTable = "mes_workstation", dicText = "station_name", dicCode = "id")
    @Dict(dictTable = "mes_workstation", dicText = "station_name", dicCode = "id")
    private String workStationId;
    /**
     * 策略方案
     */
    @Excel(name = "策略方案", width = 15,dicCode = "mesPolicyRule")
    @Dict(dicCode = "mesPolicyRule")
    private String policyRule;
    /**
     * 计划执行人员
     */
    @Excel(name = "计划执行人员", width = 15)
    private String employeeName;
}
