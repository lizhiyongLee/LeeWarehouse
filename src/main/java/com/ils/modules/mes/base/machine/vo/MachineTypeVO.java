package com.ils.modules.mes.base.machine.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import lombok.*;

import java.util.List;

/**
 * 设备类型详情
 *
 * @author Anna.
 * @date 2020/10/30 16:32
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineTypeVO extends MachineType {
    /**
     * 读数
     */
    @ExcelCollection(name = "设备类型关联读数")
    private List<MachineTypeData> machineTypeDataList;
    /**
     * 策略
     */
    @ExcelCollection(name = "设备类型关联策略")
    private List<MachineTypePolicyVO> machineTypePolicyVOList;
    /**
     * 参数数据
     */
    private List<MachineTypeParaVO> machineTypeParaVOList;
}
