package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineDataconfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 读数配置
 * @Author: Tian
 * @Date:   2020-10-28
 * @Version: V1.0
 */
public interface MachineDataconfigService extends IService<MachineDataconfig> {

    /**
     * 添加
     * @param machineDataconfig
     */
    public void saveMachineDataconfig(MachineDataconfig machineDataconfig) ;
    
    /**
     * 修改
     * @param machineDataconfig
     */
    public void updateMachineDataconfig(MachineDataconfig machineDataconfig);

}
