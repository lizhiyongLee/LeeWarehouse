package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineStopReason;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 停机原因
 * @Author: Conner
 * @Date:   2020-10-23
 * @Version: V1.0
 */
public interface MachineStopReasonService extends IService<MachineStopReason> {

    /**
     * 添加
     * @param machineStopReason
     */
    public void saveMachineStopReason(MachineStopReason machineStopReason) ;
    
    /**
     * 修改
     * @param machineStopReason
     */
    public void updateMachineStopReason(MachineStopReason machineStopReason);
}
