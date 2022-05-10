package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备类型关联策略组
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface MachineTypePolicyService extends IService<MachineTypePolicy> {

    /**
     * 添加
     * @param machineTypePolicy
     */
    public void saveMachineTypePolicy(MachineTypePolicy machineTypePolicy) ;
    
    /**
     * 修改
     * @param machineTypePolicy
     */
    public void updateMachineTypePolicy(MachineTypePolicy machineTypePolicy);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineTypePolicy (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineTypePolicy (List<String> idList);
}
