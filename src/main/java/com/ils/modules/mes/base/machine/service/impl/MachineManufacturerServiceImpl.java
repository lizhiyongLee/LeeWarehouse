package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.machine.entity.MachineManufacturer;
import com.ils.modules.mes.base.machine.mapper.MachineManufacturerMapper;
import com.ils.modules.mes.base.machine.service.MachineManufacturerService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.mapper.MachineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 设备制造商
 * @Author: Conner
 * @Date:   2020-10-28
 * @Version: V1.0
 */
@Service
public class MachineManufacturerServiceImpl extends ServiceImpl<MachineManufacturerMapper, MachineManufacturer> implements MachineManufacturerService {


    @Autowired
    @Lazy
    private MachineMapper machineMapper;
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineManufacturer(MachineManufacturer machineManufacturer) {
         baseMapper.insert(machineManufacturer);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineManufacturer(MachineManufacturer machineManufacturer) {
        if(!this.checkUpdateStatusCondition(machineManufacturer)){
            throw new ILSBootException("B-FCT-0004");
        }
        baseMapper.updateById(machineManufacturer);
    }


    private boolean checkUpdateStatusCondition(MachineManufacturer machineManufacturer){
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(machineManufacturer.getStatus())){
            QueryWrapper<Machine> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("manufacturer_id",machineManufacturer.getId());
            queryWrapper.eq("status",ZeroOrOneEnum.ONE.getStrCode());
            Integer count = machineMapper.selectCount(queryWrapper);
            return count == 0 ? true : false;
        }
        return true;
    }
}
