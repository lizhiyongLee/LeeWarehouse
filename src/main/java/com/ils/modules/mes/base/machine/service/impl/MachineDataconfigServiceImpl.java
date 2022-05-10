package com.ils.modules.mes.base.machine.service.impl;

import com.ils.modules.mes.base.machine.entity.MachineDataconfig;
import com.ils.modules.mes.base.machine.mapper.MachineDataconfigMapper;
import com.ils.modules.mes.base.machine.service.MachineDataconfigService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 读数配置
 * @Author: Conner
 * @Date:   2020-10-28
 * @Version: V1.0
 */
@Service
public class MachineDataconfigServiceImpl extends ServiceImpl<MachineDataconfigMapper, MachineDataconfig> implements MachineDataconfigService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineDataconfig(MachineDataconfig machineDataconfig) {
         baseMapper.insert(machineDataconfig);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineDataconfig(MachineDataconfig machineDataconfig) {
        baseMapper.updateById(machineDataconfig);
    }
    
}
