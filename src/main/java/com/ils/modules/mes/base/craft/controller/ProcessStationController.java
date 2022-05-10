package com.ils.modules.mes.base.craft.controller;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.craft.entity.ProcessStation;
import com.ils.modules.mes.base.craft.service.ProcessStationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 工序关联工位
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工序关联工位")
@RestController
@RequestMapping("/base/craft/processStation")
public class ProcessStationController extends ILSController<ProcessStation, ProcessStationService> {
	@Autowired
	private ProcessStationService processStationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param processStation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="工序关联工位-分页列表查询", notes="工序关联工位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProcessStation processStation,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProcessStation> queryWrapper = QueryGenerator.initQueryWrapper(processStation, req.getParameterMap());
		Page<ProcessStation> page = new Page<ProcessStation>(pageNo, pageSize);
		IPage<ProcessStation> pageList = processStationService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param processStation
	 * @return
	 */
	@ApiOperation(value="工序关联工位-添加", notes="工序关联工位-添加")
	@PostMapping(value = "/add")
	@AutoLog("工序关联工位-添加")
	public Result<?> add(@RequestBody ProcessStation processStation) {
		processStationService.saveProcessStation(processStation);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param processStation
	 * @return
	 */
	@ApiOperation(value="工序关联工位-编辑", notes="工序关联工位-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("工序关联工位-编辑")
	public Result<?> edit(@RequestBody ProcessStation processStation) {
		processStationService.updateProcessStation(processStation);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="工序关联工位-通过id查询", notes="工序关联工位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProcessStation processStation = processStationService.getById(id);
		return Result.ok(processStation);
	}


}
