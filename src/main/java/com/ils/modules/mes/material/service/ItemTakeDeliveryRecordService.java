package com.ils.modules.mes.material.service;

import java.util.List;
import com.ils.modules.mes.material.entity.ItemTakeDeliveryRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 收货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface ItemTakeDeliveryRecordService extends IService<ItemTakeDeliveryRecord> {

    /**
     * 添加
     * @param itemTakeDeliveryRecord
     */
    public void saveItemTakeDeliveryRecord(ItemTakeDeliveryRecord itemTakeDeliveryRecord) ;
    
    /**
     * 修改
     * @param itemTakeDeliveryRecord
     */
    public void updateItemTakeDeliveryRecord(ItemTakeDeliveryRecord itemTakeDeliveryRecord);
    
    /**
     * 删除
     * @param id
     */
    public void delItemTakeDeliveryRecord (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchItemTakeDeliveryRecord (List<String> idList);
}
