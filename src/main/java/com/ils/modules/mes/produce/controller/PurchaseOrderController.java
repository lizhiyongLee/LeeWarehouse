package com.ils.modules.mes.produce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.produce.entity.PurchaseOrder;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import com.ils.modules.mes.produce.mapper.PurchaseOrderLineMapper;
import com.ils.modules.mes.produce.service.PurchaseOrderLineService;
import com.ils.modules.mes.produce.service.PurchaseOrderService;
import com.ils.modules.mes.produce.vo.PurchaseOrderDetailVO;
import com.ils.modules.mes.produce.vo.PurchaseOrderLineVO;
import com.ils.modules.mes.produce.vo.PurchaseOrderVO;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 采购清单
 * @Author: Tian
 * @Date: 2021-01-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "采购清单")
@RestController
@RequestMapping("/produce/purchaseOrder")
public class PurchaseOrderController extends ILSController<PurchaseOrder, PurchaseOrderService> {
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private PurchaseOrderLineMapper purchaseOrderLineMapper;
    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    /**
     * 分页列表查询
     *
     * @param purchaseOrderVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "采购清单-分页列表查询", notes = "采购清单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PurchaseOrderVO purchaseOrderVO,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<PurchaseOrderVO> queryWrapper = QueryGenerator.initQueryWrapper(purchaseOrderVO, req.getParameterMap());
        Page<PurchaseOrderVO> page = new Page<PurchaseOrderVO>(pageNo, pageSize);
        queryWrapper.eq("1", "1");
        IPage<PurchaseOrderVO> pageList = purchaseOrderService.listPage(page, queryWrapper, purchaseOrderVO.getItemCode(), purchaseOrderVO.getItemName());
        List<PurchaseOrderVO> records = (List<PurchaseOrderVO>) pageList.getRecords();
        for (PurchaseOrderVO oldPurchaseOrderVO : records) {
            QueryWrapper<PurchaseOrderLine> purchaseOrderLineQueryWrapper = new QueryWrapper<>();
            purchaseOrderLineQueryWrapper.eq("purchase_order_id", oldPurchaseOrderVO.getId());
            purchaseOrderLineQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineMapper.selectList(purchaseOrderLineQueryWrapper);
            int amountFinished = 0;
            int amountPlanned = 0;
            if (!CommonUtil.isEmptyOrNull(purchaseOrderLineList)) {
                for (PurchaseOrderLine purchaseOrderLine : purchaseOrderLineList) {
                    if (purchaseOrderLine.getCompleteQty() != null && !purchaseOrderLine.getCompleteQty().isEmpty()) {
                        if (purchaseOrderLine.getPlanQty() != null && !purchaseOrderLine.getPlanQty().isEmpty()) {
                            if (Long.parseLong(purchaseOrderLine.getCompleteQty()) >= Long.parseLong(purchaseOrderLine.getPlanQty())) {
                                amountFinished++;
                            }
                        }
                    }
                    amountPlanned++;
                }
            }
            oldPurchaseOrderVO.setPlanProcess(amountFinished + "/" + amountPlanned);
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param purchaseOrderLineVO
     * @return
     */
    @AutoLog("采购清单-添加")
    @ApiOperation(value = "采购清单-添加", notes = "采购清单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PurchaseOrderLineVO purchaseOrderLineVO) {
        purchaseOrderService.savePurchaseOrder(purchaseOrderLineVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param purchaseOrderLineVO
     * @return
     */
    @AutoLog("采购清单-编辑")
    @ApiOperation(value = "采购清单-编辑", notes = "采购清单-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody PurchaseOrderLineVO purchaseOrderLineVO) {
        purchaseOrderService.updatePurchaseOrder(purchaseOrderLineVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }
    /**
     * 批量改状态
     * @param ids
     * @param status
     * @return
     */
    @AutoLog(value = "收货单头-批量改状态")
    @ApiOperation(value="收货单头-批量改状态", notes="收货单头-批量改状态")
    @GetMapping(value = "/updateStatusBatch")
    public Result<?> updateStatusBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="status",required=true) String status) {
        this.purchaseOrderService.updateStatus(status, Arrays.asList(ids.split(",")));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }
    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购清单-通过id删除")
    @ApiOperation(value = "采购清单-通过id删除", notes = "采购清单-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        purchaseOrderService.delPurchaseOrder(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "采购清单-批量删除")
    @ApiOperation(value = "采购清单-批量删除", notes = "采购清单-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.purchaseOrderService.delBatchPurchaseOrder(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "采购清单-通过id查询", notes = "采购清单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getById(id);
        PurchaseOrderLineVO purchaseOrderLineVO = new PurchaseOrderLineVO();
        BeanUtils.copyProperties(purchaseOrder, purchaseOrderLineVO);
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineService.queryByMainId(id);
        purchaseOrderLineVO.setPurchaseOrderLineList(purchaseOrderLineList);
        return Result.ok(purchaseOrderLineVO);
    }

    /**
     * 通过id查询采购订单详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "采购清单-通过id查询采购订单详情", notes = "采购清单-通过id查询采购订单详情")
    @GetMapping(value = "/queryDetailById")
    public Result<?> queryDetailById(@RequestParam(name = "id", required = true) String id) {
        PurchaseOrderDetailVO purchaseOrderDetailVO = purchaseOrderService.queryDetailById(id);
        return Result.ok(purchaseOrderDetailVO);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param purchaseOrder
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurchaseOrder purchaseOrder) {
        return super.exportXls(request, purchaseOrder, PurchaseOrder.class, "采购清单");
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
        return super.importExcel(request, response, PurchaseOrder.class);
    }
}
