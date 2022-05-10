package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.machine.entity.MachinePolicy;
import com.ils.modules.mes.machine.mapper.MachinePolicyMapper;
import com.ils.modules.mes.machine.service.MachinePolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 设备关联策略
 * @Author: Hhuangtao
 * @Date:   2020-11-16
 * @Version: V1.0
 */
@Service
public class MachinePolicyServiceImpl extends ServiceImpl<MachinePolicyMapper, MachinePolicy> implements MachinePolicyService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachinePolicy(MachinePolicy machinePolicy) {
         baseMapper.insert(machinePolicy);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachinePolicy(MachinePolicy machinePolicy) {
        baseMapper.updateById(machinePolicy);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachinePolicy(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachinePolicy(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
