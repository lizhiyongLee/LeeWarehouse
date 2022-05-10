package com.ils.modules.mes.produce.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.service.SaleOrderLineService;
import com.ils.modules.mes.produce.vo.SaleOrderLineVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 销售订单行
 * @author: fengyi
 * @date: 2021年1月25日 下午1:16:39
 */
@RestController
@RequestMapping("/produce/saleOrderLine")
@Api(tags = "销售订单行")
@Slf4j
public class SaleOrderLineController extends ILSController<SaleOrderLine, SaleOrderLineService> {

    @Autowired
    private SaleOrderLineService saleOrderLineService;

    /** 定义状态查询参数变量 */
    public final static String ORDER_QUERY_STATUS = "orderQueryStatus";
    /** 定义订单号 */
    public final static String ORDER_NO = "orderNo";

    public final static String EXTERNALSALEORDER_NO = "externalSaleOrderNo";

    /**
     * 分页列表查询
     *
     * @param saleOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "销售订单物料行-分页列表查询", notes = "销售订单物料行-分页列表查询")
    @GetMapping(value = "/orderLinePageList")
    public Result<?> orderLinePageList(SaleOrderLineVO saleOrderLineVO,
        @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
        @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SaleOrderLineVO> queryWrapper =
            QueryGenerator.initQueryWrapper(saleOrderLineVO, req.getParameterMap());
        String orderNo = req.getParameter(ORDER_NO);
        if (StringUtils.isNotBlank(orderNo)) {
            queryWrapper.like("mes_sale_order.sale_order_no", orderNo);
        }

        String orderQueryStatus = req.getParameter(ORDER_QUERY_STATUS);
        if (StringUtils.isNotBlank(orderQueryStatus)) {
            queryWrapper.eq("mes_sale_order.status", orderQueryStatus);
        }

        String externalSaleOrderNo = req.getParameter(EXTERNALSALEORDER_NO);
        if (StringUtils.isNotBlank(externalSaleOrderNo)) {
            queryWrapper.eq("mes_sale_order.external_sale_order_no", externalSaleOrderNo);
        }

        Page<SaleOrderLineVO> page = new Page<SaleOrderLineVO>(pageNo, pageSize);
        IPage<SaleOrderLineVO> pageList = saleOrderLineService.saleOrderLinePage(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param saleOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "销售订单行-分页列表查询", notes = "销售订单行-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SaleOrderLine saleOrderLine,
        @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
        @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SaleOrderLine> queryWrapper =
            QueryGenerator.initQueryWrapper(saleOrderLine, req.getParameterMap());
        Page<SaleOrderLine> page = new Page<SaleOrderLine>(pageNo, pageSize);
        IPage<SaleOrderLine> pageList = saleOrderLineService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

}
