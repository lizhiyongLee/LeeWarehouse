package com.ils.modules.mes.machine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.machine.entity.MachineStopTime;
import com.ils.modules.mes.machine.mapper.MachineStopTimeMapper;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.service.MachineStopTimeService;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: 设备关联停机时间
 * @Author: Hhuangtao
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineStopTimeServiceImpl extends ServiceImpl<MachineStopTimeMapper, MachineStopTime> implements MachineStopTimeService {

    @Autowired
    private MachineService machineService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineStopTime(MachineStopTime machineStopTime) {
        this.checkCondition(machineStopTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String description = "";
        if (MesCommonConstant.MACHINE_PLAN_STOP_TIME_NUMBER.equals(machineStopTime.getStopType())) {
            description = MesCommonConstant.MACHINE_PLAN_STOP_TIME;
        } else if (MesCommonConstant.MACHINE_REAL_STOP_TIME_NUMBER.equals(machineStopTime.getStopType())) {
            description = MesCommonConstant.MACHINE_REAL_STOP_TIME;
        }
        machineService.addMachineLog(machineStopTime.getMachineId(), MesCommonConstant.MACHINE_TYPE_ADD,
                "新增" + description + df.format(machineStopTime.getStartTime()) + "~" + df.format(machineStopTime.getEndTime()));
        baseMapper.insert(machineStopTime);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineStopTime(MachineStopTime machineStopTime) {
        baseMapper.updateById(machineStopTime);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineStopTime(String id) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MachineStopTime machineStopTime = baseMapper.selectById(id);

        machineService.addMachineLog(machineStopTime.getMachineId(), MesCommonConstant.MACHINE_TYPE_DELETE,
                "删除停机" + df.format(machineStopTime.getStartTime()) + "~" + df.format(machineStopTime.getEndTime()));
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineStopTime(List<String> idList) {
        MachineStopTime machineStopTime = baseMapper.selectById(idList.get(0));
        machineService.addMachineLog(machineStopTime.getMachineId(), MesCommonConstant.MACHINE_TYPE_DELETE, "批量删除" + MesCommonConstant.MACHINE_PLAN_STOP_TIME);
        baseMapper.deleteBatchIds(idList);
    }

    private void checkCondition(MachineStopTime checkMachineStopTime) {
        boolean selfTime = checkMachineStopTime.getEndTime().compareTo(checkMachineStopTime.getStartTime()) < 0;
        if (selfTime) {
            throw new ILSBootException("停机开始时间不能小于结束时间！");
        }
        List<MachineStopTime> machineStopTimeList = baseMapper.selectByMainId(checkMachineStopTime.getMachineId());
        if (!CommonUtil.isEmptyOrNull(machineStopTimeList)) {
            machineStopTimeList.forEach(machineStopTime -> {

                boolean startTime = checkMachineStopTime.getEndTime().compareTo(machineStopTime.getStartTime()) > 0 && checkMachineStopTime.getEndTime().compareTo(machineStopTime.getEndTime()) < 0;
                boolean endTime = checkMachineStopTime.getStartTime().compareTo(machineStopTime.getEndTime()) < 0 && checkMachineStopTime.getStartTime().compareTo(machineStopTime.getStartTime()) > 0;

                if (startTime || endTime) {
                    throw new ILSBootException("新增停机时间不能与已存在的停机时间重合！");
                }
            });
        }

    }
}
