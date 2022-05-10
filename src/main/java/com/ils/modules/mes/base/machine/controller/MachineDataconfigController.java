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
import com.ils.modules.mes.base.machine.entity.MachineDataconfig;
import com.ils.modules.mes.base.machine.service.MachineDataconfigService;
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
 * @Description: 读数配置
 * @Author: Tian
 * @Date:   2020-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="读数配置")
@RestController
@RequestMapping("/base/machine/machineDataconfig")
public class MachineDataconfigController extends ILSController<MachineDataconfig, MachineDataconfigService> {
	@Autowired
	private MachineDataconfigService machineDataconfigService;
	
	/**
	 * 分页列表查询
	 *
	 * @param machineDataconfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="读数配置-分页列表查询", notes="读数配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MachineDataconfig machineDataconfig,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MachineDataconfig> queryWrapper = QueryGenerator.initQueryWrapper(machineDataconfig, req.getParameterMap());
		Page<MachineDataconfig> page = new Page<MachineDataconfig>(pageNo, pageSize);
		IPage<MachineDataconfig> pageList = machineDataconfigService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param machineDataconfig
	 * @return
	 */
	@ApiOperation(value="读数配置-添加", notes="读数配置-添加")
	@PostMapping(value = "/add")
	@AutoLog("读数配置-添加")
	public Result<?> add(@RequestBody MachineDataconfig machineDataconfig) {
		machineDataconfigService.saveMachineDataconfig(machineDataconfig);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param machineDataconfig
	 * @return
	 */
	@ApiOperation(value="读数配置-编辑", notes="读数配置-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("读数配置-编辑")
	public Result<?> edit(@RequestBody MachineDataconfig machineDataconfig) {
		machineDataconfigService.updateMachineDataconfig(machineDataconfig);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="读数配置-通过id查询", notes="读数配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MachineDataconfig machineDataconfig = machineDataconfigService.getById(id);
		return Result.ok(machineDataconfig);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param machineDataconfig
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, MachineDataconfig machineDataconfig) {
      return super.exportXls(request, machineDataconfig, MachineDataconfig.class, "读数配置");
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
      return super.importExcel(request, response, MachineDataconfig.class);
  }
}
