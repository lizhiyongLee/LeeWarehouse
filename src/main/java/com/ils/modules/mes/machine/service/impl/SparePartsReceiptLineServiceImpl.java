package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.machine.entity.SparePartsReceiptLine;
import com.ils.modules.mes.machine.mapper.SparePartsReceiptLineMapper;
import com.ils.modules.mes.machine.service.SparePartsReceiptLineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 备件入库明细
 * @Author: Conner
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Service
public class SparePartsReceiptLineServiceImpl extends ServiceImpl<SparePartsReceiptLineMapper, SparePartsReceiptLine> implements SparePartsReceiptLineService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSparePartsReceiptLine(SparePartsReceiptLine sparePartsReceiptLine) {
         baseMapper.insert(sparePartsReceiptLine);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSparePartsReceiptLine(SparePartsReceiptLine sparePartsReceiptLine) {
        baseMapper.updateById(sparePartsReceiptLine);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSparePartsReceiptLine(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSparePartsReceiptLine(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
