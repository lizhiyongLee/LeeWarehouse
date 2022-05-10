package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.machine.entity.MachineRepairTaskEmployee;
import com.ils.modules.mes.machine.mapper.MachineRepairTaskEmployeeMapper;
import com.ils.modules.mes.machine.service.MachineRepairTaskEmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 维修任务计划执行人
 * @Author: Hhuangtao
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineRepairTaskEmployeeServiceImpl extends ServiceImpl<MachineRepairTaskEmployeeMapper, MachineRepairTaskEmployee> implements MachineRepairTaskEmployeeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineRepairTaskEmployee(MachineRepairTaskEmployee machineRepairTaskEmployee) {
         baseMapper.insert(machineRepairTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineRepairTaskEmployee(MachineRepairTaskEmployee machineRepairTaskEmployee) {
        baseMapper.updateById(machineRepairTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineRepairTaskEmployee(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineRepairTaskEmployee(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
