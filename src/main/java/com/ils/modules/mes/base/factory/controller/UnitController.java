package com.ils.modules.mes.base.factory.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.util.CommonUtil;
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
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 计量单位
 * @Author: fengyi
 * @Date: 2020-10-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="计量单位")
@RestController
@RequestMapping("/base/factory/unit")
public class UnitController extends ILSController<Unit, UnitService> {
	@Autowired
	private UnitService unitService;
	
	/**
	 * 分页列表查询
	 *
	 * @param unit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="计量单位-分页列表查询", notes="计量单位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Unit unit,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Unit> queryWrapper = QueryGenerator.initQueryWrapper(unit, req.getParameterMap());

		Page<Unit> page = new Page<Unit>(pageNo, pageSize);
		IPage<Unit> pageList = unitService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param unit
	 * @return
	 */
	@ApiOperation(value="计量单位-添加", notes="计量单位-添加")
	@PostMapping(value = "/add")
	@AutoLog("计量单位-添加")
	public Result<?> add(@RequestBody Unit unit) {
        unit.setTenantId(CommonUtil.getTenantId());
		unitService.saveUnit(unit);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param unit
	 * @return
	 */
	@ApiOperation(value="计量单位-编辑", notes="计量单位-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("计量单位-编辑")
	public Result<?> edit(@RequestBody Unit unit) {
		unitService.updateUnit(unit);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="计量单位-通过id查询", notes="计量单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Unit unit = unitService.getById(id);
		return Result.ok(unit);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param unit
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Unit unit) {
      return super.exportXls(request, unit, Unit.class, "计量单位");
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
		 return super.importExcel(request, response, Unit.class);
	 }
}
