package com.ils.modules.mes.base.qc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.qc.entity.NgItem;
import com.ils.modules.mes.base.qc.mapper.NgItemMapper;
import com.ils.modules.mes.base.qc.service.NgItemService;
import com.ils.modules.mes.base.qc.vo.NgItemVO;

/**
 * @Description: 不良类型原因
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Service
public class NgItemServiceImpl extends ServiceImpl<NgItemMapper, NgItem> implements NgItemService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNgItem(NgItem ngItem) {
         baseMapper.insert(ngItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateNgItem(NgItem ngItem) {
        baseMapper.updateById(ngItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delNgItem(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchNgItem(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage<NgItemVO> queryPageList(IPage<NgItemVO> page, QueryWrapper<NgItemVO> queryWrapper) {
        return baseMapper.queryPageList(page, queryWrapper);
    }
}
