package com.ils.modules.mes.material.service.impl;

import com.ils.modules.mes.material.entity.ItemDeliveryGoodsRecord;
import com.ils.modules.mes.material.mapper.ItemDeliveryGoodsRecordMapper;
import com.ils.modules.mes.material.service.ItemDeliveryGoodsRecordService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 发货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class ItemDeliveryGoodsRecordServiceImpl extends ServiceImpl<ItemDeliveryGoodsRecordMapper, ItemDeliveryGoodsRecord> implements ItemDeliveryGoodsRecordService {

    @Override
    public void saveItemDeliveryGoodsRecord(ItemDeliveryGoodsRecord itemDeliveryGoodsRecord) {
         baseMapper.insert(itemDeliveryGoodsRecord);
    }

    @Override
    public void updateItemDeliveryGoodsRecord(ItemDeliveryGoodsRecord itemDeliveryGoodsRecord) {
        baseMapper.updateById(itemDeliveryGoodsRecord);
    }

    @Override
    public void delItemDeliveryGoodsRecord(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchItemDeliveryGoodsRecord(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
