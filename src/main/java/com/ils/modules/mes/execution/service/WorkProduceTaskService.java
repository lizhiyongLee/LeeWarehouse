package com.ils.modules.mes.execution.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.vo.UpdateParamVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskDetailVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskInfoVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskQueryVO;

import java.util.List;

/**
 * @Description: 执行生产任务
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
public interface WorkProduceTaskService extends IService<WorkProduceTask> {

    /**
     * 添加
     * @param workProduceTask
     */
    public void saveWorkProduceTask(WorkProduceTask workProduceTask) ;

    /**
     * 修改
     * @param workProduceTask
     */
    public void updateWorkProduceTask(WorkProduceTask workProduceTask);


    /**
     *
     * 查询执行进度、工单任务详细信息
     *
     * @param id
     * @return WorkProduceTaskInfoVO
     * @date 2020年12月9日
     */
    public WorkProduceTaskInfoVO getWorkProduceTaskInfoById(String id);

    /**
     *
     * 查询包含物料执行任务详细信息
     *
     * @param id
     * @return WorkProduceTaskDetailVO
     * @date 2020年12月9日
     */
    public WorkProduceTaskDetailVO queryDetailById(String id);

    /**
     *
     * 领取任务
     *
     * @param id
     * @date 2020年12月9日
     */
    public void signWorkProduceTask(String id);

    /**
     *
     * 转接任务
     *
     * @param id
     * @date 2020年12月9日
     */
    public void suspendWorkProduceTask(String id);

    /**
     *
     * 更新任务状态更新
     *
     * @param updateParamsVO
     */
    public void updateProduceTaskStatus(UpdateParamVO updateParamsVO);

    /**
     *
     * 更新任务模板
     *
     * @param taskId
     * @param templateId
     */
    public void updateProduceTaskTemplate(String taskId, String templateId);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchWorkProduceTask (List<String> idList);

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
     * 删除
     *
     * @param id
     */
    public void delWorkProduceTask(String id);

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
    public IPage<WorkProduceTask> todoList(Page<WorkProduceTask> page, String userId,
                                           QueryWrapper<WorkProduceTask> queryWrapper);

    /**
     *
     * 领取之后执行任务
     *
     * @param page
     * @param userId
     * @param positionName
     * @param queryWrapper
     * @return
     * @date 2020年12月9日
     */
    public IPage<WorkProduceTask> doneList(Page<WorkProduceTask> page, String userId, String positionName,
                                           QueryWrapper<WorkProduceTask> queryWrapper);


    /**
     * 树状结构查询最底层数据
     * @param positionNameList 待匹配列表
     * @param lstNodeVO 所有数据
     * @return
     */
    List<String> getStationNameList(List<String> positionNameList, List<NodeVO> lstNodeVO);

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
                                                              QueryWrapper<WorkProduceTaskQueryVO> queryWrapper);

    /**
     * 指派任务
     *
     * @param workProduceTaskDetailVO
     * @date 2021年1月14日
     */
    public void assignProduceTask(WorkProduceTaskDetailVO workProduceTaskDetailVO);

    /**
     * 查询生产物料是标签码管理的生产任务
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<WorkProduceTask> allQrcodeTaskList(Page<WorkProduceTask> page,
                                                    QueryWrapper<WorkProduceTask> queryWrapper);
}
