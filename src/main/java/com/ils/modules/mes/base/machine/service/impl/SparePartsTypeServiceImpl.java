package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.machine.entity.SparePartsType;
import com.ils.modules.mes.base.machine.mapper.SparePartsTypeMapper;
import com.ils.modules.mes.base.machine.service.SparePartsTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 备件类型
 * @Author: Conner
 * @Date:   2021-02-23
 * @Version: V1.0
 */
@Service
public class SparePartsTypeServiceImpl extends ServiceImpl<SparePartsTypeMapper, SparePartsType> implements SparePartsTypeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSparePartsType(SparePartsType sparePartsType) {
         baseMapper.insert(sparePartsType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSparePartsType(SparePartsType sparePartsType) {
        baseMapper.updateById(sparePartsType);
    }
}
