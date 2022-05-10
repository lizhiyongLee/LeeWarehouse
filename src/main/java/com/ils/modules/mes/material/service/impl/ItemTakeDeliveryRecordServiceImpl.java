package com.ils.modules.mes.material.service.impl;

import com.ils.modules.mes.material.entity.ItemTakeDeliveryRecord;
import com.ils.modules.mes.material.mapper.ItemTakeDeliveryRecordMapper;
import com.ils.modules.mes.material.service.ItemTakeDeliveryRecordService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 收货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class ItemTakeDeliveryRecordServiceImpl extends ServiceImpl<ItemTakeDeliveryRecordMapper, ItemTakeDeliveryRecord> implements ItemTakeDeliveryRecordService {

    @Override
    public void saveItemTakeDeliveryRecord(ItemTakeDeliveryRecord itemTakeDeliveryRecord) {
         baseMapper.insert(itemTakeDeliveryRecord);
    }

    @Override
    public void updateItemTakeDeliveryRecord(ItemTakeDeliveryRecord itemTakeDeliveryRecord) {
        baseMapper.updateById(itemTakeDeliveryRecord);
    }

    @Override
    public void delItemTakeDeliveryRecord(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchItemTakeDeliveryRecord(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
