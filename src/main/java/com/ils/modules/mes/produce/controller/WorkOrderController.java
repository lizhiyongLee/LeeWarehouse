package com.ils.modules.mes.produce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.produce.entity.*;
import com.ils.modules.mes.produce.service.*;
import com.ils.modules.mes.produce.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 工单
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@RestController
@RequestMapping("/produce/workOrder")
@Api(tags = "工单")
@Slf4j
public class WorkOrderController extends ILSController<WorkOrder, WorkOrderService> {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private WorkOrderLineService workOrderLineService;
    @Autowired
    private WorkOrderBomService workOrderBomService;
    @Autowired
    private WorkOrderItemBomService workOrderItemBomService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;
    @Autowired
    private WorkOrderLineParaService workOrderLineParaService;

    /**
     * 分页列表查询
     *
     * @param workOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "工单-分页列表查询", notes = "工单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WorkOrder workOrder,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WorkOrder> queryWrapper = QueryGenerator.initQueryWrapper(workOrder, req.getParameterMap());
        Page<WorkOrder> page = new Page<WorkOrder>(pageNo, pageSize);
        IPage<WorkOrder> pageList = workOrderService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param workOrderPage
     * @return
     */
    @AutoLog("工单-添加")
    @ApiOperation(value = "工单-添加", notes = "工单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WorkOrderVO workOrderVO) {
        workOrderService.saveMain(workOrderVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 生成子工单预览列表
     *
     * @param workOrderVO 父工单
     * @return
     */
    @ApiOperation(value = "工单-子工单生成", notes = "工单-子工单生成")
    @PostMapping(value = "/generateSubWorkOrderList")
    public Result<?> generateSubWorkOrderList(@RequestBody WorkOrderVO workOrderVO) {
        workOrderService.generateSubWorkOrderTree(workOrderVO);
        return Result.ok(workOrderVO);
    }

    /**
     * 工单及子工单保存
     *
     * @param workOrderVO 工单及子工单
     * @return
     */
    @AutoLog("工单-工单及子工单保存")
    @ApiOperation(value = "工单-工单及子工单保存", notes = "工单-工单及子工单保存")
    @PostMapping(value = "/addBySubWorkOrder")
    public Result<?> addBySubWorkOrder(@RequestBody WorkOrderVO workOrderVO) {
        Set<WorkOrder> workOrderSet = new HashSet<>(8);
        workOrderService.addBySubWorkOrder(workOrderVO, null, workOrderSet);
        workOrderSet.forEach(workOrder -> workOrderService.sendMsg(workOrder));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 工单列表保存
     *
     * @param workOrderVOList 工单列表
     * @return
     */
    @AutoLog("工单-批量添加")
    @ApiOperation(value = "工单-工单列表保存", notes = "工单-工单列表保存")
    @PostMapping(value = "/addByWorkOrderList")
    public Result<?> addByWorkOrderList(@RequestBody List<WorkOrderVO> workOrderVOList) {
        workOrderService.addByWorkOrderList(workOrderVOList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 根据销售订单行批量新增工单
     *
     * @param workOrderPage
     * @return
     */
    @AutoLog("工单-根据销售订单行批量添加")
    @ApiOperation(value = "工单-批量添加", notes = "工单-批量添加")
    @PostMapping(value = "/batchSaleAdd")
    public Result<?> batchSaleAdd(@RequestBody List<BatchWorkOrderVO> lstBatchWorkOrderVO) {
        workOrderService.batchSaleAdd(lstBatchWorkOrderVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param workOrderPage
     * @return
     */
    @AutoLog("工单-编辑")
    @ApiOperation(value = "工单-编辑", notes = "工单-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WorkOrderVO workOrderVO) {
        workOrderService.updateMain(workOrderVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单-通过id查询", notes = "工单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkOrderVO workOrderVO = workOrderService.querySubWorkOrder(id, new ArrayList<>(), new ArrayList<>());
        List<DefineFieldValueVO> lstDefineFields =
                defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WORK_ORDER_TABLE_CODE, id);
        workOrderVO.setLstDefineFields(lstDefineFields);
        return Result.ok(workOrderVO);
    }

    /**
     * 通过id查询工单及其子工单列表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单-通过id查询工单及其子工单列表", notes = "工单-通过id查询工单及其子工单列表")
    @GetMapping(value = "/queryWorkOrderListDetail")
    public Result<?> queryWorkOrderListDetail(@RequestParam(name = "id", required = true) String id) {
        WorkOrderDetailVO workOrderDetailVO = workOrderService.queryWorkOrderListDetail(id);
        List<DefineFieldValueVO> lstDefineFields =
                defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WORK_ORDER_TABLE_CODE, id);
        workOrderDetailVO.getWorkOrderVO().setLstDefineFields(lstDefineFields);
        return Result.ok(workOrderDetailVO);
    }

    /**
     * 工单-通过工单id查询工单工序明细
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单-通过工单id查询工单工序明细", notes = "工单-通过工单id查询工单工序明细")
    @GetMapping(value = "/queryWorkOrderLineByMainId")
    public Result<?> queryWorkOrderLineListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<WorkOrderLine> workOrderLineList = workOrderLineService.selectByMainId(id);
        return Result.ok(workOrderLineList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单-通过工单id查询工单产品BOM", notes = "工单-通过工单id查询工单产品BOM")
    @GetMapping(value = "/queryWorkOrderBomByMainId")
    public Result<?> queryWorkOrderBomListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<WorkOrderBom> workOrderBomList = workOrderBomService.selectByMainId(id);
        return Result.ok(workOrderBomList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单-通过工单id查询工单物料清单明细", notes = "工单-通过工单id查询工单物料清单明细")
    @GetMapping(value = "/queryWorkOrderItemBomByMainId")
    public Result<?> queryWorkOrderItemBomListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<WorkOrderItemBom> workOrderItemBomList = workOrderItemBomService.selectByMainId(id);
        return Result.ok(workOrderItemBomList);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单-通过工单id查询工单物料清单明细", notes = "工单-通过工单id查询工单物料清单明细")
    @GetMapping(value = "/cancelWorkOrder")
    public Result<?> cancelWorkOrder(@RequestParam(name = "id", required = true) String id) {
        workOrderService.cancelWorkOrder(id);
        return Result.ok();
    }

    @ApiOperation(value = "工单bom-分页查询工单bom信息和销售订单id", notes = "工单bom-分页查询工单bom信息和销售订单id")
    @GetMapping(value = "/listPageWorkOrderBom")
    public Result<?> listPageWorkOrderBom(WorkOrderBomVO workOrderBomVO,
                                          @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                          @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                          HttpServletRequest req) {

        QueryWrapper<WorkOrderBomVO> queryWrapper = QueryGenerator.initQueryWrapper(workOrderBomVO, req.getParameterMap());
        Page<WorkOrderBomVO> page = new Page<WorkOrderBomVO>(pageNo, pageSize);
        if (workOrderBomVO.getKeyWord() == null || workOrderBomVO.getKeyWord().isEmpty()) {
            return Result.ok(page);
        }
        IPage<WorkOrderBomVO> pageList = workOrderBomService.listPageWorkOrderBom(page, queryWrapper);
        return Result.ok(pageList);
    }

    @ApiOperation(value = "完工-关闭工单", notes = "完工-关闭工单")
    @GetMapping(value = "/finish")
    public Result<?> finish(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        workOrderService.finish(idList);
        return Result.ok();
    }

    @ApiOperation(value = "查询在制品工序进度", notes = "查询在制品工序进度")
    @GetMapping(value = "/getProgressProductsProcess")
    public Result<?> getProgressProductsProcess() {
        List<WorkOrderDetailVO> progressProductsProcess = workOrderService.getProgressProductsProcess();
        ProgressProductsProcessVO progressProductsProcessVO = new ProgressProductsProcessVO();
        progressProductsProcessVO.setWorkOrderDetailVOList(progressProductsProcess);
        return Result.ok(progressProductsProcessVO);
    }

    @ApiOperation(value = "获取工单对应工序或者产品bom对应的质检任务", notes = "获取工单对应工序或者产品bom对应的质检任务")
    @GetMapping(value = "/listQcMethodByProcessId")
    public Result<?> listQcMethodByWorkflowType(@RequestParam String processId,
                                                @RequestParam(required = false) String orderId,
                                                @RequestParam(required = false) String productId,
                                                @RequestParam(required = false) String routeId) {
        ResultDataVO resultDataVO;
        if (StringUtils.isBlank(orderId)) {
            resultDataVO = workOrderService.listQcMethodByWorkflowType(processId, productId, routeId);
        } else {
            resultDataVO = workOrderService.listQcMethodByOrderId(processId, orderId);
        }
        return Result.ok(resultDataVO);
    }

    @ApiOperation(value = "获取工单对应工艺路线或者产品bom对应的参数模板", notes = "获取工单对应工艺路线或者产品bom对应的参数模板")
    @GetMapping(value = "/listParaByLineId")
    public Result<?> listParaByLineId(@RequestParam(required = false) String processId,
                                      @RequestParam(required = false) String lineId,
                                      @RequestParam(required = false) String productId,
                                      @RequestParam(required = false) String routeId) {
        List<WorkOrderLinePara> paraList;
        if (StringUtils.isBlank(lineId)) {
            paraList = workOrderService.listParaByWorkflowType(processId, productId, routeId, null, null);
        } else {
            QueryWrapper<WorkOrderLinePara> workOrderLineParaQueryWrapper = new QueryWrapper<>();
            workOrderLineParaQueryWrapper.eq("work_order_line_id", lineId);
            paraList = workOrderLineParaService.list(workOrderLineParaQueryWrapper);
        }
        ResultDataVO resultDataVO = new ResultDataVO();
        resultDataVO.setData(paraList);
        return Result.ok(resultDataVO);
    }

    @ApiOperation(value = "获取工单对应物料需求报表", notes = "获取工单对应物料需求报表")
    @GetMapping(value = "/getRequiredMaterial")
    public Result<?> getRequiredMaterial(RequiredMaterialVO requiredMaterialVO,
                                         @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                         @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        Page<RequiredMaterialVO> page = new Page<>(pageNo, pageSize);
        Page<RequiredMaterialVO> requiredMaterialVOList = workOrderService.getRequiredMaterial(page,requiredMaterialVO);
        return Result.ok(requiredMaterialVOList);
    }

    /**
     * 导出物料需求报表excel
     */
    @GetMapping(value = "/exportRequiredMaterialXls")
    public ModelAndView exportRequiredMaterialXls(RequiredMaterialVO requiredMaterialVO,
            @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        Page<RequiredMaterialVO> page = new Page<>(pageNo, pageSize);
        String title = "物料需求报表";
        List<RequiredMaterialVO> requiredMaterialVOList = workOrderService.getRequiredMaterial(page,requiredMaterialVO).getRecords();
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject("fileName", title);
        mv.addObject("entity", RequiredMaterialVO.class);
        mv.addObject("params", new ExportParams(title, "导出人:" + CommonUtil.getLoginUser().getRealname(), title));
        mv.addObject("data", requiredMaterialVOList);
        return mv;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param workOrder
     * @return
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkOrder workOrder) {
        return super.exportXls(request, workOrder, WorkOrder.class, "计划工单");

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
        return super.importExcel(request, response, WorkOrder.class);
    }
}
