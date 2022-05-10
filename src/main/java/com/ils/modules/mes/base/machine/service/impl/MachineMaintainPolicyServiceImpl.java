package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.machine.entity.MachineMaintainPolicy;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.ils.modules.mes.base.machine.mapper.MachineMaintainPolicyMapper;
import com.ils.modules.mes.base.machine.mapper.MachineTypePolicyMapper;
import com.ils.modules.mes.base.machine.service.MachineMaintainPolicyService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 维保策略组定义
 * @Author: Conner
 * @Date:   2020-10-27
 * @Version: V1.0
 */
@Service
public class MachineMaintainPolicyServiceImpl extends ServiceImpl<MachineMaintainPolicyMapper, MachineMaintainPolicy> implements MachineMaintainPolicyService {

    @Autowired
    @Lazy
    private MachineTypePolicyMapper machineTypePolicyMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineMaintainPolicy(MachineMaintainPolicy machineMaintainPolicy) {
         baseMapper.insert(machineMaintainPolicy);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineMaintainPolicy(MachineMaintainPolicy machineMaintainPolicy) {
        if (!this.checkUpdateStatusCondition(machineMaintainPolicy)){
            throw new ILSBootException("B-FCT-0005");
        }
        baseMapper.updateById(machineMaintainPolicy);
    }


    /**
     * 维保策略定义状态为关闭时判断设备关联策略的状态是否为关闭
     * @param machineMaintainPolicy
     * @return
     */
    public boolean checkUpdateStatusCondition(MachineMaintainPolicy machineMaintainPolicy){
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(machineMaintainPolicy.getStatus())){
            QueryWrapper<MachineTypePolicy> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("policy_group_id",machineMaintainPolicy.getId());
            queryWrapper.eq("status",ZeroOrOneEnum.ONE.getStrCode());
            Integer count = machineTypePolicyMapper.selectCount(queryWrapper);
            return count == 0 ? true:false;
        }

        return  true;
    }
}
