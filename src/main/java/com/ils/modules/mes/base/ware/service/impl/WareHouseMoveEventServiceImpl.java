package com.ils.modules.mes.base.ware.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.ware.entity.WareHouseMoveEvent;
import com.ils.modules.mes.base.ware.mapper.WareHouseMoveEventMapper;
import com.ils.modules.mes.base.ware.service.WareHouseMoveEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 仓库移动事务
 * @Author: Conner
 * @Date: 2021-02-08
 * @Version: V1.0
 */
@Service
public class WareHouseMoveEventServiceImpl extends ServiceImpl<WareHouseMoveEventMapper, WareHouseMoveEvent> implements WareHouseMoveEventService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWareHouseMoveEvent(WareHouseMoveEvent wareHouseMoveEvent) {
        baseMapper.insert(wareHouseMoveEvent);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWareHouseMoveEvent(WareHouseMoveEvent wareHouseMoveEvent) {
        baseMapper.updateById(wareHouseMoveEvent);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWareHouseMoveEvent(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWareHouseMoveEvent(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
