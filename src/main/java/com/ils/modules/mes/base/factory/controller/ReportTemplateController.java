package com.ils.modules.mes.base.factory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import com.ils.modules.mes.base.factory.mapper.ReportTemplateMapper;
import com.ils.modules.mes.base.factory.service.ReportTemplateService;
import com.ils.modules.mes.base.factory.vo.ReportTemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 报告模板
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="报告模板")
@RestController
@RequestMapping("/base/factory/reportTemplate")
public class ReportTemplateController extends ILSController<ReportTemplate, ReportTemplateService> {
	@Autowired
	private ReportTemplateService reportTemplateService;
	@Autowired
	private ReportTemplateMapper reportTemplateMapper;


	/**
	 * 分页列表查询
	 *
	 * @param reportTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value = "RF界面配置-分页列表查询", notes = "RF界面配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReportTemplate reportTemplate,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
								   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReportTemplate> queryWrapper = QueryGenerator.initQueryWrapper(reportTemplate, req.getParameterMap());
		Page<ReportTemplate> page = new Page<ReportTemplate>(pageNo, pageSize);
		IPage<ReportTemplate> pageList = reportTemplateService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 添加
	  *
	  * @param reportTemplateVO
	  * @return
	  */
	 @ApiOperation(value = "报告模板-添加", notes = "报告模板-添加")
	 @PostMapping(value = "/add")
	 @AutoLog(logType = CommonConstant.OPERATE_TYPE_ADD,value = "报告模板-添加")
	 public Result<?> add(@RequestBody ReportTemplateVO reportTemplateVO) {
		 ReportTemplate reportTemplate = new ReportTemplate();
		 BeanUtils.copyProperties(reportTemplateVO, reportTemplate);
		 ReportTemplate reportTemplate1 = reportTemplateService.saveMain(reportTemplate, reportTemplateVO.getReportTemplateControlList());
		 return Result.ok(reportTemplate1);
	 }

	/**
	 * 编辑
	 *
	 * @param reportTemplateVO
	 * @return
	 */
	@ApiOperation(value = "报告模板-编辑", notes = "报告模板-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("报告模板-编辑")
	public Result<?> edit(@RequestBody ReportTemplateVO reportTemplateVO) {
		ReportTemplate reportTemplate = new ReportTemplate();
		BeanUtils.copyProperties(reportTemplateVO, reportTemplate);
		reportTemplateService.updateMain(reportTemplate, reportTemplateVO.getReportTemplateControlList());
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}

	/**
	 * 复制界面
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "报告模板-复制", notes = "报告模板-复制")
	@GetMapping(value = "/copy")
	public Result<?> copy(@RequestParam(name = "id", required = true) String id) {
		reportTemplateService.copy(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报告模板-通过id删除")
	@ApiOperation(value = "报告模板-通过id删除", notes = "报告模板-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		reportTemplateService.delMain(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报告模板-批量删除")
	@ApiOperation(value = "报告模板-批量删除", notes = "报告模板-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.reportTemplateService.delBatchMain(Arrays.asList(ids.split(",")));
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "报告模板-通过报告模板id查询报告子控件明细", notes = "报告模板-通过报告模板id查询报告子控件明细")
	@GetMapping(value = "/queryReportTemplateDetailByMainId")
	public Result<?> queryReportTemplateDetailByMainId(@RequestParam(name = "id", required = true) String id) {
		List<ReportTemplateControl> reportTemplateControlList = reportTemplateService.selectByMainId(id);
		return Result.ok(reportTemplateControlList);
	}
	@ApiOperation(value = "报告模板-通过报告模板id查询报告模板明细包括子控件", notes = "报告模板-通过报告模板id查询报告模板明细包括子控件")
	@GetMapping(value = "/querReportTemplateWithTask")
	public Result<?> querReportTemplateWithTask(@RequestParam(name = "id", required = true) String id){
		List<ReportTemplateControl> reportTemplateControlList = reportTemplateService.selectByMainId(id);
		ReportTemplate reportTemplate = reportTemplateMapper.selectById(id);
		ReportTemplateVO reportTemplateVO = new ReportTemplateVO();
		BeanUtils.copyProperties(reportTemplate,reportTemplateVO);
		reportTemplateVO.setReportTemplateControlList(reportTemplateControlList);
		return Result.ok(reportTemplateVO);
	}

	@ApiOperation(value = "报告模板-查询所有的报告模板id和名字", notes = "故障现象-查询所有的报告模板id和名字")
	@GetMapping(value = "/mesTemplateDict")
	public Result<?> queryDictTemplate() {
		List<DictModel> dictModels = reportTemplateService.queryDictTemplate();
		return Result.ok(dictModels);
	}
}
