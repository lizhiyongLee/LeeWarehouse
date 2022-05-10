package com.ils.modules.mes.qc.service.impl;

import com.ils.modules.mes.qc.entity.QcTaskReportItemValue;
import com.ils.modules.mes.qc.mapper.QcTaskReportItemValueMapper;
import com.ils.modules.mes.qc.service.QcTaskReportItemValueService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 质检报告记录值
 * @Author: Conner
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Service
public class QcTaskReportItemValueServiceImpl extends ServiceImpl<QcTaskReportItemValueMapper, QcTaskReportItemValue> implements QcTaskReportItemValueService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcTaskReportItemValue(QcTaskReportItemValue qcTaskReportItemValue) {
         baseMapper.insert(qcTaskReportItemValue);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTaskReportItemValue(QcTaskReportItemValue qcTaskReportItemValue) {
        baseMapper.updateById(qcTaskReportItemValue);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcTaskReportItemValue(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcTaskReportItemValue(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
