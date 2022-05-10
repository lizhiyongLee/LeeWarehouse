package com.ils.modules.mes.sop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.sop.entity.SopStep;
import com.ils.modules.mes.sop.vo.SopStepVO;

import java.util.List;

/**
 * @Description: 标准作业任务步骤
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
public interface SopStepService extends IService<SopStep> {

    /**
     * 添加
     *
     * @param sopStep
     */
    void saveSopStep(SopStep sopStep);

    /**
     * 修改
     *
     * @param sopStep
     */
    void updateSopStep(SopStep sopStep);


    /**
     * 通过任务id查询作业流程
     * @param taskId
     * @return
     */
    List<SopStepVO> queryByTaskId(String taskId);

    /**
     * sop任务步骤，完成当前任务
     * @param id
     */
    void completeTask(String id);

    /**
     * 根据步骤id生成对应的质检任务
     * @param stepId
     */
    void sopQcTaskCreateByStepId(String stepId);
}
