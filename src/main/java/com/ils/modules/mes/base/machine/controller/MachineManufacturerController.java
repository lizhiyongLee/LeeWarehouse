package com.ils.modules.mes.base.machine.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ils.common.api.vo.Result;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.base.machine.entity.MachineManufacturer;
import com.ils.modules.mes.base.machine.service.MachineManufacturerService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.constant.CommonConstant;
import com.ils.framework.poi.excel.ExcelImportUtil;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.ImportParams;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.ils.common.system.base.controller.ILSController;

 /**
 * @Description: 设备制造商
 * @Author: Tian
 * @Date:   2020-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备制造商")
@RestController
@RequestMapping("/base/machine/machineManufacturer")
public class MachineManufacturerController extends ILSController<MachineManufacturer, MachineManufacturerService> {
	@Autowired
	private MachineManufacturerService machineManufacturerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param machineManufacturer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备制造商-分页列表查询", notes="设备制造商-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MachineManufacturer machineManufacturer,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MachineManufacturer> queryWrapper = QueryGenerator.initQueryWrapper(machineManufacturer, req.getParameterMap());
		Page<MachineManufacturer> page = new Page<MachineManufacturer>(pageNo, pageSize);
		IPage<MachineManufacturer> pageList = machineManufacturerService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param machineManufacturer
	 * @return
	 */
	@AutoLog("设备制造商-添加")
	@ApiOperation(value="设备制造商-添加", notes="设备制造商-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MachineManufacturer machineManufacturer) {
		machineManufacturerService.saveMachineManufacturer(machineManufacturer);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param machineManufacturer
	 * @return
	 */
	@AutoLog("设备制造商-编辑")
	@ApiOperation(value="设备制造商-编辑", notes="设备制造商-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody MachineManufacturer machineManufacturer) {
		machineManufacturerService.updateMachineManufacturer(machineManufacturer);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备制造商-通过id查询", notes="设备制造商-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MachineManufacturer machineManufacturer = machineManufacturerService.getById(id);
		return Result.ok(machineManufacturer);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param machineManufacturer
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, MachineManufacturer machineManufacturer) {
      return super.exportXls(request, machineManufacturer, MachineManufacturer.class, "设备制造商");
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
      return super.importExcel(request, response, MachineManufacturer.class);
  }
}
