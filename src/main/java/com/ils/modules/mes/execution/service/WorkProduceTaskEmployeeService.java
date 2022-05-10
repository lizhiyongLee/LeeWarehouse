package com.ils.modules.mes.execution.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.execution.entity.WorkProduceTaskEmployee;

/**
 * @Description: 执行生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
public interface WorkProduceTaskEmployeeService extends IService<WorkProduceTaskEmployee> {

    /**
     * 添加
     * @param workProduceTaskEmployee
     */
    public void saveWorkProduceTaskEmployee(WorkProduceTaskEmployee workProduceTaskEmployee) ;
    
    /**
     * 修改
     * @param workProduceTaskEmployee
     */
    public void updateWorkProduceTaskEmployee(WorkProduceTaskEmployee workProduceTaskEmployee);
    
    /**
     * 删除
     * @param id
     */
    public void delWorkProduceTaskEmployee (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWorkProduceTaskEmployee (List<String> idList);

    /**
     * 
     * 根据执行任务删除执行人员
     * 
     * @param excuteTaskId
     * @return
     * @date 2020年12月8日
     */
    public boolean deleteByExcuteTaskId(String excuteTaskId);
}
