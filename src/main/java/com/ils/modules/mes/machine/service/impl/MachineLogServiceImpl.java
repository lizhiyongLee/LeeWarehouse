package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.MachineLog;
import com.ils.modules.mes.machine.mapper.MachineLogMapper;
import com.ils.modules.mes.machine.service.MachineLogService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 设备日志
 * @Author: Hhuangtao
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineLogServiceImpl extends ServiceImpl<MachineLogMapper, MachineLog> implements MachineLogService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineLog(MachineLog machineLog) {
         baseMapper.insert(machineLog);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineLog(MachineLog machineLog) {
        baseMapper.updateById(machineLog);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineLog(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineLog(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<MachineLog> queryByMachineId(String machineId) {
        QueryWrapper<MachineLog> machineLogQueryWrapper = new QueryWrapper<>();
        machineLogQueryWrapper.eq("machine_id",machineId);
        machineLogQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineLog> machineLogList = baseMapper.selectList(machineLogQueryWrapper);
        return machineLogList;
    }
}
