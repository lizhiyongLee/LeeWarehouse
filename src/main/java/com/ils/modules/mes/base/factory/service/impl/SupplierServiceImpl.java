package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.Supplier;
import com.ils.modules.mes.base.factory.mapper.SupplierMapper;
import com.ils.modules.mes.base.factory.service.SupplierService;

/**
 * @Description: 供应商
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSupplier(Supplier supplier) {
         baseMapper.insert(supplier);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSupplier(Supplier supplier) {
        baseMapper.updateById(supplier);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSupplier(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSupplier(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
