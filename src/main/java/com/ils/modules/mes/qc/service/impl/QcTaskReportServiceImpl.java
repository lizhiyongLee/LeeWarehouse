package com.ils.modules.mes.qc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.qc.entity.QcTaskReport;
import com.ils.modules.mes.qc.mapper.QcTaskReportMapper;
import com.ils.modules.mes.qc.service.QcTaskReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 质检任务报告
 * @Author: Conner
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Service
public class QcTaskReportServiceImpl extends ServiceImpl<QcTaskReportMapper, QcTaskReport> implements QcTaskReportService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcTaskReport(QcTaskReport qcTaskReport) {
         baseMapper.insert(qcTaskReport);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTaskReport(QcTaskReport qcTaskReport) {
        baseMapper.updateById(qcTaskReport);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcTaskReport(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcTaskReport(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
