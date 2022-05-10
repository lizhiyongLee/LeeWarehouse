package com.ils.modules.mes.base.schedule.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ils.modules.mes.base.schedule.entity.SchedulePrepareTime;
import com.ils.modules.mes.base.schedule.service.SchedulePrepareTimeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 动态准备时间
 * @Author: fengyi
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="动态准备时间")
@RestController
@RequestMapping("/base/schedule/schedulePrepareTime")
public class SchedulePrepareTimeController extends ILSController<SchedulePrepareTime, SchedulePrepareTimeService> {
	@Autowired
	private SchedulePrepareTimeService schedulePrepareTimeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param schedulePrepareTime
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="动态准备时间-分页列表查询", notes="动态准备时间-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SchedulePrepareTime schedulePrepareTime,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SchedulePrepareTime> queryWrapper = QueryGenerator.initQueryWrapper(schedulePrepareTime, req.getParameterMap());
		Page<SchedulePrepareTime> page = new Page<SchedulePrepareTime>(pageNo, pageSize);
		IPage<SchedulePrepareTime> pageList = schedulePrepareTimeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param schedulePrepareTime
	 * @return
	 */
	@AutoLog("动态准备时间-添加")
	@ApiOperation(value="动态准备时间-添加", notes="动态准备时间-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SchedulePrepareTime schedulePrepareTime) {
		schedulePrepareTimeService.saveSchedulePrepareTime(schedulePrepareTime);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param schedulePrepareTime
	 * @return
	 */
	@AutoLog("动态准备时间-编辑")
	@ApiOperation(value="动态准备时间-编辑", notes="动态准备时间-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody SchedulePrepareTime schedulePrepareTime) {
		schedulePrepareTimeService.updateSchedulePrepareTime(schedulePrepareTime);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "动态准备时间-通过id删除")
	@ApiOperation(value="动态准备时间-通过id删除", notes="动态准备时间-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		schedulePrepareTimeService.delSchedulePrepareTime(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "动态准备时间-批量删除")
	@ApiOperation(value="动态准备时间-批量删除", notes="动态准备时间-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.schedulePrepareTimeService.delBatchSchedulePrepareTime(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="动态准备时间-通过id查询", notes="动态准备时间-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SchedulePrepareTime schedulePrepareTime = schedulePrepareTimeService.getById(id);
		return Result.ok(schedulePrepareTime);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param schedulePrepareTime
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SchedulePrepareTime schedulePrepareTime) {
      return super.exportXls(request, schedulePrepareTime, SchedulePrepareTime.class, "动态准备时间");
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
      return super.importExcel(request, response, SchedulePrepareTime.class);
  }
}
