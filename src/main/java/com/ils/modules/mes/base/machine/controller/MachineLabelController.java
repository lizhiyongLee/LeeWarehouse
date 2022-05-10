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
import com.ils.modules.mes.base.machine.entity.MachineLabel;
import com.ils.modules.mes.base.machine.service.MachineLabelService;
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
 * @Description: 设备标签
 * @Author: Tian
 * @Date:   2020-10-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备标签")
@RestController
@RequestMapping("/base/machine/machineLabel")
public class MachineLabelController extends ILSController<MachineLabel, MachineLabelService> {
	@Autowired
	private MachineLabelService machineLabelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param machineLabel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="设备标签-分页列表查询", notes="设备标签-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MachineLabel machineLabel,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MachineLabel> queryWrapper = QueryGenerator.initQueryWrapper(machineLabel, req.getParameterMap());
		Page<MachineLabel> page = new Page<MachineLabel>(pageNo, pageSize);
		IPage<MachineLabel> pageList = machineLabelService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param machineLabel
	 * @return
	 */
	@ApiOperation(value="设备标签-添加", notes="设备标签-添加")
	@PostMapping(value = "/add")
	@AutoLog("设备标签-添加")
	public Result<?> add(@RequestBody MachineLabel machineLabel) {
		machineLabelService.saveMachineLabel(machineLabel);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param machineLabel
	 * @return
	 */
	@ApiOperation(value="设备标签-编辑", notes="设备标签-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("设备标签-编辑")
	public Result<?> edit(@RequestBody MachineLabel machineLabel) {
		machineLabelService.updateMachineLabel(machineLabel);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="设备标签-通过id查询", notes="设备标签-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MachineLabel machineLabel = machineLabelService.getById(id);
		return Result.ok(machineLabel);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param machineLabel
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, MachineLabel machineLabel) {
      return super.exportXls(request, machineLabel, MachineLabel.class, "设备标签");
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
      return super.importExcel(request, response, MachineLabel.class);
  }
}
