package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.Shift;
import com.ils.modules.mes.base.factory.mapper.ShiftMapper;
import com.ils.modules.mes.base.factory.service.ShiftService;

/**
 * @Description: 班次
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Service
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveShift(Shift shift) {
         baseMapper.insert(shift);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateShift(Shift shift) {
        baseMapper.updateById(shift);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delShift(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchShift(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
