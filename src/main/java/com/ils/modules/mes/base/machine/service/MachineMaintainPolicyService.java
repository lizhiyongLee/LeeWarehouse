package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineMaintainPolicy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 维保策略组定义
 * @Author: Tian
 * @Date:   2020-10-27
 * @Version: V1.0
 */
public interface MachineMaintainPolicyService extends IService<MachineMaintainPolicy> {

    /**
     * 添加
     * @param machineMaintainPolicy
     */
    public void saveMachineMaintainPolicy(MachineMaintainPolicy machineMaintainPolicy) ;
    
    /**
     * 修改
     * @param machineMaintainPolicy
     */
    public void updateMachineMaintainPolicy(MachineMaintainPolicy machineMaintainPolicy);

}
