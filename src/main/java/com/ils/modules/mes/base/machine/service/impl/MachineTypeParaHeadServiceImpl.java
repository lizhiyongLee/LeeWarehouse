package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ProcessParaHead;
import com.ils.modules.mes.base.machine.entity.MachineTypeParaDetail;
import com.ils.modules.mes.base.machine.entity.MachineTypeParaHead;
import com.ils.modules.mes.base.machine.mapper.MachineTypeParaDetailMapper;
import com.ils.modules.mes.base.machine.mapper.MachineTypeParaHeadMapper;
import com.ils.modules.mes.base.machine.service.MachineTypeParaHeadService;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 设备类型关联参数主表
 * @Author: Conner
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Service
public class MachineTypeParaHeadServiceImpl extends ServiceImpl<MachineTypeParaHeadMapper, MachineTypeParaHead> implements MachineTypeParaHeadService {

    @Autowired
   private MachineTypeParaDetailMapper machineTypeParaDetailMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteByMachineTypeId(String machineTypeId) {
        List<String> ids = new ArrayList<>();
        //删除主从表
        QueryWrapper<MachineTypeParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("machine_type_id", machineTypeId);
        List<MachineTypeParaHead> machineTypeParaHeadList = baseMapper.selectList(headQueryWrapper);
        machineTypeParaHeadList.forEach(machineTypeParaHead -> ids.add(machineTypeParaHead.getId()));
        baseMapper.deleteByMainId(machineTypeId);
        if (!CommonUtil.isEmptyOrNull(ids)) {
            ids.forEach(id->machineTypeParaDetailMapper.deleteByMainId(id));
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(String id){
        machineTypeParaDetailMapper.deleteByMainId(id);
        baseMapper.deleteById(id);
    }
}