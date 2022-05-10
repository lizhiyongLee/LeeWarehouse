package com.ils.modules.mes.machine.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/23 15:48
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineRepairVO extends MachineRepairTask {
    private static final long serialVersionUID = 1L;
    /** 设备名称 */
    @Excel(name = "设备名称",width = 15)
    private String machineName;
    /** 设备编码 */
    @Excel(name = "设备编码",width = 15)
    private String machineCode;
    /** 设备类型 */
    @Excel(name = "设备类型",width = 15)
    private String machineTypeName;
    /** 车间 */
    @Excel(name = "车间",width = 15)
    private String workShopName;
    /** 工位 */
    @Excel(name = "工位",width = 15)
    private String workStationName;
    /** 设备标注 */
    @Excel(name = "设备标注",width = 15)
    private String labelName;
    /** 设备等级 */
    @Excel(name = "设备等级",width = 15)
    private String levelName;
    /** 计划执行人员 */
    @Excel(name = "计划执行人员",width = 15)
    private String employeeName;
    /**实际执行人名字 */
    @Excel(name = "实际执行人名字",width = 15)
    private String realExcuterName;
}
