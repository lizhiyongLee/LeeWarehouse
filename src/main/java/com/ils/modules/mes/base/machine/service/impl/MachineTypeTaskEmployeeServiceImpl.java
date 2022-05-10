package com.ils.modules.mes.base.machine.service.impl;

import com.ils.modules.mes.base.machine.entity.MachineTypeTaskEmployee;
import com.ils.modules.mes.base.machine.mapper.MachineTypeTaskEmployeeMapper;
import com.ils.modules.mes.base.machine.service.MachineTypeTaskEmployeeService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 设备类型关联策略组
 * @Author: Conner
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Service
public class MachineTypeTaskEmployeeServiceImpl extends ServiceImpl<MachineTypeTaskEmployeeMapper, MachineTypeTaskEmployee> implements MachineTypeTaskEmployeeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineTypeTaskEmployee(MachineTypeTaskEmployee machineTypeTaskEmployee) {
         baseMapper.insert(machineTypeTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineTypeTaskEmployee(MachineTypeTaskEmployee machineTypeTaskEmployee) {
        baseMapper.updateById(machineTypeTaskEmployee);
    }

}
