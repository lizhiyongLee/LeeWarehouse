package com.ils.modules.mes.produce.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.modules.mes.produce.vo.GanttChartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.PlanTaskStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkPlanTaskEmployee;
import com.ils.modules.mes.produce.service.WorkPlanTaskEmployeeService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.vo.PlanTaskErrorMsgVO;
import com.ils.modules.mes.produce.vo.WorkPlanTaskVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 派工单生产任务
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "派工单生产任务")
@RestController
@RequestMapping("/produce/workPlanTask")
public class WorkPlanTaskController extends ILSController<WorkPlanTask, WorkPlanTaskService> {
    @Autowired
    private WorkPlanTaskService workPlanTaskService;

    @Autowired
    private WorkPlanTaskEmployeeService workPlanTaskEmployeeService;

    /**
     * 分页列表查询
     *
     * @param workPlanTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "派工单已排程生产任务-分页列表查询", notes = "派工单已排程生产任务-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WorkPlanTaskVO workPlanTaskVO,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WorkPlanTaskVO> queryWrapper =
                QueryGenerator.initQueryWrapper(workPlanTaskVO, req.getParameterMap());
        queryWrapper.eq("plan_status", PlanTaskStatusEnum.SCHEDULED.getValue());
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        Page<WorkPlanTaskVO> page = new Page<WorkPlanTaskVO>(pageNo, pageSize);
        IPage<WorkPlanTaskVO> pageList = workPlanTaskService.listPage(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 排程添加派工任务
     *
     * @param workPlanTask
     * @return
     */
    @AutoLog("派工单生产任务-添加")
    @ApiOperation(value = "派工单生产任务-添加", notes = "派工单生产任务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WorkPlanTaskVO workPlanTaskVO) {
        workPlanTaskService.saveWorkPlanTask(workPlanTaskVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 批量排程
     *
     * @param workPlanTask
     * @return
     */
    @AutoLog("批量派工单生产任务-添加")
    @ApiOperation(value = "批量派工单生产任务-添加", notes = "批量派工单生产任务-添加")
    @PostMapping(value = "/batchAdd")
    public Result<?> batchAdd(@RequestBody List<WorkPlanTaskVO> lstWorkPlanTaskVO) {
        workPlanTaskService.batchSaveWorkPlanTask(lstWorkPlanTaskVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑派工任务
     *
     * @param workPlanTask
     * @return
     */
    @AutoLog("派工单生产任务-编辑")
    @ApiOperation(value = "派工单生产任务-编辑", notes = "派工单生产任务-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WorkPlanTaskVO workPlanTaskVO) {
        workPlanTaskService.updateWorkPlanTask(workPlanTaskVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 编辑派工任务-甘特图修改工位和时间
     *
     * @param workPlanTask
     * @return
     */
    @AutoLog("派工单生产任务-甘特图修改工位和时间")
    @ApiOperation(value = "派工单生产任务-甘特图修改工位和时间", notes = "派工单生产任务-甘特图修改工位和时间")
    @PostMapping(value = "/updateById")
    public Result<?> updateById(@RequestBody WorkPlanTask workPlanTask) {
        workPlanTaskService.updateById(workPlanTask);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 取消派工任务
     *
     * @param workPlanTask
     * @return
     */
    @ApiOperation(value = "派工任务排程-取消", notes = "派工任务排程-取消")
    @GetMapping(value = "/cancelPlanTask")
    public Result<?> cancelPlanTask(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        PlanTaskErrorMsgVO planTaskErrorMsgVO = this.workPlanTaskService.cancelPlanTask(idList);
        return Result.ok(planTaskErrorMsgVO);
    }

    /**
     * 分页列表查询
     *
     * @param workPlanTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "派工单已下发生产任务-分页列表查询", notes = "派工单已下发生产任务-分页列表查询")
    @GetMapping(value = "/issueTaskList")
    public Result<?> issueTaskList(WorkPlanTaskVO workPlanTaskVO,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        QueryWrapper<WorkPlanTaskVO> queryWrapper =
                QueryGenerator.initQueryWrapper(workPlanTaskVO, req.getParameterMap());
        queryWrapper.eq("plan_status", PlanTaskStatusEnum.ISSUED.getValue());
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        Page<WorkPlanTaskVO> page = new Page<WorkPlanTaskVO>(pageNo, pageSize);
        IPage<WorkPlanTaskVO> pageList = workPlanTaskService.listPage(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * TODO
     *
     * @param ids下发任务
     * @param checkWorkOrder1需要跟工单时间校验，0 不需要
     * @return
     * @date 2020年11月30日
     */
    @ApiOperation(value = "派工任务-下发", notes = "派工任务-下发")
    @GetMapping(value = "/issuePlanTask")
    public Result<?> issuePlanTask(@RequestParam(name = "ids", required = true) String ids,
                                   @RequestParam(name = "checkWorkOrder", required = false) String checkWorkOrder) {
        List<String> idList = Arrays.asList(ids.split(","));
        PlanTaskErrorMsgVO planTaskErrorMsgVO = this.workPlanTaskService.issuePlanTask(idList, checkWorkOrder);
        return Result.ok(planTaskErrorMsgVO);
    }

    /**
     * 撤回下发
     *
     * @param workPlanTask
     * @return
     */
    @ApiOperation(value = "派工任务-撤回下发", notes = "派工任务-撤回下发")
    @GetMapping(value = "/undoIssuePlanTask")
    public Result<?> undoIssuePlanTask(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        PlanTaskErrorMsgVO planTaskErrorMsgVO = this.workPlanTaskService.undoIssuePlanTask(idList);
        return Result.ok(planTaskErrorMsgVO);
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "派工单生产任务-通过id删除")
    @ApiOperation(value = "派工单生产任务-通过id删除", notes = "派工单生产任务-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        workPlanTaskService.delWorkPlanTask(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "派工单生产任务-批量删除")
    @ApiOperation(value = "派工单生产任务-批量删除", notes = "派工单生产任务-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.workPlanTaskService.delBatchWorkPlanTask(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "派工单生产任务-通过id查询", notes = "派工单生产任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkPlanTaskVO workPlanTaskVO = new WorkPlanTaskVO();
        WorkPlanTask workPlanTask = workPlanTaskService.getById(id);
        BeanUtils.copyProperties(workPlanTask, workPlanTaskVO);
        if (MesCommonConstant.PLAN_TASK_USER_TYPE.equals(workPlanTask.getUserType())) {
            QueryWrapper<WorkPlanTaskEmployee> queryWrapper = new QueryWrapper();
            queryWrapper.eq("task_id", workPlanTask.getId());
            List<WorkPlanTaskEmployee> lst = workPlanTaskEmployeeService.list(queryWrapper);
            workPlanTaskVO.setEmployeeIds(lst.stream().map(item -> item.getEmployeeId()).collect(Collectors.joining(",")));
        }
        workPlanTaskVO.setTaskStatus(workPlanTaskService.generateTaskStatus(workPlanTask));
        return Result.ok(workPlanTaskVO);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param workPlanTask
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkPlanTask workPlanTask) {
        return super.exportXls(request, workPlanTask, WorkPlanTask.class, "派工单生产任务");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WorkPlanTask.class);
    }

    /**
     * 获取甘特图数据
     *
     * @return 甘特图数据
     */
    @GetMapping(value = "/getGanttChart")
    @ApiOperation(value = "获取甘特图数据", notes = "获取甘特图数据")
    public Result<?> getGanttChart(WorkPlanTaskVO workPlanTaskVO,HttpServletRequest req) {
        List<GanttChartVO> ganttChartVOList = workPlanTaskService.getGanttChart(workPlanTaskVO,req);
        return Result.ok(ganttChartVOList);
    }

    /**
     * 批量保存任务
     */
    @PostMapping(value = "/updateBatchWorkPlanTaskVO")
    @ApiOperation(value = "批量保存任务", notes = "批量保存任务")
    public Result<?> updateBatchWorkPlanTaskVO(@RequestBody List<WorkPlanTaskVO> workPlanTaskVOList) {
        workPlanTaskService.updateBatchWorkPlanTaskVO(workPlanTaskVOList);
        return Result.ok();
    }

    /**
     * 只更新锁定状态
     */
    @PostMapping(value = "/updateWorkPlanTaskLockStatus")
    @ApiOperation(value = "只更新锁定状态", notes = "只更新锁定状态")
    public Result<?> updateWorkPlanTaskLockStatus(@RequestBody WorkPlanTaskVO workPlanTaskVO) {
        workPlanTaskService.updateWorkPlanTaskLockStatus(workPlanTaskVO);
        return Result.ok();
    }
}
