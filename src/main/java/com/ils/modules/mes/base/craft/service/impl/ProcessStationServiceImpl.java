package com.ils.modules.mes.base.craft.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ProcessStation;
import com.ils.modules.mes.base.craft.mapper.ProcessStationMapper;
import com.ils.modules.mes.base.craft.service.ProcessStationService;

/**
 * @Description: 工序关联工位
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Service
public class ProcessStationServiceImpl extends ServiceImpl<ProcessStationMapper, ProcessStation> implements ProcessStationService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveProcessStation(ProcessStation processStation) {
         baseMapper.insert(processStation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateProcessStation(ProcessStation processStation) {
        baseMapper.updateById(processStation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delProcessStation(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchProcessStation(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
