package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.Customer;
import com.ils.modules.mes.base.factory.mapper.CustomerMapper;
import com.ils.modules.mes.base.factory.service.CustomerService;

/**
 * @Description: 客户
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveCustomer(Customer customer) {
         baseMapper.insert(customer);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateCustomer(Customer customer) {
        baseMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delCustomer(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchCustomer(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
