package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineTypeTaskEmployee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备类型关联策略组
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface MachineTypeTaskEmployeeService extends IService<MachineTypeTaskEmployee> {

    /**
     * 添加
     * @param machineTypeTaskEmployee
     */
    public void saveMachineTypeTaskEmployee(MachineTypeTaskEmployee machineTypeTaskEmployee) ;
    
    /**
     * 修改
     * @param machineTypeTaskEmployee
     */
    public void updateMachineTypeTaskEmployee(MachineTypeTaskEmployee machineTypeTaskEmployee);

}
