package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Shift;

/**
 * @Description: 班次
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
public interface ShiftService extends IService<Shift> {

    /**
     * 添加
     * @param shift
     */
    public void saveShift(Shift shift) ;
    
    /**
     * 修改
     * @param shift
     */
    public void updateShift(Shift shift);
    
    /**
     * 删除
     * @param id
     */
    public void delShift (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchShift (List<String> idList);
}
