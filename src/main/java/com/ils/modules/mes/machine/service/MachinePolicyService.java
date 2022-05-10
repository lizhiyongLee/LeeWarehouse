package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.MachinePolicy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备关联策略
 * @Author: Tian
 * @Date:   2020-11-16
 * @Version: V1.0
 */
public interface MachinePolicyService extends IService<MachinePolicy> {

    /**
     * 添加
     * @param machinePolicy
     */
    public void saveMachinePolicy(MachinePolicy machinePolicy) ;
    
    /**
     * 修改
     * @param machinePolicy
     */
    public void updateMachinePolicy(MachinePolicy machinePolicy);
    
    /**
     * 删除
     * @param id
     */
    public void delMachinePolicy (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachinePolicy (List<String> idList);
}
