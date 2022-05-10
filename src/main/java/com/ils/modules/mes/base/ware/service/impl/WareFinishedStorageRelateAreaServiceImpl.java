package com.ils.modules.mes.base.ware.service.impl;

import com.ils.modules.mes.base.ware.entity.WareFinishedStorageRelateArea;
import com.ils.modules.mes.base.ware.mapper.WareFinishedStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.service.WareFinishedStorageRelateAreaService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 完工仓位
 * @Author: Conner
 * @Date:   2020-12-04
 * @Version: V1.0
 */
@Service
public class WareFinishedStorageRelateAreaServiceImpl extends ServiceImpl<WareFinishedStorageRelateAreaMapper, WareFinishedStorageRelateArea> implements WareFinishedStorageRelateAreaService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWareFinishedStorageRelateArea(WareFinishedStorageRelateArea wareFinishedStorageRelateArea) {
         baseMapper.insert(wareFinishedStorageRelateArea);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWareFinishedStorageRelateArea(WareFinishedStorageRelateArea wareFinishedStorageRelateArea) {
        baseMapper.updateById(wareFinishedStorageRelateArea);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWareFinishedStorageRelateArea(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWareFinishedStorageRelateArea(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
