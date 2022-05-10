package com.ils.modules.mes.material.service;

import java.util.List;
import com.ils.modules.mes.material.entity.ItemDeliveryGoodsRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 发货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface ItemDeliveryGoodsRecordService extends IService<ItemDeliveryGoodsRecord> {

    /**
     * 添加
     * @param itemDeliveryGoodsRecord
     */
    public void saveItemDeliveryGoodsRecord(ItemDeliveryGoodsRecord itemDeliveryGoodsRecord) ;
    
    /**
     * 修改
     * @param itemDeliveryGoodsRecord
     */
    public void updateItemDeliveryGoodsRecord(ItemDeliveryGoodsRecord itemDeliveryGoodsRecord);
    
    /**
     * 删除
     * @param id
     */
    public void delItemDeliveryGoodsRecord (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchItemDeliveryGoodsRecord (List<String> idList);
}
