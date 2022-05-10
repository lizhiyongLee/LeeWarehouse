package com.ils.modules.mes.qc.service.impl;

import com.ils.modules.mes.qc.entity.QcTaskEmployee;
import com.ils.modules.mes.qc.mapper.QcTaskEmployeeMapper;
import com.ils.modules.mes.qc.service.QcTaskEmployeeService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 质检任务关联人员
 * @Author: Conner
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Service
public class QcTaskEmployeeServiceImpl extends ServiceImpl<QcTaskEmployeeMapper, QcTaskEmployee> implements QcTaskEmployeeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQcTaskEmployee(QcTaskEmployee qcTaskEmployee) {
         baseMapper.insert(qcTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTaskEmployee(QcTaskEmployee qcTaskEmployee) {
        baseMapper.updateById(qcTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delQcTaskEmployee(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchQcTaskEmployee(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
