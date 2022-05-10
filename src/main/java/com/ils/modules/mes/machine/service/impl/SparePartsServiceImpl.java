package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.machine.entity.SpareParts;
import com.ils.modules.mes.machine.mapper.SparePartsMapper;
import com.ils.modules.mes.machine.service.SparePartsService;
import com.ils.modules.mes.machine.vo.SparePartsQtyVO;
import com.ils.modules.mes.machine.vo.SparePartsReceiptOrSendOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 备件定义
 * @Author: Conner
 * @Date: 2021-02-23
 * @Version: V1.0
 */
@Service
public class SparePartsServiceImpl extends ServiceImpl<SparePartsMapper, SpareParts> implements SparePartsService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSpareParts(SpareParts spareParts) {
        baseMapper.insert(spareParts);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSpareParts(SpareParts spareParts) {
        baseMapper.updateById(spareParts);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSpareParts(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSpareParts(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<SparePartsReceiptOrSendOrderVO> querySparePartsRelate(String id) {
        return  baseMapper.querySparePartsRelate(id);
    }

    @Override
    public List<SparePartsQtyVO> querySparePartsQtyWithStorage(String id) {
        return baseMapper.querySparePartsQtyWithStorage(id);
    }
}
