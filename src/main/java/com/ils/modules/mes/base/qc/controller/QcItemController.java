package com.ils.modules.mes.base.qc.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.ils.modules.mes.base.qc.entity.QcItem;
import com.ils.modules.mes.base.qc.service.QcItemService;
import com.ils.modules.mes.base.qc.vo.QcItemVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="质检项")
@RestController
@RequestMapping("/base/qc/qcItem")
public class QcItemController extends ILSController<QcItem, QcItemService> {
	@Autowired
	private QcItemService qcItemService;
	
    public static String QCITEM_STATUS = "qcItemstatus";

	/**
	 * 分页列表查询
	 *
	 * @param qcItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="质检项-分页列表查询", notes="质检项-分页列表查询")
	@GetMapping(value = "/list")
    public Result<?> queryPageList(QcItemVO qcItemVO,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<QcItemVO> queryWrapper = QueryGenerator.initQueryWrapper(qcItemVO, req.getParameterMap());
        String qcItemstatus = req.getParameter(QCITEM_STATUS);
        if (StringUtils.isNoneBlank(qcItemstatus)) {
            queryWrapper.eq("a.status", qcItemstatus);
        }
        Page<QcItemVO> page = new Page<QcItemVO>(pageNo, pageSize);
        IPage<QcItemVO> pageList = qcItemService.queryPageList(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param qcItem
	 * @return
	 */
	@AutoLog("质检项-添加")
	@ApiOperation(value="质检项-添加", notes="质检项-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody QcItem qcItem) {
		qcItemService.saveQcItem(qcItem);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param qcItem
	 * @return
	 */
	@AutoLog("质检项-编辑")
	@ApiOperation(value="质检项-编辑", notes="质检项-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody QcItem qcItem) {
		qcItemService.updateQcItem(qcItem);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "质检项-通过id删除")
	@ApiOperation(value="质检项-通过id删除", notes="质检项-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		qcItemService.delQcItem(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "质检项-批量删除")
	@ApiOperation(value="质检项-批量删除", notes="质检项-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.qcItemService.delBatchQcItem(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="质检项-通过id查询", notes="质检项-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		QcItem qcItem = qcItemService.getById(id);
		return Result.ok(qcItem);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param qcItem
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, QcItem qcItem) {
      return super.exportXls(request, qcItem, QcItem.class, "质检项");
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
      return super.importExcel(request, response, QcItem.class);
  }
}
