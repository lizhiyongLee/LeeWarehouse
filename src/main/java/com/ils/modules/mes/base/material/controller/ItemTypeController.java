package com.ils.modules.mes.base.material.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.modules.mes.util.CommonUtil;
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
import com.ils.modules.mes.base.material.entity.ItemType;
import com.ils.modules.mes.base.material.service.ItemTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 物料类型
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
@Slf4j
@Api(tags="物料类型")
@RestController
@RequestMapping("/base/material/itemType")
public class ItemTypeController extends ILSController<ItemType, ItemTypeService> {
	@Autowired
	private ItemTypeService itemTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itemType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="物料类型-分页列表查询", notes="物料类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ItemType itemType,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItemType> queryWrapper = QueryGenerator.initQueryWrapper(itemType, req.getParameterMap());
		Page<ItemType> page = new Page<ItemType>(pageNo, pageSize);
		IPage<ItemType> pageList = itemTypeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param itemType
	 * @return
	 */
	@AutoLog("物料类型-添加")
	@ApiOperation(value="物料类型-添加", notes="物料类型-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ItemType itemType) {
        itemType.setTenantId(CommonUtil.getTenantId());
		itemTypeService.saveItemType(itemType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param itemType
	 * @return
	 */
	@AutoLog("物料类型-编辑")
	@ApiOperation(value="物料类型-编辑", notes="物料类型-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody ItemType itemType) {
		itemTypeService.updateItemType(itemType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料类型-通过id删除")
	@ApiOperation(value="物料类型-通过id删除", notes="物料类型-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		itemTypeService.delItemType(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料类型-批量删除")
	@ApiOperation(value="物料类型-批量删除", notes="物料类型-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.itemTypeService.delBatchItemType(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="物料类型-通过id查询", notes="物料类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ItemType itemType = itemTypeService.getById(id);
		return Result.ok(itemType);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param itemType
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ItemType itemType) {
      return super.exportXls(request, itemType, ItemType.class, "物料类型");
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
      return super.importExcel(request, response, ItemType.class);
  }
}
