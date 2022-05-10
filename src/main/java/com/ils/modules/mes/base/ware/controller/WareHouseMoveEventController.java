package com.ils.modules.mes.base.ware.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.ware.entity.WareHouseMoveEvent;
import com.ils.modules.mes.base.ware.service.WareHouseMoveEventService;
import com.ils.modules.mes.enums.EventObjectEnum;
import com.ils.modules.mes.produce.entity.SaleOrder;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.service.SaleOrderService;
import com.ils.modules.mes.produce.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 仓库移动事务
 * @Author: Tian
 * @Date: 2021-02-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "仓库移动事务")
@RestController
@RequestMapping("/base/ware/wareHouseMoveEvent")
public class WareHouseMoveEventController extends ILSController<WareHouseMoveEvent, WareHouseMoveEventService> {
    @Autowired
    private WareHouseMoveEventService wareHouseMoveEventService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private SaleOrderService saleOrderService;

    /**
     * 分页列表查询
     *
     * @param wareHouseMoveEvent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "仓库移动事务-分页列表查询", notes = "仓库移动事务-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WareHouseMoveEvent wareHouseMoveEvent,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WareHouseMoveEvent> queryWrapper = QueryGenerator.initQueryWrapper(wareHouseMoveEvent, req.getParameterMap());
        Page<WareHouseMoveEvent> page = new Page<WareHouseMoveEvent>(pageNo, pageSize);
        IPage<WareHouseMoveEvent> pageList = wareHouseMoveEventService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param wareHouseMoveEvent
     * @return
     */
    @AutoLog("仓库移动事务-添加")
    @ApiOperation(value = "仓库移动事务-添加", notes = "仓库移动事务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WareHouseMoveEvent wareHouseMoveEvent) {
        wareHouseMoveEventService.saveWareHouseMoveEvent(wareHouseMoveEvent);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param wareHouseMoveEvent
     * @return
     */
    @AutoLog("仓库移动事务-编辑")
    @ApiOperation(value = "仓库移动事务-编辑", notes = "仓库移动事务-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WareHouseMoveEvent wareHouseMoveEvent) {
        wareHouseMoveEventService.updateWareHouseMoveEvent(wareHouseMoveEvent);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "仓库移动事务-通过id查询", notes = "仓库移动事务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WareHouseMoveEvent wareHouseMoveEvent = wareHouseMoveEventService.getById(id);
        return Result.ok(wareHouseMoveEvent);
    }

    /**
     * 通过id查询工单列表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "仓库移动事务-通过id查询工单列表", notes = "仓库移动事务-通过id查询工单列表")
    @GetMapping(value = "/queryOrderById")
    public Result<?> queryOrderById(@RequestParam(name = "id", required = true) String id) {
        WareHouseMoveEvent wareHouseMoveEvent = wareHouseMoveEventService.getById(id);
        List<?> orderList = new ArrayList<>(16);
        if (EventObjectEnum.ORDER.getValue().equals(wareHouseMoveEvent.getEventObject())) {
            LambdaQueryWrapper<WorkOrder> workOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            workOrderLambdaQueryWrapper.in(WorkOrder::getStatus, '1', '2', '3', '4');
            orderList = workOrderService.list(workOrderLambdaQueryWrapper);
        } else if (EventObjectEnum.SALE_ORDER.getValue().equals(wareHouseMoveEvent.getEventObject())) {
            LambdaQueryWrapper<SaleOrder> saleOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            saleOrderLambdaQueryWrapper.in(SaleOrder::getStatus, '1', '2');
            orderList = saleOrderService.list(saleOrderLambdaQueryWrapper);
        }
        return Result.ok(orderList);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param wareHouseMoveEvent
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WareHouseMoveEvent wareHouseMoveEvent) {
        return super.exportXls(request, wareHouseMoveEvent, WareHouseMoveEvent.class, "仓库移动事务");
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
        return super.importExcel(request, response, WareHouseMoveEvent.class);
    }
}
