package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Supplier;

/**
 * @Description: 供应商
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * 添加
     * @param supplier
     */
    public void saveSupplier(Supplier supplier) ;
    
    /**
     * 修改
     * @param supplier
     */
    public void updateSupplier(Supplier supplier);
    
    /**
     * 删除
     * @param id
     */
    public void delSupplier (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSupplier (List<String> idList);
}
