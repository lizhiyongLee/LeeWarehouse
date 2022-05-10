package com.ils.modules.mes.machine.service.impl;

import com.ils.modules.mes.machine.entity.MachineMaintenanceTaskEmployee;
import com.ils.modules.mes.machine.mapper.MachineMaintenanceTaskEmployeeMapper;
import com.ils.modules.mes.machine.service.MachineMaintenanceTaskEmployeeService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 维保任务计划执行人
 * @Author: Hhuangtao
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineMaintenanceTaskEmployeeServiceImpl extends ServiceImpl<MachineMaintenanceTaskEmployeeMapper, MachineMaintenanceTaskEmployee> implements MachineMaintenanceTaskEmployeeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineMaintenanceTaskEmployee(MachineMaintenanceTaskEmployee machineMaintenanceTaskEmployee) {
         baseMapper.insert(machineMaintenanceTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineMaintenanceTaskEmployee(MachineMaintenanceTaskEmployee machineMaintenanceTaskEmployee) {
        baseMapper.updateById(machineMaintenanceTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineMaintenanceTaskEmployee(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineMaintenanceTaskEmployee(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
