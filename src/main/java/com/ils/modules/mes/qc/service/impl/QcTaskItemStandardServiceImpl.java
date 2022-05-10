package com.ils.modules.mes.qc.service.impl;

import com.ils.modules.mes.qc.entity.QcTaskItemStandard;
import com.ils.modules.mes.qc.mapper.QcTaskItemStandardMapper;
import com.ils.modules.mes.qc.service.QcTaskItemStandardService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 质检任务关联质检标准
 * @Author: Conner
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Service
public class QcTaskItemStandardServiceImpl extends ServiceImpl<QcTaskItemStandardMapper, QcTaskItemStandard> implements QcTaskItemStandardService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcTaskItemStandard(QcTaskItemStandard qcTaskItemStandard) {
         baseMapper.insert(qcTaskItemStandard);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTaskItemStandard(QcTaskItemStandard qcTaskItemStandard) {
        baseMapper.updateById(qcTaskItemStandard);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcTaskItemStandard(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcTaskItemStandard(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
