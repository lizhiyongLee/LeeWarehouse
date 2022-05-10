package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备关联读数
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface MachineTypeDataService extends IService<MachineTypeData> {

    /**
     * 添加
     * @param machineTypeData
     */
    public void saveMachineTypeData(MachineTypeData machineTypeData) ;
    
    /**
     * 修改
     * @param machineTypeData
     */
    public void updateMachineTypeData(MachineTypeData machineTypeData);
}
