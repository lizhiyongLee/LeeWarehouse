package com.ils.modules.mes.execution.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.execution.entity.WorkProduceTaskEmployee;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskEmployeeMapper;
import com.ils.modules.mes.execution.service.WorkProduceTaskEmployeeService;

/**
 * @Description: 执行生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
@Service
public class WorkProduceTaskEmployeeServiceImpl extends ServiceImpl<WorkProduceTaskEmployeeMapper, WorkProduceTaskEmployee> implements WorkProduceTaskEmployeeService {

    @Override
    public void saveWorkProduceTaskEmployee(WorkProduceTaskEmployee workProduceTaskEmployee) {
         baseMapper.insert(workProduceTaskEmployee);
    }

    @Override
    public void updateWorkProduceTaskEmployee(WorkProduceTaskEmployee workProduceTaskEmployee) {
        baseMapper.updateById(workProduceTaskEmployee);
    }

    @Override
    public void delWorkProduceTaskEmployee(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchWorkProduceTaskEmployee(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public boolean deleteByExcuteTaskId(String excuteTaskId) {
        return baseMapper.deleteByExcuteTaskId(excuteTaskId);
    }
}
