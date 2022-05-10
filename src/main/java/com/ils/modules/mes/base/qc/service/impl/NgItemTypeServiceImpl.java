package com.ils.modules.mes.base.qc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.qc.entity.NgItemType;
import com.ils.modules.mes.base.qc.mapper.NgItemTypeMapper;
import com.ils.modules.mes.base.qc.service.NgItemTypeService;

/**
 * @Description: 不良类型
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Service
public class NgItemTypeServiceImpl extends ServiceImpl<NgItemTypeMapper, NgItemType> implements NgItemTypeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNgItemType(NgItemType ngItemType) {
         baseMapper.insert(ngItemType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateNgItemType(NgItemType ngItemType) {
        baseMapper.updateById(ngItemType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delNgItemType(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchNgItemType(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
