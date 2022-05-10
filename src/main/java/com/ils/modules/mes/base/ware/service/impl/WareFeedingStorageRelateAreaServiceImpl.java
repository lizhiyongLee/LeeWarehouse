package com.ils.modules.mes.base.ware.service.impl;

import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.mapper.WareFeedingStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.service.WareFeedingStorageRelateAreaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 投料仓位
 * @Author: Conner
 * @Date:   2020-12-04
 * @Version: V1.0
 */
@Service
public class WareFeedingStorageRelateAreaServiceImpl extends ServiceImpl<WareFeedingStorageRelateAreaMapper, WareFeedingStorageRelateArea> implements WareFeedingStorageRelateAreaService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWareFeedingStorageRelateArea(WareFeedingStorageRelateArea wareFeedingStorageRelateArea) {
         baseMapper.insert(wareFeedingStorageRelateArea);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWareFeedingStorageRelateArea(WareFeedingStorageRelateArea wareFeedingStorageRelateArea) {
        baseMapper.updateById(wareFeedingStorageRelateArea);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWareFeedingStorageRelateArea(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWareFeedingStorageRelateArea(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
