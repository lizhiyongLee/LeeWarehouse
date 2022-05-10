package com.ils.modules.mes.qc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.qc.entity.QcTaskRelateSample;
import com.ils.modules.mes.qc.mapper.QcTaskRelateSampleMapper;
import com.ils.modules.mes.qc.service.QcTaskRelateSampleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 质检任务关联物料样本
 * @Author: Conner
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Service
public class QcTaskRelateSampleServiceImpl extends ServiceImpl<QcTaskRelateSampleMapper, QcTaskRelateSample> implements QcTaskRelateSampleService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcTaskRelateSample(QcTaskRelateSample qcTaskRelateSample) {
         baseMapper.insert(qcTaskRelateSample);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTaskRelateSample(QcTaskRelateSample qcTaskRelateSample) {
        baseMapper.updateById(qcTaskRelateSample);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcTaskRelateSample(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcTaskRelateSample(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
