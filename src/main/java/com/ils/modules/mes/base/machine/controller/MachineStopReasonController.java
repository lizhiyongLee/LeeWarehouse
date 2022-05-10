package com.ils.modules.mes.base.machine.controller;

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
import com.ils.modules.mes.base.machine.entity.MachineStopReason;
import com.ils.modules.mes.base.machine.service.MachineStopReasonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 停机原因
 * @Author: Tian
 * @Date:   2020-10-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="停机原因")
@RestController
@RequestMapping("/base/machine/machineStopReason")

public class MachineStopReasonController extends ILSController<MachineStopReason, MachineStopReasonService> {
	@Autowired
	private MachineStopReasonService machineStopReasonService;
	
	/**
	 * 分页列表查询
	 *
	 * @param machineStopReason
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="停机原因-分页列表查询", notes="停机原因-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MachineStopReason machineStopReason,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MachineStopReason> queryWrapper = QueryGenerator.initQueryWrapper(machineStopReason, req.getParameterMap());
		Page<MachineStopReason> page = new Page<MachineStopReason>(pageNo, pageSize);
		IPage<MachineStopReason> pageList = machineStopReasonService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
     * 列表查询
     *
     * @param machineStopReason
     * @param machineStopReason
     * @param req
     * @param req
     * @return
     */
    @ApiOperation(value = "停机原因-列表查询", notes = "停机原因-列表查询")
    @GetMapping(value = "/reasonList")
    public Result<?> reasonList(MachineStopReason machineStopReason, HttpServletRequest req) {
        QueryWrapper<MachineStopReason> queryWrapper =
            QueryGenerator.initQueryWrapper(machineStopReason, req.getParameterMap());
        List<MachineStopReason> lstMachineStopReason = machineStopReasonService.list(queryWrapper);
        return Result.ok(lstMachineStopReason);
    }

    /**
     * 添加
     *
     * @param machineStopReason
     * @return
     */
	@AutoLog("停机原因-添加")
	@ApiOperation(value="停机原因-添加", notes="停机原因-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MachineStopReason machineStopReason) {
		machineStopReasonService.saveMachineStopReason(machineStopReason);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param machineStopReason
	 * @return
	 */
	@AutoLog("停机原因-编辑")
	@ApiOperation(value="停机原因-编辑", notes="停机原因-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody MachineStopReason machineStopReason) {
		machineStopReasonService.updateMachineStopReason(machineStopReason);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="停机原因-通过id查询", notes="停机原因-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MachineStopReason machineStopReason = machineStopReasonService.getById(id);
		return Result.ok(machineStopReason);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param machineStopReason
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, MachineStopReason machineStopReason) {
      return super.exportXls(request, machineStopReason, MachineStopReason.class, "停机原因");
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
      return super.importExcel(request, response, MachineStopReason.class);
  }
}
