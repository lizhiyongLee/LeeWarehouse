package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.machine.entity.MachineData;
import com.ils.modules.mes.machine.mapper.MachineDataMapper;
import com.ils.modules.mes.machine.service.MachineDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 设备类型关联读数
 * @Author: Hhuangtao
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineDataServiceImpl extends ServiceImpl<MachineDataMapper, MachineData> implements MachineDataService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineData(MachineData machineData) {
        baseMapper.insert(machineData);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineData(MachineData machineData) {
        baseMapper.updateById(machineData);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineData(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineData(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
