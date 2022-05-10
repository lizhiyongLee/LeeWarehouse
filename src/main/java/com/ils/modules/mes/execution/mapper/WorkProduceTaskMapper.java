package com.ils.modules.mes.execution.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.vo.WorkProduceTaskInfoVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskQueryVO;

/**
 * @Description: 执行生产任务
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
public interface WorkProduceTaskMapper extends ILSMapper<WorkProduceTask> {

    /**
     * 
     * 根据派工任务删除执行任务
     * 
     * @param planTaskId
     * @return
     * @date 2020年12月8日
     */
    public boolean deleteByPlanTaskId(String planTaskId);
    
    /**
     * 
     * 待领取执行任务
     * 
     * @param page
     * @param userId
     * @param queryWrapper
     * @return
     * @date 2020年12月9日
     */
    public IPage<WorkProduceTask> todoList(Page<WorkProduceTask> page, @Param("userId") String userId,
        @Param("ew") QueryWrapper<WorkProduceTask> queryWrapper);

    /**
     * 
     * 领取之后执行任务
     * 
     * @param page
     * @param userId
     * @param queryWrapper
     * @return
     * @date 2020年12月9日
     */
    public IPage<WorkProduceTask> doneList(Page<WorkProduceTask> page, @Param("userId") String userId,
        @Param("ew") QueryWrapper<WorkProduceTask> queryWrapper);

    /**
     * 
     * 获取执行任务详细信息
     * 
     * @param id
     * @return WorkProduceTaskInfoVO
     * @date 2020年12月10日
     */
    public WorkProduceTaskInfoVO getProduceTaskInfoById(@Param("id") String id);

    /**
     * 
     * web 端执行任务查询
     * 
     * @param page
     * @param queryWrapper
     * @return
     * @date 2021年1月13日
     */
    public IPage<WorkProduceTaskQueryVO> queryProduceTaskList(Page<WorkProduceTaskQueryVO> page,
        @Param("ew") QueryWrapper<WorkProduceTaskQueryVO> queryWrapper);

    /**
     * 查询产出物料是标签码管理的物料
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<WorkProduceTask> allQrcodeTaskList(Page<WorkProduceTask> page,
                                           @Param("ew") QueryWrapper<WorkProduceTask> queryWrapper);
}
