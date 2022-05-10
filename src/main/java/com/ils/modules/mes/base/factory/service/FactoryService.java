package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Factory;
import com.ils.modules.mes.base.factory.vo.FactoryVO;

/**
 * @Description: 工厂
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
public interface FactoryService extends IService<Factory> {

    /**
     * 添加
     * 
     * @param factoryVO
     */
    public void saveFactory(FactoryVO factoryVO);
    
    /**
     * 修改
     * 
     * @param factoryVO
     */
    public void updateFactory(FactoryVO factoryVO);
    
    /**
     * 删除
     * @param id
     */
    public void delFactory (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchFactory (List<String> idList);
}
