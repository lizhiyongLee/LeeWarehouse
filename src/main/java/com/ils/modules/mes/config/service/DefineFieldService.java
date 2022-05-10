package com.ils.modules.mes.config.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.config.entity.DefineField;

/**
 * @Description: 自定义字段
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
public interface DefineFieldService extends IService<DefineField> {

    /**
     * 添加
     * @param defineField
     */
    public void saveDefineField(DefineField defineField) ;
    
    /**
     * 修改
     * @param defineField
     */
    public void updateDefineField(DefineField defineField);
    
    /**
     * 删除
     * @param id
     */
    public void delDefineField (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchDefineField (List<String> idList);
}
