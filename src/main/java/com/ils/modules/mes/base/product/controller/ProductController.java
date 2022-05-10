package com.ils.modules.mes.base.product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
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
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.product.entity.Product;
import com.ils.modules.mes.base.product.service.ProductService;
import com.ils.modules.mes.base.product.vo.ProductLineBomVO;
import com.ils.modules.mes.base.product.vo.ProductListVO;
import com.ils.modules.mes.base.product.vo.ProductVO;
import com.ils.modules.mes.base.product.vo.ResultDataVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
 
/**
 * @Description: 产品
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/product/product")
@Api(tags="产品")
@Slf4j
public class ProductController extends ILSController<Product, ProductService> {
	@Autowired
	private ProductService productService;
	/**
	 * 分页列表查询
	 *
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="产品-分页列表查询", notes="产品-分页列表查询")
	@GetMapping(value = "/list")
    public Result<?> queryPageList(ProductListVO productListVO,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<ProductListVO> queryWrapper =
            QueryGenerator.initQueryWrapper(productListVO, req.getParameterMap());
        Page<ProductListVO> page = new Page<ProductListVO>(pageNo, pageSize);
        IPage<ProductListVO> pageList = productService.listPage(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param productPage
	 * @return
	 */
    @AutoLog("产品-添加")
    @ApiOperation(value="产品-添加", notes="产品-添加")
	@PostMapping(value = "/add")
    public Result<?> add(@RequestBody ProductVO productVO) {
       productService.saveMain(productVO);
       return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param productPage
	 * @return
	 */
    @AutoLog("产品-编辑")
    @ApiOperation(value="产品-编辑", notes="产品-编辑")
	@PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ProductVO productVO) {

        productService.updateMain(productVO);

		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	
	
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "产品-通过id查询", notes = "产品-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ProductVO productVO = productService.queryById(id);
        return Result.ok(productVO);
    }

    /**
     * 通过id查询BOM头信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "产品-通过id头信息", notes = "产品-通过id头信息")
    @GetMapping(value = "/queryHeadById")
    public Result<?> queryHeadById(@RequestParam(name = "id", required = true) String id) {
        Product product = productService.getById(id);
        return Result.ok(product);
    }
	
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "产品工艺物料 -通过id查询", notes = "产品工艺物料-通过id查询")
    @GetMapping(value = "/queryProductLineBomById")
    public Result<?> queryProductLineBomById(@RequestParam(name = "id", required = true) String id) {
        ResultDataVO resultDataVO = new ResultDataVO();
        List<ProductLineBomVO> lstProductLineBomVO = productService.queryProductLineBomById(id);
        resultDataVO.setData(lstProductLineBomVO);
        return Result.ok(resultDataVO);
    }



}
