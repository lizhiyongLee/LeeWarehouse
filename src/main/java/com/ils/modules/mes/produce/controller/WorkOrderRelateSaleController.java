package com.ils.modules.mes.produce.controller;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
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
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.produce.entity.WorkOrderRelateSale;
import com.ils.modules.mes.produce.service.WorkOrderRelateSaleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 工单关联销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工单关联销售订单物料行")
@RestController
@RequestMapping("/produce/workOrderRelateSale")
public class WorkOrderRelateSaleController extends ILSController<WorkOrderRelateSale, WorkOrderRelateSaleService> {
	@Autowired
	private WorkOrderRelateSaleService workOrderRelateSaleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param workOrderRelateSale
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="工单关联销售订单物料行-分页列表查询", notes="工单关联销售订单物料行-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WorkOrderRelateSale workOrderRelateSale,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WorkOrderRelateSale> queryWrapper = QueryGenerator.initQueryWrapper(workOrderRelateSale, req.getParameterMap());
		Page<WorkOrderRelateSale> page = new Page<WorkOrderRelateSale>(pageNo, pageSize);
		IPage<WorkOrderRelateSale> pageList = workOrderRelateSaleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param workOrderRelateSale
	 * @return
	 */
	@AutoLog("工单关联销售订单物料行-添加")
	@ApiOperation(value="工单关联销售订单物料行-添加", notes="工单关联销售订单物料行-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WorkOrderRelateSale workOrderRelateSale) {
		workOrderRelateSaleService.saveWorkOrderRelateSale(workOrderRelateSale);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	
}
