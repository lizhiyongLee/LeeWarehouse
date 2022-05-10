package com.ils.modules.mes.produce.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkPlanTaskEmployee;
import com.ils.modules.mes.produce.mapper.WorkPlanTaskEmployeeMapper;
import com.ils.modules.mes.produce.service.WorkPlanTaskEmployeeService;

/**
 * @Description: 派工计划生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Service
public class WorkPlanTaskEmployeeServiceImpl extends ServiceImpl<WorkPlanTaskEmployeeMapper, WorkPlanTaskEmployee> implements WorkPlanTaskEmployeeService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkPlanTaskEmployee(WorkPlanTaskEmployee workPlanTaskEmployee) {
         baseMapper.insert(workPlanTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkPlanTaskEmployee(WorkPlanTaskEmployee workPlanTaskEmployee) {
        baseMapper.updateById(workPlanTaskEmployee);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkPlanTaskEmployee(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkPlanTaskEmployee(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
