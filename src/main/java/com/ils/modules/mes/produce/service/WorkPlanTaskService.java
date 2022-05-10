package com.ils.modules.mes.produce.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.vo.GanttChartVO;
import com.ils.modules.mes.produce.vo.PlanTaskErrorMsgVO;
import com.ils.modules.mes.produce.vo.WorkPlanTaskVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 派工单生产任务
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
public interface WorkPlanTaskService extends IService<WorkPlanTask> {

    /**
     * 添加
     *
     * @param workPlanTaskVO
     */
    public void saveWorkPlanTask(WorkPlanTaskVO workPlanTaskVO);

    /**
     * 批量添加
     *
     * @param lstWorkPlanTaskVO
     */
    public void batchSaveWorkPlanTask(List<WorkPlanTaskVO> lstWorkPlanTaskVO);

    /**
     * 修改
     *
     * @param workPlanTaskVO
     */
    public void updateWorkPlanTask(WorkPlanTaskVO workPlanTaskVO);

    /**
     * 删除
     *
     * @param id
     */
    public void delWorkPlanTask(String id);

    /**
     * 派工任务排程取消
     *
     * @param idList
     * @return PlanTaskErrorMsgVO
     * @date 2020年11月30日
     */
    public PlanTaskErrorMsgVO cancelPlanTask(List<String> idList);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchWorkPlanTask(List<String> idList);

    /**
     * 查询派工任务 BOM ListVO
     *
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    public IPage<WorkPlanTaskVO> listPage(Page<WorkPlanTaskVO> page,
                                          QueryWrapper<WorkPlanTaskVO> queryWrapper);

    /**
     * 派工任务下发
     *
     * @param idList
     * @param checkWorkOrder 1 需要跟工单时间校验，0 不需要
     * @return PlanTaskErrorMsgVO
     * @date 2020年11月26日
     */
    public PlanTaskErrorMsgVO issuePlanTask(List<String> idList, String checkWorkOrder);

    /**
     * 取消下发派工任务
     *
     * @param idList下发任务
     * @return PlanTaskErrorMsgVO
     * @date 2020年11月26日
     */
    public PlanTaskErrorMsgVO undoIssuePlanTask(List<String> idList);

    /**
     * 获取数据
     *
     * @param workPlanTaskVO
     * @param req
     * @return 符合格式的数据
     */
    public List<GanttChartVO> getGanttChart(WorkPlanTaskVO workPlanTaskVO, HttpServletRequest req);

    /**
     * 批量更新数据
     *
     * @param workPlanTaskVOList 数据列表
     */
    public void updateBatchWorkPlanTaskVO(List<WorkPlanTaskVO> workPlanTaskVOList);

    /**
     * 只更新锁定状态
     * @param workPlanTaskVO
     */
    void updateWorkPlanTaskLockStatus(WorkPlanTaskVO workPlanTaskVO);

    /**
     * 获取综合状态（展示用）
     * @param workPlanTask
     * @return
     */
    String generateTaskStatus(WorkPlanTask workPlanTask);
}
