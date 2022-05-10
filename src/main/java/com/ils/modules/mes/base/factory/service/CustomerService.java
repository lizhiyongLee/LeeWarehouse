package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Customer;

/**
 * @Description: 客户
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 添加
     * @param customer
     */
    public void saveCustomer(Customer customer) ;
    
    /**
     * 修改
     * @param customer
     */
    public void updateCustomer(Customer customer);
    
    /**
     * 删除
     * @param id
     */
    public void delCustomer (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchCustomer (List<String> idList);
}
