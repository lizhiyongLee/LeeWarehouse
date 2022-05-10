package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.machine.entity.SparePartsHouse;
import com.ils.modules.mes.base.machine.mapper.SparePartsHouseMapper;
import com.ils.modules.mes.base.machine.service.SparePartsHouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 备件库
 * @Author: Conner
 * @Date:   2021-02-23
 * @Version: V1.0
 */
@Service
public class SparePartsHouseServiceImpl extends ServiceImpl<SparePartsHouseMapper, SparePartsHouse> implements SparePartsHouseService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSparePartsHouse(SparePartsHouse sparePartsHouse) {
         baseMapper.insert(sparePartsHouse);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSparePartsHouse(SparePartsHouse sparePartsHouse) {
        baseMapper.updateById(sparePartsHouse);
    }

}
