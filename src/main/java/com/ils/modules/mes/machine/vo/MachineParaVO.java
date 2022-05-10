package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.MachineParaDetail;
import com.ils.modules.mes.machine.entity.MachineParaHead;
import lombok.Data;

import java.util.List;

/**
 * 设备参数相关数据
 *
 * @author Anna.
 * @date 2021/10/19 17:33
 */
@Data
public class MachineParaVO extends MachineParaHead {
    /**
     * 设备参数详情项
     */
    private List<MachineParaDetail> machineParaDetailList;
}
