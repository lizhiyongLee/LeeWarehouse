package com.ils.modules.mes.execution.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.ProduceTaskTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import com.ils.modules.mes.execution.service.WorkProduceTaskParaService;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.execution.vo.UpdateParamVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskDetailVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskInfoVO;
import com.ils.modules.mes.execution.vo.WorkProduceTaskQueryVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 执行生产任务
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "执行生产任务")
@RestController
@RequestMapping("/execution/workProduceTask")
public class WorkProduceTaskController extends ILSController<WorkProduceTask, WorkProduceTaskService> {
    @Autowired
    private WorkProduceTaskService workProduceTaskService;
    @Autowired
    private WorkProduceTaskParaService workProduceTaskParaService;


    /**
     * 待领任务分页列表查询
     *
     * @param workProduceTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "待领任务-分页列表查询", notes = "待领任务-分页列表查询")
    @GetMapping(value = "/todoList")
    public Result<?> todoList(WorkProduceTask workProduceTask,
                              @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                              @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<WorkProduceTask> queryWrapper = QueryGenerator.initQueryWrapper(workProduceTask, req.getParameterMap());

        // 获取执行任务排他配置开关
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_TASK_TYPE_SWITCH);
        String taskType = null;
        if (bizConfig.getConfigValue() == null) {
            taskType = ProduceTaskTypeEnum.TOGETHER.getValue();
        } else {
            taskType = bizConfig.getConfigValue();
        }
        Page<WorkProduceTask> page = new Page<WorkProduceTask>(pageNo, pageSize);
        String userId = null;
        if (ProduceTaskTypeEnum.TOGETHER.getValue().equals(taskType)) {
            userId = CommonUtil.getLoginUser().getId();
        }
        // 加一个默认条件，防止SQL报错
        queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ONE.getStrCode());
        IPage<WorkProduceTask> pageList = workProduceTaskService.todoList(page, userId, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 已领任务分页列表查询
     *
     * @param workProduceTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "已领任务-分页列表查询", notes = "已领任务-分页列表查询")
    @GetMapping(value = "/doneList")
    public Result<?> doneList(WorkProduceTask workProduceTask,
                              @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                              @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        QueryWrapper<WorkProduceTask> queryWrapper =
                QueryGenerator.initQueryWrapper(workProduceTask, req.getParameterMap());

        Page<WorkProduceTask> page = new Page<WorkProduceTask>(pageNo, pageSize);
        String userId = CommonUtil.getLoginUser().getId();
        String positionName = req.getParameter("positionName");
        // 加一个默认条件，防止SQL报错
        queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ONE.getStrCode());
        IPage<WorkProduceTask> pageList = workProduceTaskService.doneList(page, userId,positionName, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 查询生产物料是标签码管理的生产任务
     *
     * @param workProduceTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "查询生产任务-查询生产物料是标签码管理的生产任务", notes = "查询生产任务-查询生产物料是标签码管理的生产任务")
    @GetMapping(value = "/allQrcodeTaskList")
    public Result<?> allQrcodeTaskList(WorkProduceTask workProduceTask,
                                       @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                       @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        QueryWrapper<WorkProduceTask> queryWrapper =
                QueryGenerator.initQueryWrapper(workProduceTask, req.getParameterMap());
        Page<WorkProduceTask> page = new Page<WorkProduceTask>(pageNo, pageSize);
        // 加一个默认条件，防止SQL报错
        queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ONE.getStrCode());
        IPage<WorkProduceTask> pageList = workProduceTaskService.allQrcodeTaskList(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询生产任务-通过id查询", notes = "查询生产任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkProduceTaskInfoVO workProduceTaskInfoVO = workProduceTaskService.getWorkProduceTaskInfoById(id);
        return Result.ok(workProduceTaskInfoVO);
    }

    /**
     * 通过id查询详情信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询生产任务详情-通过id查询", notes = "查询生产任务详情-通过id查询")
    @GetMapping(value = "/queryDetailById")
    public Result<?> queryDetailById(@RequestParam(name = "id", required = true) String id) {
        WorkProduceTaskDetailVO workProduceTaskDetailVO = workProduceTaskService.queryDetailById(id);
        return Result.ok(workProduceTaskDetailVO);
    }

    /**
     * 通过任务id查询模板参数
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "通过任务id查询模板参数", notes = "通过任务id查询模板参数")
    @GetMapping(value = "/queryParaById")
    public Result<?> queryParaById(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<WorkProduceTaskPara> workProduceTaskParaQueryWrapper = new QueryWrapper<>();
        workProduceTaskParaQueryWrapper.eq("produce_task_id", id);
        List<WorkProduceTaskPara> workProduceTaskParaList = workProduceTaskParaService.list(workProduceTaskParaQueryWrapper);
        ResultDataVO resultDataVO = new ResultDataVO();
        resultDataVO.setData(workProduceTaskParaList);
        return Result.ok(resultDataVO);
    }

    /**
     * 领取任务
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "领取任务-通过id领取任务", notes = "领取任务-通过id领取任务")
    @GetMapping(value = "/signWorkProduceTask")
    public Result<?> signWorkProduceTask(@RequestParam(name = "id", required = true) String id) {
        workProduceTaskService.signWorkProduceTask(id);
        return Result.ok();
    }

    /**
     * 转接任务
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "转接任务-通过id转接任务", notes = "转接任务-通过id转接任务")
    @GetMapping(value = "/suspendWorkProduceTask")
    public Result<?> suspendWorkProduceTask(@RequestParam(name = "id", required = true) String id) {
        workProduceTaskService.suspendWorkProduceTask(id);
        return Result.ok();
    }

    /**
     * 指派任务
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "指派任务-通过id转接任务", notes = "指派任务-通过id转接任务")
    @PostMapping(value = "/assignProduceTask")
    public Result<?> assignProduceTask(@RequestBody WorkProduceTaskDetailVO workProduceTaskDetailVO) {
        workProduceTaskService.assignProduceTask(workProduceTaskDetailVO);
        return Result.ok();
    }

    /**
     * 更新任务状态接口
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "更新任务状态", notes = "更新任务状态")
    @PostMapping(value = "/updateProduceTaskStatus")
    public Result<?> updateProduceTaskStatus(@RequestBody UpdateParamVO updateParamsVO) {
        workProduceTaskService.updateProduceTaskStatus(updateParamsVO);
        return Result.ok();
    }

    /**
     * 更新任务报告模板
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "更新任务报告模板", notes = "更新任务报告模板")
    @PostMapping(value = "/updateProduceTaskTemplate")
    public Result<?> updateProduceTaskTemplate(@RequestParam String taskId, @RequestParam String templateId) {
        workProduceTaskService.updateProduceTaskTemplate(taskId, templateId);
        return Result.ok();
    }


    /**
     * 导出excel
     *
     * @param request
     * @param workProduceTask
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkProduceTask workProduceTask) {
        return super.exportXls(request, workProduceTask, WorkProduceTask.class, "执行生产任务");
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
        return super.importExcel(request, response, WorkProduceTask.class);
    }

    /**
     * 执行任务分页列表查询
     *
     * @param workProduceTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "web端执行任务-分页列表查询", notes = "web端执行任务-分页列表查询")
    @GetMapping(value = "/queryProduceTaskList")
    public Result<?> queryProduceTaskList(WorkProduceTaskQueryVO workProduceTaskQueryVO,
                                          @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                          @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        String employeeId = workProduceTaskQueryVO.getEmployeeId();
        if (StringUtils.isNoneBlank(employeeId)) {
            workProduceTaskQueryVO.setEmployeeId("*" + employeeId + "*");
        }
        QueryWrapper<WorkProduceTaskQueryVO> queryWrapper = QueryGenerator.initQueryWrapper(workProduceTaskQueryVO, req.getParameterMap());

        Page<WorkProduceTaskQueryVO> page = new Page<WorkProduceTaskQueryVO>(pageNo, pageSize);

        IPage<WorkProduceTaskQueryVO> pageList = workProduceTaskService.queryProduceTaskList(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 条件转换
     *
     * @param req
     * @param queryWrapper
     * @date 2021年1月13日
     */
    private void initQueryCondition(HttpServletRequest req, QueryWrapper<WorkProduceTaskQueryVO> queryWrapper) {

        String taskCode = req.getParameter("taskCode");
        if (StringUtils.isNoneBlank(taskCode)) {
            queryWrapper.like("a.task_code", taskCode);
        }

        String processId = req.getParameter("processId");
        if (StringUtils.isNoneBlank(processId)) {
            queryWrapper.eq("a.process_id", processId);
        }

        String itemId = req.getParameter("itemId");
        if (StringUtils.isNoneBlank(itemId)) {
            queryWrapper.eq("b.item_id", itemId);
        }

        String stationId = req.getParameter("stationId");
        if (StringUtils.isNoneBlank(stationId)) {
            queryWrapper.eq("a.station_id", stationId);
        }

        String orderNo = req.getParameter("orderNo");
        if (StringUtils.isNoneBlank(orderNo)) {
            queryWrapper.like("b.order_no", orderNo);
        }

        String batchNo = req.getParameter("batchNo");
        if (StringUtils.isNoneBlank(batchNo)) {
            queryWrapper.like("a.batch_no", batchNo);
        }

        String employeeId = req.getParameter("employeeId");
        if (StringUtils.isNoneBlank(employeeId)) {
            queryWrapper.like("c.employee_id", employeeId);
        }
        String exeStatus = req.getParameter("exeStatus");
        if (StringUtils.isNoneBlank(exeStatus)) {
            queryWrapper.like("a.exe_status", exeStatus);
        }
    }
}
