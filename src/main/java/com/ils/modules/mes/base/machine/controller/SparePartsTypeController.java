package com.ils.modules.mes.base.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.machine.entity.SparePartsType;
import com.ils.modules.mes.base.machine.service.SparePartsTypeService;
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
 * @Description: 备件类型
 * @Author: Tian
 * @Date:   2021-02-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="备件类型")
@RestController
@RequestMapping("/base/machine/sparePartsType")
public class SparePartsTypeController extends ILSController<SparePartsType, SparePartsTypeService> {
	@Autowired
	private SparePartsTypeService sparePartsTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sparePartsType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="备件类型-分页列表查询", notes="备件类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SparePartsType sparePartsType,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SparePartsType> queryWrapper = QueryGenerator.initQueryWrapper(sparePartsType, req.getParameterMap());
		Page<SparePartsType> page = new Page<SparePartsType>(pageNo, pageSize);
		IPage<SparePartsType> pageList = sparePartsTypeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sparePartsType
	 * @return
	 */
	@AutoLog("备件类型-添加")
	@ApiOperation(value="备件类型-添加", notes="备件类型-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SparePartsType sparePartsType) {
		sparePartsTypeService.saveSparePartsType(sparePartsType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param sparePartsType
	 * @return
	 */
	@AutoLog("备件类型-编辑")
	@ApiOperation(value="备件类型-编辑", notes="备件类型-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody SparePartsType sparePartsType) {
		sparePartsTypeService.updateSparePartsType(sparePartsType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="备件类型-通过id查询", notes="备件类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SparePartsType sparePartsType = sparePartsTypeService.getById(id);
		return Result.ok(sparePartsType);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sparePartsType
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SparePartsType sparePartsType) {
      return super.exportXls(request, sparePartsType, SparePartsType.class, "备件类型");
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
      return super.importExcel(request, response, SparePartsType.class);
  }
}
