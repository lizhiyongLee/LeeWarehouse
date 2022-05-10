package com.ils.modules.mes.base.machine.vo;

import com.ils.modules.mes.base.machine.entity.MachineTypeParaDetail;
import com.ils.modules.mes.base.machine.entity.MachineTypeParaHead;
import lombok.Data;

import java.util.List;

/**
 * 设备类型参数vo类
 *
 * @author Anna.
 * @date 2021/10/15 17:34
 */
@Data
public class MachineTypeParaVO extends MachineTypeParaHead {
    /**
     * 设备类型管关联参数模板详情
     */
    private List<MachineTypeParaDetail> machineTypeParaDetailList;
}
