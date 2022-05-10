package com.ils.modules.mes.base.qc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.qc.entity.QcItem;
import com.ils.modules.mes.base.qc.mapper.QcItemMapper;
import com.ils.modules.mes.base.qc.service.QcItemService;
import com.ils.modules.mes.base.qc.vo.QcItemVO;

/**
 * @Description: 质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Service
public class QcItemServiceImpl extends ServiceImpl<QcItemMapper, QcItem> implements QcItemService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcItem(QcItem qcItem) {
         baseMapper.insert(qcItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcItem(QcItem qcItem) {
        baseMapper.updateById(qcItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcItem(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcItem(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage<QcItemVO> queryPageList(IPage<QcItemVO> page, QueryWrapper<QcItemVO> queryWrapper) {
        return baseMapper.queryPageList(page, queryWrapper);
    }
}
