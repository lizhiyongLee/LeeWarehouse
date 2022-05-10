package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.mapper.UnitMapper;
import com.ils.modules.mes.base.factory.service.UnitService;

/**
 * @Description: 计量单位
 * @Author: fengyi
 * @Date: 2020-10-16
 * @Version: V1.0
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveUnit(Unit unit) {
         baseMapper.insert(unit);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateUnit(Unit unit) {
        baseMapper.updateById(unit);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delUnit(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchUnit(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
