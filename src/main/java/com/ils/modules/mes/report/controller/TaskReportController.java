package com.ils.modules.mes.report.controller;

import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.report.entity.TaskReport;
import com.ils.modules.mes.report.service.TaskReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 任务报告
 * @Author: Tian
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="任务报告")
@RestController
@RequestMapping("/report/taskReport")
public class TaskReportController extends ILSController<TaskReport, TaskReportService> {
	@Autowired
	private TaskReportService taskReportService;

	
	/**
	 * 添加
	 *
	 * @param taskReport
	 * @return
	 */
	@ApiOperation(value="任务报告-添加", notes="任务报告-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TaskReport taskReport) {
		taskReportService.saveTaskReport(taskReport);
		return Result.ok(taskReport);
	}
	
	/**
	 * 编辑
	 *
	 * @param taskReport
	 * @return
	 */
	@ApiOperation(value="任务报告-编辑", notes="任务报告-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody TaskReport taskReport) {
		taskReportService.updateTaskReport(taskReport);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value="任务报告-通过id查询", notes="任务报告-通过id查询")
	@GetMapping(value = "/queryByTaskId")
	public Result<?> queryById(@RequestParam(name="taskId",required=true) String taskId) {
		TaskReport taskReport = taskReportService.queryTaskReportByTaskId(taskId);
		return Result.ok(taskReport);
	}
}
