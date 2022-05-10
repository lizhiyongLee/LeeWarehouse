package com.ils.modules.mes.produce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import com.ils.modules.mes.produce.service.PurchaseOrderLineService;
import com.ils.modules.mes.produce.vo.PurchaseOrderLinePageVO;
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
 * @Description: 采购清单物料行
 * @Author: Tian
 * @Date:   2021-01-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="采购清单物料行")
@RestController
@RequestMapping("/produce/purchaseOrderLine")
public class PurchaseOrderLineController extends ILSController<PurchaseOrderLine, PurchaseOrderLineService> {
	@Autowired
	private PurchaseOrderLineService purchaseOrderLineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purchaseOrderLinePageVO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="采购清单物料行-分页列表查询", notes="采购清单物料行-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PurchaseOrderLinePageVO purchaseOrderLinePageVO,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
								   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurchaseOrderLinePageVO> queryWrapper = QueryGenerator.initQueryWrapper(purchaseOrderLinePageVO, req.getParameterMap());
		Page<PurchaseOrderLinePageVO> page = new Page<PurchaseOrderLinePageVO>(pageNo, pageSize);
		IPage<PurchaseOrderLinePageVO> pageList = purchaseOrderLineService.listPage(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param purchaseOrderLine
	 * @return
	 */
	@AutoLog("采购清单物料行-添加")
	@ApiOperation(value="采购清单物料行-添加", notes="采购清单物料行-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PurchaseOrderLine purchaseOrderLine) {
		purchaseOrderLineService.savePurchaseOrderLine(purchaseOrderLine);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param purchaseOrderLine
	 * @return
	 */
	@AutoLog("采购清单物料行-编辑")
	@ApiOperation(value="采购清单物料行-编辑", notes="采购清单物料行-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody PurchaseOrderLine purchaseOrderLine) {
		purchaseOrderLineService.updatePurchaseOrderLine(purchaseOrderLine);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购清单物料行-通过id删除")
	@ApiOperation(value="采购清单物料行-通过id删除", notes="采购清单物料行-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		purchaseOrderLineService.delPurchaseOrderLine(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购清单物料行-批量删除")
	@ApiOperation(value="采购清单物料行-批量删除", notes="采购清单物料行-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.purchaseOrderLineService.delBatchPurchaseOrderLine(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="采购清单物料行-通过id查询", notes="采购清单物料行-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PurchaseOrderLine purchaseOrderLine = purchaseOrderLineService.getById(id);
		return Result.ok(purchaseOrderLine);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param purchaseOrderLine
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, PurchaseOrderLine purchaseOrderLine) {
      return super.exportXls(request, purchaseOrderLine, PurchaseOrderLine.class, "采购清单物料行");
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
      return super.importExcel(request, response, PurchaseOrderLine.class);
  }
}
