package com.ils.modules.mes.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.report.entity.TaskReport;

/**
 * @Description: 任务报告
 * @Author: Tian
 * @Date:   2020-12-18
 * @Version: V1.0
 */
public interface TaskReportService extends IService<TaskReport> {

    /**
     * 添加
     * @param taskReport
     */
    public void saveTaskReport(TaskReport taskReport) ;
    
    /**
     * 修改
     * @param taskReport
     */
    public void updateTaskReport(TaskReport taskReport);

    /**
     * 通过任务id查询任务内容
     * @param taskId
     * @return
     */
    public TaskReport queryTaskReportByTaskId(String taskId);

}
