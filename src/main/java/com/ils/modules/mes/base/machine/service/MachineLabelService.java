package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineLabel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备标签
 * @Author: Tian
 * @Date:   2020-10-27
 * @Version: V1.0
 */
public interface MachineLabelService extends IService<MachineLabel> {

    /**
     * 添加
     * @param machineLabel
     */
    public void saveMachineLabel(MachineLabel machineLabel) ;
    
    /**
     * 修改
     * @param machineLabel
     */
    public void updateMachineLabel(MachineLabel machineLabel);

}
