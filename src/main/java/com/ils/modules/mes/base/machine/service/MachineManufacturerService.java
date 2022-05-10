package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineManufacturer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备制造商
 * @Author: Tian
 * @Date:   2020-10-28
 * @Version: V1.0
 */
public interface MachineManufacturerService extends IService<MachineManufacturer> {

    /**
     * 添加
     * @param machineManufacturer
     */
    public void saveMachineManufacturer(MachineManufacturer machineManufacturer) ;
    
    /**
     * 修改
     * @param machineManufacturer
     */
    public void updateMachineManufacturer(MachineManufacturer machineManufacturer);

}
