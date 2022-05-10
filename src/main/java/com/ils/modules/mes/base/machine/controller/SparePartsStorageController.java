package com.ils.modules.mes.base.machine.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.machine.service.SparePartsStorageService;
import com.ils.modules.mes.base.machine.vo.SparePartsStoargeNodeTreeVO;
import com.ils.modules.mes.base.machine.vo.SparePartsStorageListVO;
import com.ils.modules.mes.base.machine.vo.SparePartsStorageVO;
import com.ils.modules.mes.enums.ItemQcStatusEnum;
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
 * @Description: 备件库位
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="备件库位")
@RestController
@RequestMapping("/base/machine/sparePartsStorage")
public class SparePartsStorageController extends ILSController<SparePartsStorage, SparePartsStorageService> {
	@Autowired
	private SparePartsStorageService sparePartsStorageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sparePartsStorage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="备件库位-分页列表查询", notes="备件库位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SparePartsStorage sparePartsStorage,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SparePartsStorage> queryWrapper = QueryGenerator.initQueryWrapper(sparePartsStorage, req.getParameterMap());
		Page<SparePartsStorage> page = new Page<SparePartsStorage>(pageNo, pageSize);
		IPage<SparePartsStorage> pageList = sparePartsStorageService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @ApiOperation(value = "备件库位-分页列表查询", notes = "备件库位-分页列表查询")
	 @GetMapping(value = "/rootList")
	 public Result<?> queryPageList(
			 @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			 @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
			 HttpServletRequest req) {
		 Page<SparePartsStorageVO> page = new Page<SparePartsStorageVO>(pageNo, pageSize);
		 IPage<SparePartsStorageVO> pageList = sparePartsStorageService.querySparePartsHouse(page);
		 return Result.ok(pageList);
	 }

	 /**
	  * @param upStorageId
	  * @return
	  */
	 @ApiOperation(value = "通过childid查询", notes = "通过childid查询")
	 @GetMapping(value = "/childList")
	 public Result<?> queryByChildList(@RequestParam(name = "upStorageId", required = false) String upStorageId) {
		 if (upStorageId.isEmpty()) {
			 upStorageId = "0";
		 }
		 List<SparePartsStorageVO> sparePartsStorageVOList = sparePartsStorageService.selcetChildList(upStorageId);
		 SparePartsStorageListVO sparePartsStorageListVO = new SparePartsStorageListVO();
		 sparePartsStorageListVO.setSparePartsStorageVOList(sparePartsStorageVOList);
		 return Result.ok(sparePartsStorageListVO);
	 }
	 /**
	  * 查询仓位上级区域，以树形结构展示
	  *
	  * @return
	  */
	 @ApiOperation(value = "查询上级区域，以树形结构展示-树形查询", notes = "查询上级区域，以树形结构展示-树形查询")
	 @RequestMapping(value = "/queryShopLineTreeList", method = RequestMethod.GET)
	 public Result<?> queryShopLineTreeList() {
		 List<SparePartsStoargeNodeTreeVO> sparePartsStoargeNodeTreeVOList = sparePartsStorageService.queryWareStorageTreeList();
		 return Result.ok(sparePartsStoargeNodeTreeVOList);
	 }
	/**
	 * 添加
	 *
	 * @param sparePartsStorage
	 * @return
	 */
	@AutoLog("备件库位-添加")
	@ApiOperation(value="备件库位-添加", notes="备件库位-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SparePartsStorage sparePartsStorage) {
		sparePartsStorageService.saveSparePartsStorage(sparePartsStorage);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param sparePartsStorage
	 * @return
	 */
	@ApiOperation(value="备件库位-编辑", notes="备件库位-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("备件库位-编辑")
	public Result<?> edit(@RequestBody SparePartsStorage sparePartsStorage) {
		sparePartsStorageService.updateSparePartsStorage(sparePartsStorage);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="备件库位-通过id查询", notes="备件库位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SparePartsStorage sparePartsStorage = sparePartsStorageService.getById(id);
		SparePartsStorageVO sparePartsStorageVO = new SparePartsStorageVO();
		BeanUtil.copyProperties(sparePartsStorage,sparePartsStorageVO);
		sparePartsStorageVO.setQcStatusName(sparePartsStorage.getQcStatus().replace(ItemQcStatusEnum.QUALIFIED.getValue(),ItemQcStatusEnum.QUALIFIED.getDesc())
				.replace(ItemQcStatusEnum.WAIT_TEST.getValue(),ItemQcStatusEnum.WAIT_TEST.getDesc())
				.replace(ItemQcStatusEnum.UNQUALIFIED.getValue(),ItemQcStatusEnum.UNQUALIFIED.getDesc()));
		return Result.ok(sparePartsStorageVO);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sparePartsStorage
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SparePartsStorage sparePartsStorage) {
      return super.exportXls(request, sparePartsStorage, SparePartsStorage.class, "备件库位");
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
      return super.importExcel(request, response, SparePartsStorage.class);
  }
}
