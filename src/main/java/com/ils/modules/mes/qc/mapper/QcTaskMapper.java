package com.ils.modules.mes.qc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.vo.QcTaskVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 质检任务
 * @Author: Tian
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface QcTaskMapper extends ILSMapper<QcTask> {

    /**
     * 所有质检任务分页查询
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<QcTaskVO> listPage(Page<QcTaskVO> page, @Param("ew") QueryWrapper<QcTaskVO> queryWrapper);


    /**
     * 待领取任务列表分页查询
     *
     * @param userId
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<QcTaskVO> toReceiveQcTaskListPage(Page<QcTask> page, @Param("ew") QueryWrapper<QcTask> queryWrapper, String userId);

    /**
     * 已领取任务列表分页查询
     *
     * @param userId
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<QcTaskVO> receivedQcTaskListPage(Page<QcTask> page, @Param("ew") QueryWrapper<QcTask> queryWrapper, String userId);

    /**
     * 质检任务，查询我的任务
     * @param page
     * @param queryWrapper
     * @param userId
     * @return
     */
    public IPage<QcTaskVO> myTaskListPage(Page<QcTask> page, @Param("ew") QueryWrapper<QcTask> queryWrapper, String userId);
    /**
     * 查询计划执行人员
     *
     * @param taskId
     * @return
     */
    public String queryTaskEmployeeByTaskId(String taskId);

}
