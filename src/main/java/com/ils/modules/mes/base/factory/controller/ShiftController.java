package com.ils.modules.mes.base.factory.controller;

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
import com.ils.modules.mes.base.factory.entity.Shift;
import com.ils.modules.mes.base.factory.service.ShiftService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 班次
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="班次")
@RestController
@RequestMapping("/base/factory/shift")
public class ShiftController extends ILSController<Shift, ShiftService> {
	@Autowired
	private ShiftService shiftService;
	
	/**
	 * 分页列表查询
	 *
	 * @param shift
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="班次-分页列表查询", notes="班次-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Shift shift,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Shift> queryWrapper = QueryGenerator.initQueryWrapper(shift, req.getParameterMap());

		Page<Shift> page = new Page<Shift>(pageNo, pageSize);
		IPage<Shift> pageList = shiftService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
     * 分页列表查询
     *
     * @param shift
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="班次-列表查询", notes="班次-列表查询")
    @GetMapping(value = "/worklist")
    public Result<?> worklist(Shift shift,    
                                   HttpServletRequest req) {
        QueryWrapper<Shift> queryWrapper = QueryGenerator.initQueryWrapper(shift, req.getParameterMap());

        queryWrapper.orderByAsc("shift_start_time");
        List<Shift> lstShift = shiftService.list(queryWrapper);
        return Result.ok(lstShift);
    }

    /**
     * 添加
     *
     * @param shift
     * @return
     */
	@ApiOperation(value="班次-添加", notes="班次-添加")
	@PostMapping(value = "/add")
	@AutoLog("班次-添加")
	public Result<?> add(@RequestBody Shift shift) {
        shift.setTenantId(CommonUtil.getTenantId());
        shiftService.saveShift(shift);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param shift
	 * @return
	 */
	@ApiOperation(value="班次-编辑", notes="班次-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("班次-编辑")
	public Result<?> edit(@RequestBody Shift shift) {
		shiftService.updateShift(shift);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班次-通过id删除")
	@ApiOperation(value="班次-通过id删除", notes="班次-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		shiftService.delShift(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "班次-批量删除")
	@ApiOperation(value="班次-批量删除", notes="班次-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.shiftService.delBatchShift(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="班次-通过id查询", notes="班次-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Shift shift = shiftService.getById(id);
		return Result.ok(shift);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param shift
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Shift shift) {
      return super.exportXls(request, shift, Shift.class, "班次");
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
      return super.importExcel(request, response, Shift.class);
  }
}
