package com.ils.modules.mes.machine.service.impl;

import com.ils.modules.mes.machine.entity.SparePartsSendLine;
import com.ils.modules.mes.machine.mapper.SparePartsSendLineMapper;
import com.ils.modules.mes.machine.service.SparePartsSendLineService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 备件入库明细
 * @Author: Conner
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Service
public class SparePartsSendLineServiceImpl extends ServiceImpl<SparePartsSendLineMapper, SparePartsSendLine> implements SparePartsSendLineService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSparePartsSendLine(SparePartsSendLine sparePartsSendLine) {
         baseMapper.insert(sparePartsSendLine);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSparePartsSendLine(SparePartsSendLine sparePartsSendLine) {
        baseMapper.updateById(sparePartsSendLine);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSparePartsSendLine(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSparePartsSendLine(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
