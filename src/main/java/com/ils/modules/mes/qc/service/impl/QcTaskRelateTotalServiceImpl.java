package com.ils.modules.mes.qc.service.impl;

import com.ils.modules.mes.qc.entity.QcTaskRelateTotal;
import com.ils.modules.mes.qc.mapper.QcTaskRelateTotalMapper;
import com.ils.modules.mes.qc.service.QcTaskRelateTotalService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 质检任务关联物料整体
 * @Author: Conner
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Service
public class QcTaskRelateTotalServiceImpl extends ServiceImpl<QcTaskRelateTotalMapper, QcTaskRelateTotal> implements QcTaskRelateTotalService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcTaskRelateTotal(QcTaskRelateTotal qcTaskRelateTotal) {
         baseMapper.insert(qcTaskRelateTotal);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTaskRelateTotal(QcTaskRelateTotal qcTaskRelateTotal) {
        baseMapper.updateById(qcTaskRelateTotal);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcTaskRelateTotal(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcTaskRelateTotal(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
