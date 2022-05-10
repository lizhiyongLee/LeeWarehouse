package com.ils.modules.mes.base.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.machine.entity.SparePartsHouse;
import com.ils.modules.mes.base.machine.service.SparePartsHouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 /**
 * @Description: 备件库
 * @Author: Tian
 * @Date:   2021-02-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="备件库")
@RestController
@RequestMapping("/base/machine/sparePartsHouse")
public class SparePartsHouseController extends ILSController<SparePartsHouse, SparePartsHouseService> {
	@Autowired
	private SparePartsHouseService sparePartsHouseService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sparePartsHouse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="备件库-分页列表查询", notes="备件库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SparePartsHouse sparePartsHouse,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SparePartsHouse> queryWrapper = QueryGenerator.initQueryWrapper(sparePartsHouse, req.getParameterMap());
		Page<SparePartsHouse> page = new Page<SparePartsHouse>(pageNo, pageSize);
		IPage<SparePartsHouse> pageList = sparePartsHouseService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sparePartsHouse
	 * @return
	 */
	@AutoLog("备件库-添加")
	@ApiOperation(value="备件库-添加", notes="备件库-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SparePartsHouse sparePartsHouse) {
		sparePartsHouseService.saveSparePartsHouse(sparePartsHouse);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param sparePartsHouse
	 * @return
	 */
	@AutoLog("备件库-编辑")
	@ApiOperation(value="备件库-编辑", notes="备件库-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody SparePartsHouse sparePartsHouse) {
		sparePartsHouseService.updateSparePartsHouse(sparePartsHouse);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="备件库-通过id查询", notes="备件库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SparePartsHouse sparePartsHouse = sparePartsHouseService.getById(id);
		return Result.ok(sparePartsHouse);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sparePartsHouse
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SparePartsHouse sparePartsHouse) {
      return super.exportXls(request, sparePartsHouse, SparePartsHouse.class, "备件库");
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
      return super.importExcel(request, response, SparePartsHouse.class);
  }
}
