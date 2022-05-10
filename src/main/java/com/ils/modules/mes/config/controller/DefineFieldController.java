package com.ils.modules.mes.config.controller;

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
import com.ils.modules.mes.config.entity.DefineField;
import com.ils.modules.mes.config.service.DefineFieldService;
import com.ils.modules.mes.config.service.DefineFieldValueService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 自定义字段
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="自定义字段")
@RestController
@RequestMapping("/config/defineField")
public class DefineFieldController extends ILSController<DefineField, DefineFieldService> {
	@Autowired
	private DefineFieldService defineFieldService;
	
    @Autowired
    private DefineFieldValueService defineFieldValueService;

	/**
	 * 分页列表查询
	 *
	 * @param defineField
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="自定义字段-分页列表查询", notes="自定义字段-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DefineField defineField,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DefineField> queryWrapper = QueryGenerator.initQueryWrapper(defineField, req.getParameterMap());

		Page<DefineField> page = new Page<DefineField>(pageNo, pageSize);
		IPage<DefineField> pageList = defineFieldService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param defineField
	 * @return
	 */
	@AutoLog("自定义字段-添加")
	@ApiOperation(value="自定义字段-添加", notes="自定义字段-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DefineField defineField) {
		defineFieldService.saveDefineField(defineField);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param defineField
	 * @return
	 */
	@AutoLog("自定义字段-编辑")
	@ApiOperation(value="自定义字段-编辑", notes="自定义字段-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody DefineField defineField) {
		defineFieldService.updateDefineField(defineField);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自定义字段-通过id删除")
	@ApiOperation(value="自定义字段-通过id删除", notes="自定义字段-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		defineFieldService.delDefineField(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "自定义字段-批量删除")
	@ApiOperation(value="自定义字段-批量删除", notes="自定义字段-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.defineFieldService.delBatchDefineField(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="自定义字段-通过id查询", notes="自定义字段-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DefineField defineField = defineFieldService.getById(id);
		return Result.ok(defineField);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param defineField
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, DefineField defineField) {
      return super.exportXls(request, defineField, DefineField.class, "自定义字段");
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
      return super.importExcel(request, response, DefineField.class);
  }
}
