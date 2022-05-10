package com.ils.modules.mes.base.machine.service.impl;

import com.ils.modules.mes.base.machine.entity.MachineLabel;
import com.ils.modules.mes.base.machine.mapper.MachineLabelMapper;
import com.ils.modules.mes.base.machine.service.MachineLabelService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 设备标签
 * @Author: Conner
 * @Date:   2020-10-27
 * @Version: V1.0
 */
@Service
public class MachineLabelServiceImpl extends ServiceImpl<MachineLabelMapper, MachineLabel> implements MachineLabelService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineLabel(MachineLabel machineLabel) {
         baseMapper.insert(machineLabel);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineLabel(MachineLabel machineLabel) {
        baseMapper.updateById(machineLabel);
    }

}
