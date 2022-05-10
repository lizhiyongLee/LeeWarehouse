package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Unit;

/**
 * @Description: 计量单位
 * @Author: fengyi
 * @Date: 2020-10-16
 * @Version: V1.0
 */
public interface UnitService extends IService<Unit> {

    /**
     * 添加
     * @param unit
     */
    public void saveUnit(Unit unit) ;
    
    /**
     * 修改
     * @param unit
     */
    public void updateUnit(Unit unit);
    
    /**
     * 删除
     * @param id
     */
    public void delUnit (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchUnit (List<String> idList);
}
