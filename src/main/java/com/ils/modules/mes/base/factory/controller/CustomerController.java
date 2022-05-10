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
import com.ils.modules.mes.base.factory.entity.Customer;
import com.ils.modules.mes.base.factory.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 客户
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="客户")
@RestController
@RequestMapping("/base/factory/customer")
public class CustomerController extends ILSController<Customer, CustomerService> {
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param customer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="客户-分页列表查询", notes="客户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Customer customer,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());

		Page<Customer> page = new Page<Customer>(pageNo, pageSize);
		IPage<Customer> pageList = customerService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
    /**
     * 获取列表查询
     *
     * @param customer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "客户-获取列表查询", notes = "客户-获取列表查询")
    @GetMapping(value = "/selectList")
    public Result<?> selectList(Customer customer, HttpServletRequest req) {
        QueryWrapper<Customer> queryWrapper = QueryGenerator.initQueryWrapper(customer, req.getParameterMap());
        List<Customer> lstCustomer = customerService.list(queryWrapper);
        return Result.ok(lstCustomer);
    }

	/**
	 * 添加
	 *
	 * @param customer
	 * @return
	 */
	@ApiOperation(value="客户-添加", notes="客户-添加")
	@PostMapping(value = "/add")
	@AutoLog("客户-添加")
	public Result<?> add(@RequestBody Customer customer) {
        customer.setTenantId(CommonUtil.getTenantId());
		customerService.saveCustomer(customer);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param customer
	 * @return
	 */
	@ApiOperation(value="客户-编辑", notes="客户-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("客户-编辑")
	public Result<?> edit(@RequestBody Customer customer) {
		customerService.updateCustomer(customer);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户-通过id删除")
	@ApiOperation(value="客户-通过id删除", notes="客户-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		customerService.delCustomer(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户-批量删除")
	@ApiOperation(value="客户-批量删除", notes="客户-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.customerService.delBatchCustomer(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@ApiOperation(value="客户-通过id查询", notes="客户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Customer customer = customerService.getById(id);
		return Result.ok(customer);
	}

  /**
   * 导出excel
   * @param request
   * @param customer
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Customer customer) {
      return super.exportXls(request, customer, Customer.class, "客户");
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
      return super.importExcel(request, response, Customer.class);
  }
}
