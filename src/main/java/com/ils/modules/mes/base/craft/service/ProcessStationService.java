package com.ils.modules.mes.base.craft.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.ProcessStation;

/**
 * @Description: 工序关联工位
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessStationService extends IService<ProcessStation> {

    /**
     * 添加
     * @param processStation
     */
    public void saveProcessStation(ProcessStation processStation) ;
    
    /**
     * 修改
     * @param processStation
     */
    public void updateProcessStation(ProcessStation processStation);
    
    /**
     * 删除
     * @param id
     */
    public void delProcessStation (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchProcessStation (List<String> idList);
}
