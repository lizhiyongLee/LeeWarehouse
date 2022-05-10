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
import com.ils.modules.mes.base.qc.entity.NgItemType;
import com.ils.modules.mes.base.qc.service.NgItemTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 不良类型
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="不良类型")
@RestController
@RequestMapping("/base/qc/ngItemType")
public class NgItemTypeController extends ILSController<NgItemType, NgItemTypeService> {
	@Autowired
	private NgItemTypeService ngItemTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ngItemType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="不良类型-分页列表查询", notes="不良类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NgItemType ngItemType,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NgItemType> queryWrapper = QueryGenerator.initQueryWrapper(ngItemType, req.getParameterMap());

		Page<NgItemType> page = new Page<NgItemType>(pageNo, pageSize);
		IPage<NgItemType> pageList = ngItemTypeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param ngItemType
	 * @return
	 */
	@AutoLog("不良类型-添加")
	@ApiOperation(value="不良类型-添加", notes="不良类型-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NgItemType ngItemType) {
        ngItemType.setTenantId(CommonUtil.getTenantId());
		ngItemTypeService.saveNgItemType(ngItemType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param ngItemType
	 * @return
	 */
	@AutoLog("不良类型-编辑")
	@ApiOperation(value="不良类型-编辑", notes="不良类型-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody NgItemType ngItemType) {
		ngItemTypeService.updateNgItemType(ngItemType);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "不良类型-通过id删除")
	@ApiOperation(value="不良类型-通过id删除", notes="不良类型-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ngItemTypeService.delNgItemType(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "不良类型-批量删除")
	@ApiOperation(value="不良类型-批量删除", notes="不良类型-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.ngItemTypeService.delBatchNgItemType(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="不良类型-通过id查询", notes="不良类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NgItemType ngItemType = ngItemTypeService.getById(id);
		return Result.ok(ngItemType);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param ngItemType
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, NgItemType ngItemType) {
      return super.exportXls(request, ngItemType, NgItemType.class, "不良类型");
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
      return super.importExcel(request, response, NgItemType.class);
  }
}
