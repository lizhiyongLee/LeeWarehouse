package com.ils.modules.mes.material.controller;

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
import com.ils.modules.mes.material.entity.ItemTakeDeliveryRecord;
import com.ils.modules.mes.material.service.ItemTakeDeliveryRecordService;
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
 * @Description: 收货记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="收货记录")
@RestController
@RequestMapping("/material/itemTakeDeliveryRecord")
public class ItemTakeDeliveryRecordController extends ILSController<ItemTakeDeliveryRecord, ItemTakeDeliveryRecordService> {
	@Autowired
	private ItemTakeDeliveryRecordService itemTakeDeliveryRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itemTakeDeliveryRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="收货记录-分页列表查询", notes="收货记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ItemTakeDeliveryRecord itemTakeDeliveryRecord,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItemTakeDeliveryRecord> queryWrapper = QueryGenerator.initQueryWrapper(itemTakeDeliveryRecord, req.getParameterMap());
		Page<ItemTakeDeliveryRecord> page = new Page<ItemTakeDeliveryRecord>(pageNo, pageSize);
		IPage<ItemTakeDeliveryRecord> pageList = itemTakeDeliveryRecordService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param itemTakeDeliveryRecord
	 * @return
	 */
	@AutoLog("收货记录-添加")
	@ApiOperation(value="收货记录-添加", notes="收货记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ItemTakeDeliveryRecord itemTakeDeliveryRecord) {
		itemTakeDeliveryRecordService.saveItemTakeDeliveryRecord(itemTakeDeliveryRecord);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param itemTakeDeliveryRecord
	 * @return
	 */
	@AutoLog("收货记录-编辑")
	@ApiOperation(value="收货记录-编辑", notes="收货记录-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody ItemTakeDeliveryRecord itemTakeDeliveryRecord) {
		itemTakeDeliveryRecordService.updateItemTakeDeliveryRecord(itemTakeDeliveryRecord);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收货记录-通过id删除")
	@ApiOperation(value="收货记录-通过id删除", notes="收货记录-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		itemTakeDeliveryRecordService.delItemTakeDeliveryRecord(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "收货记录-批量删除")
	@ApiOperation(value="收货记录-批量删除", notes="收货记录-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.itemTakeDeliveryRecordService.delBatchItemTakeDeliveryRecord(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="收货记录-通过id查询", notes="收货记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ItemTakeDeliveryRecord itemTakeDeliveryRecord = itemTakeDeliveryRecordService.getById(id);
		return Result.ok(itemTakeDeliveryRecord);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param itemTakeDeliveryRecord
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ItemTakeDeliveryRecord itemTakeDeliveryRecord) {
      return super.exportXls(request, itemTakeDeliveryRecord, ItemTakeDeliveryRecord.class, "收货记录");
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
      return super.importExcel(request, response, ItemTakeDeliveryRecord.class);
  }
}
