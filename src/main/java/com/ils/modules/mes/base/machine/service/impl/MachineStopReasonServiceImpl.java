package com.ils.modules.mes.base.machine.service.impl;

import com.ils.modules.mes.base.machine.entity.MachineStopReason;
import com.ils.modules.mes.base.machine.mapper.MachineStopReasonMapper;
import com.ils.modules.mes.base.machine.service.MachineStopReasonService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 停机原因
 * @Author: Conner
 * @Date:   2020-10-23
 * @Version: V1.0
 */
@Service
public class MachineStopReasonServiceImpl extends ServiceImpl<MachineStopReasonMapper, MachineStopReason> implements MachineStopReasonService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineStopReason(MachineStopReason machineStopReason) {
         baseMapper.insert(machineStopReason);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineStopReason(MachineStopReason machineStopReason) {
        baseMapper.updateById(machineStopReason);
    }


}
