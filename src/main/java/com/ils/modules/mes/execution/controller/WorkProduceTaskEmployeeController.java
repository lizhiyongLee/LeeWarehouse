package com.ils.modules.mes.execution.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.execution.entity.WorkProduceTaskEmployee;
import com.ils.modules.mes.execution.service.WorkProduceTaskEmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 执行生产任务关联计划执行人员
 * @Author: fengyi
 * @Date: 2020-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="执行生产任务关联计划执行人员")
@RestController
@RequestMapping("/execution/workProduceTaskEmployee")
public class WorkProduceTaskEmployeeController extends ILSController<WorkProduceTaskEmployee, WorkProduceTaskEmployeeService> {
	@Autowired
	private WorkProduceTaskEmployeeService workProduceTaskEmployeeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param workProduceTaskEmployee
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="执行生产任务关联计划执行人员-分页列表查询", notes="执行生产任务关联计划执行人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WorkProduceTaskEmployee workProduceTaskEmployee,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WorkProduceTaskEmployee> queryWrapper = QueryGenerator.initQueryWrapper(workProduceTaskEmployee, req.getParameterMap());
		Page<WorkProduceTaskEmployee> page = new Page<WorkProduceTaskEmployee>(pageNo, pageSize);
		IPage<WorkProduceTaskEmployee> pageList = workProduceTaskEmployeeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	
	
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="执行生产任务关联计划执行人员-通过id查询", notes="执行生产任务关联计划执行人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WorkProduceTaskEmployee workProduceTaskEmployee = workProduceTaskEmployeeService.getById(id);
		return Result.ok(workProduceTaskEmployee);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param workProduceTaskEmployee
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WorkProduceTaskEmployee workProduceTaskEmployee) {
      return super.exportXls(request, workProduceTaskEmployee, WorkProduceTaskEmployee.class, "执行生产任务关联计划执行人员");
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
      return super.importExcel(request, response, WorkProduceTaskEmployee.class);
  }
}
