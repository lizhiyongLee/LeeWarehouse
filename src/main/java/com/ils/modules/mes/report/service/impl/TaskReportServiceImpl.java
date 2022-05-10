package com.ils.modules.mes.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.report.entity.TaskReport;
import com.ils.modules.mes.report.mapper.TaskReportMapper;
import com.ils.modules.mes.report.service.TaskReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 任务报告
 * @Author: Conner
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Service
public class TaskReportServiceImpl extends ServiceImpl<TaskReportMapper, TaskReport> implements TaskReportService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveTaskReport(TaskReport taskReport) {

         baseMapper.insert(taskReport);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateTaskReport(TaskReport taskReport) {
        baseMapper.updateById(taskReport);
    }

    @Override
    public TaskReport queryTaskReportByTaskId(String taskId) {
        QueryWrapper<TaskReport> taskReportQueryWrapper = new QueryWrapper<>();
        taskReportQueryWrapper.eq("task_id",taskId);
        taskReportQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode()  );
        TaskReport taskReport = baseMapper.selectOne(taskReportQueryWrapper);
        return taskReport;
    }
}
