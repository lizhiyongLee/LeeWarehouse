package com.ils.modules.mes.base.qc.controller;

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
import com.ils.modules.mes.base.qc.entity.QcItemType;
import com.ils.modules.mes.base.qc.service.QcItemTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 质检项分类
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="质检项分类")
@RestController
@RequestMapping("/base/qc/qcItemType")
public class QcItemTypeController extends ILSController<QcItemType, QcItemTypeService> {
	@Autowired
	private QcItemTypeService qcItemTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param qcItemType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="质检项分类-分页列表查询", notes="质检项分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(QcItemType qcItemType,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<QcItemType> queryWrapper = QueryGenerator.initQueryWrapper(qcItemType, req.getParameterMap());

		Page<QcItemType> page = new Page<QcItemType>(pageNo, pageSize);
		IPage<QcItemType> pageList = qcItemTypeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param qcItemType
	 * @return
	 */
	@AutoLog("质检项分类-添加")
	@ApiOperation(value="质检项分类-添加", notes="质检项分类-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody QcItemType qcItemType) {
        qcItemType.setTenantId(CommonUtil.getTenantId());
		qcItemTypeService.saveQcItemType(qcItemType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param qcItemType
	 * @return
	 */
	@AutoLog("质检项分类-编辑")
	@ApiOperation(value="质检项分类-编辑", notes="质检项分类-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody QcItemType qcItemType) {
		qcItemTypeService.updateQcItemType(qcItemType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "质检项分类-通过id删除")
	@ApiOperation(value="质检项分类-通过id删除", notes="质检项分类-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		qcItemTypeService.delQcItemType(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "质检项分类-批量删除")
	@ApiOperation(value="质检项分类-批量删除", notes="质检项分类-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.qcItemTypeService.delBatchQcItemType(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="质检项分类-通过id查询", notes="质检项分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		QcItemType qcItemType = qcItemTypeService.getById(id);
		return Result.ok(qcItemType);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param qcItemType
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, QcItemType qcItemType) {
      return super.exportXls(request, qcItemType, QcItemType.class, "质检项分类");
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
      return super.importExcel(request, response, QcItemType.class);
  }
}
