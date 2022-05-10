package com.ils.modules.mes.base.qc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.qc.entity.NgItemType;

/**
 * @Description: 不良类型
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
public interface NgItemTypeService extends IService<NgItemType> {

    /**
     * 添加
     * @param ngItemType
     */
    public void saveNgItemType(NgItemType ngItemType) ;
    
    /**
     * 修改
     * @param ngItemType
     */
    public void updateNgItemType(NgItemType ngItemType);
    
    /**
     * 删除
     * @param id
     */
    public void delNgItemType (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchNgItemType (List<String> idList);
}
