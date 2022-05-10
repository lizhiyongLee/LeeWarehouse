package com.ils.modules.mes.base.machine.service.impl;

import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.modules.mes.base.machine.mapper.MachineTypeDataMapper;
import com.ils.modules.mes.base.machine.service.MachineTypeDataService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 设备关联读数
 * @Author: Conner
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Service
public class MachineTypeDataServiceImpl extends ServiceImpl<MachineTypeDataMapper, MachineTypeData> implements MachineTypeDataService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineTypeData(MachineTypeData machineTypeData) {
         baseMapper.insert(machineTypeData);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineTypeData(MachineTypeData machineTypeData) {
        baseMapper.updateById(machineTypeData);
    }
}
