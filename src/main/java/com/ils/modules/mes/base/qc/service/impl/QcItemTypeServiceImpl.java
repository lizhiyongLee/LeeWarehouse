package com.ils.modules.mes.base.qc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.qc.entity.QcItemType;
import com.ils.modules.mes.base.qc.mapper.QcItemTypeMapper;
import com.ils.modules.mes.base.qc.service.QcItemTypeService;

/**
 * @Description: 质检项分类
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Service
public class QcItemTypeServiceImpl extends ServiceImpl<QcItemTypeMapper, QcItemType> implements QcItemTypeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcItemType(QcItemType qcItemType) {
         baseMapper.insert(qcItemType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcItemType(QcItemType qcItemType) {
        baseMapper.updateById(qcItemType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcItemType(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcItemType(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
