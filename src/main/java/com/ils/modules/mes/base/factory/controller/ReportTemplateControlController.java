package com.ils.modules.mes.base.factory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import com.ils.modules.mes.base.factory.service.ReportTemplateControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 报告模板控件
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="报告模板控件")
@RestController
@RequestMapping("/base/factory/reportTemplateControl")
public class ReportTemplateControlController extends ILSController<ReportTemplateControl, ReportTemplateControlService> {
	@Autowired
	private ReportTemplateControlService reportTemplateControlService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reportTemplateControl
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="报告模板控件-分页列表查询", notes="报告模板控件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReportTemplateControl reportTemplateControl,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReportTemplateControl> queryWrapper = QueryGenerator.initQueryWrapper(reportTemplateControl, req.getParameterMap());
		Page<ReportTemplateControl> page = new Page<ReportTemplateControl>(pageNo, pageSize);
		IPage<ReportTemplateControl> pageList = reportTemplateControlService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param reportTemplateControl
	 * @return
	 */
	@ApiOperation(value="报告模板控件-添加", notes="报告模板控件-添加")
	@PostMapping(value = "/add")
	@AutoLog("报告模板控件-添加")
	public Result<?> add(@RequestBody ReportTemplateControl reportTemplateControl) {
		reportTemplateControlService.saveReportTemplateControl(reportTemplateControl);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param reportTemplateControl
	 * @return
	 */
	@ApiOperation(value="报告模板控件-编辑", notes="报告模板控件-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("工厂-编辑")
	public Result<?> edit(@RequestBody ReportTemplateControl reportTemplateControl) {
		reportTemplateControlService.updateReportTemplateControl(reportTemplateControl);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报告模板控件-通过id删除")
	@ApiOperation(value="报告模板控件-通过id删除", notes="报告模板控件-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		reportTemplateControlService.delReportTemplateControl(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报告模板控件-批量删除")
	@ApiOperation(value="报告模板控件-批量删除", notes="报告模板控件-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.reportTemplateControlService.delBatchReportTemplateControl(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="报告模板控件-通过id查询", notes="报告模板控件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ReportTemplateControl reportTemplateControl = reportTemplateControlService.getById(id);
		return Result.ok(reportTemplateControl);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param reportTemplateControl
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ReportTemplateControl reportTemplateControl) {
      return super.exportXls(request, reportTemplateControl, ReportTemplateControl.class, "报告模板控件");
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
      return super.importExcel(request, response, ReportTemplateControl.class);
  }
}
