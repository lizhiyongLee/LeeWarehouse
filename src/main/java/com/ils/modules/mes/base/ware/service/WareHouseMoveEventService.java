package com.ils.modules.mes.base.ware.service;

import java.util.List;
import com.ils.modules.mes.base.ware.entity.WareHouseMoveEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 仓库移动事务
 * @Author: Tian
 * @Date:   2021-02-08
 * @Version: V1.0
 */
public interface WareHouseMoveEventService extends IService<WareHouseMoveEvent> {

    /**
     * 添加
     * @param wareHouseMoveEvent
     */
    public void saveWareHouseMoveEvent(WareHouseMoveEvent wareHouseMoveEvent) ;
    
    /**
     * 修改
     * @param wareHouseMoveEvent
     */
    public void updateWareHouseMoveEvent(WareHouseMoveEvent wareHouseMoveEvent);
    
    /**
     * 删除
     * @param id
     */
    public void delWareHouseMoveEvent (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWareHouseMoveEvent (List<String> idList);
}
