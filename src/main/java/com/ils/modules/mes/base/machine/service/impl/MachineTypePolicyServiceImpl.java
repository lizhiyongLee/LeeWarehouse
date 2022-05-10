package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.ils.modules.mes.base.machine.mapper.MachineTypeMapper;
import com.ils.modules.mes.base.machine.mapper.MachineTypePolicyMapper;
import com.ils.modules.mes.base.machine.service.MachineTypePolicyService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class MachineTypePolicyServiceImpl extends ServiceImpl<MachineTypePolicyMapper, MachineTypePolicy> implements MachineTypePolicyService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineTypePolicy(MachineTypePolicy machineTypePolicy) {
         baseMapper.insert(machineTypePolicy);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineTypePolicy(MachineTypePolicy machineTypePolicy) {
        if (true){
            throw new ILSBootException("");
        }
        baseMapper.updateById(machineTypePolicy);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineTypePolicy(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineTypePolicy(List<String> idList) {

        baseMapper.deleteBatchIds(idList);
    }


}
