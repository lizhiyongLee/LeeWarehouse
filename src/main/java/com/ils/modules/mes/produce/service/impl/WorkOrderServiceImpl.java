package com.ils.modules.mes.produce.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.service.RouteLineMethodService;
import com.ils.modules.mes.base.craft.service.RouteLineParaHeadService;
import com.ils.modules.mes.base.craft.vo.RouteLineMethodVO;
import com.ils.modules.mes.base.craft.vo.RouteLineParaVO;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.mapper.UnitMapper;
import com.ils.modules.mes.base.factory.mapper.WorkstationMapper;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.product.entity.*;
import com.ils.modules.mes.base.product.mapper.ProductBomMapper;
import com.ils.modules.mes.base.product.mapper.ProductRouteMethodMapper;
import com.ils.modules.mes.base.product.service.*;
import com.ils.modules.mes.base.product.vo.*;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.mapper.SopTemplateMapper;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.produce.entity.*;
import com.ils.modules.mes.produce.mapper.*;
import com.ils.modules.mes.produce.vo.*;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.system.entity.BizConfig;
import com.ils.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.craft.service.RouteLineService;
import com.ils.modules.mes.base.craft.vo.RouteLineVO;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.CodeGeneratorService;

import static com.ils.modules.mes.constants.MesCommonConstant.MAX_ORDER_LAYER;
import static com.ils.modules.mes.constants.MesCommonConstant.MIN_ORDER_LAYER;

/**
 * @Description: 工单
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderLineMapper workOrderLineMapper;
    @Autowired
    private WorkOrderBomMapper workOrderBomMapper;
    @Autowired
    private WorkOrderItemBomMapper workOrderItemBomMapper;
    @Autowired
    private ProductBomMapper productBomMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ItemBomService itemBomService;
    @Autowired
    private ProductLineService productLineService;
    @Autowired
    private WorkProcessTaskMapper workProcessTaskMapper;
    @Autowired
    private WorkOrderRelateSaleMapper workOrderRelateSaleMapper;
    @Autowired
    private SaleOrderLineMapper saleOrderLineMapper;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private RouteLineService routeLineService;
    @Autowired
    private ItemBomDetailService itemBomDetailService;
    @Autowired
    private WorkPlanTaskMapper workPlanTaskMapper;
    @Autowired
    private SopTemplateMapper sopTemplateMapper;
    @Autowired
    private DefineFieldValueService defineFieldValueService;
    @Autowired
    private RouteLineMethodService routeLineMethodService;
    @Autowired
    private ProductRouteMethodMapper productRouteMethodMapper;
    @Autowired
    private ProductRouteParaHeadService productRouteParaHeadService;
    @Autowired
    private RouteLineParaHeadService routeLineParaHeadService;
    @Autowired
    private WorkProcessTaskServiceImpl workProcessTaskServiceImpl;
    @Autowired
    private WorkstationMapper workstationMapper;
    @Autowired
    private WorkOrderLineStationMapper workOrderLineStationMapper;
    @Autowired
    private WorkOrderLineMethodMapper workOrderLineMethodMapper;
    @Autowired
    private WorkOrderLineParaMapper workOrderLineParaMapper;
    @Autowired
    private UnitService unitService;
    @Autowired
    private MsgHandleServer msgHandleServer;

    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WorkOrder saveMain(WorkOrderVO workOrderVO) {
        WorkOrder workOrder = new WorkOrder();
        BeanUtils.copyProperties(workOrderVO, workOrder);
        if (WorkOrderBatchTypeEnum.RULE.getValue().equals(workOrder.getBatchType())) {
            // 生成单号
            String batchNo = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.WORK_ORDER_BATCH_NO, workOrder);
            workOrder.setBatchNo(batchNo);
        }
        BizConfig noBizConfig = CommonUtil.getBizConfig(MesCommonConstant.WORK_ORDER_AUTO_NO_SWITCH);
        if (ZeroOrOneEnum.ONE.getStrCode().equals(noBizConfig.getConfigValue())) {
            // 生成单号
            String orderNo = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.MES_WORK_ORDER_NO, workOrder);
            workOrder.setOrderNo(orderNo);
        }
        List<WorkOrderLine> workOrderLineList = workOrderVO.getWorkOrderLineList();
        List<WorkOrderBom> workOrderBomList = workOrderVO.getWorkOrderBomList();
        List<WorkOrderItemBom> workOrderItemBomList = workOrderVO.getWorkOrderItemBomList();

        // 数据合规性检查
        this.checkCondition(workOrder);
        //检查SOP
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.SOP_TEMPLATE_SWITCH);
        boolean switchIdent = ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue());
        if (switchIdent) {
            this.checkSop(workOrderVO);
        }
        // 审批状态为未审批
        workOrder.setAuditStatus(AuditStatusEnum.AUDIT_NEW.getValue());
        // 插入主表
        workOrderMapper.insert(workOrder);
        // 保存明细
        this.saveWorkOrderDetail(workOrder, workOrderLineList, workOrderBomList, workOrderItemBomList);
        // 生成工序任务
        this.saveWorkProcessTask(workOrder, workOrderLineList);

        // 保存订单明细
        List<WorkOrderRelateSale> lstSaleOrderLine = workOrderVO.getSaleOrderLineList();
        this.saveSaleLine(workOrder, lstSaleOrderLine);

        //保存自定义列
        workOrderVO.setId(workOrder.getId());
        defineFieldValueService.saveDefineFieldValue(workOrderVO.getLstDefineFields(),
                TableCodeConstants.WORK_ORDER_TABLE_CODE, workOrder.getId());

        return workOrder;

    }


    @Override
    public void generateSubWorkOrderTree(WorkOrderVO workOrderVO) {
        //如果没有层级则为父工单，赋予初始值
        if (workOrderVO.getOrderLayer() == null) {
            //父工单初始层级
            workOrderVO.setOrderLayer(MIN_ORDER_LAYER);
        }
        String workflowType = workOrderVO.getWorkflowType();
        workOrderVO.setPlanQtyTemp(workOrderVO.getPlanQty());
        //工艺+物料路线
        if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType)) {
            this.buildSubWorkOrderListByItem(workOrderVO);
        }
        //产品bom路线
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
            this.buildSubWorkOrderListByProduct(workOrderVO);
        }
    }

    /**
     * 根据产品bom生产子工单
     *
     * @param workOrderVO
     */
    private void buildSubWorkOrderListByProduct(WorkOrderVO workOrderVO) {
        if (workOrderVO.getOrderLayer() > MAX_ORDER_LAYER) {
            throw new ILSBootException("P-AU-0073");
        }
        //工单物料清单
        List<WorkOrderBom> workOrderBomList = workOrderVO.getWorkOrderBomList();
        List<WorkOrderVO> subWorkOrderVOList = new ArrayList<>();
        if (workOrderBomList != null && !workOrderBomList.isEmpty()) {
            //子工单序号生成
            int orderNoNum = 1;
            for (WorkOrderBom workOrderBom : workOrderBomList) {
                String itemId = workOrderBom.getItemId();
                //查找产品基础数据
                List<Product> productList = productService.selectByItemIdAndOrderByUpdateTime(itemId);
                if (!productList.isEmpty()) {
                    //获取最新的方案
                    Product product = productList.get(0);
                    //获取方案详情
                    List<ProductBomVO> productBomVOList = productBomMapper.selectByProductId(product.getId());
                    //判断方案是否需要物料，是则建立子工单
                    if (!productBomVOList.isEmpty()) {
                        WorkOrderVO subWorkOrderVO = new WorkOrderVO();
                        BeanUtils.copyProperties(workOrderVO, subWorkOrderVO);
                        subWorkOrderVO.setOrderNo(workOrderVO.getOrderNo() + "-" + orderNoNum);
                        subWorkOrderVO.setOrderLayer(workOrderVO.getOrderLayer() + 1);
                        subWorkOrderVO.setFatherOrderNo(workOrderVO.getOrderNo());
                        BigDecimal multiply = workOrderBom.getQty().multiply(workOrderVO.getPlanQty());
                        subWorkOrderVO.setPlanQtyTemp(multiply);
                        subWorkOrderVO.setPlanQty(multiply);
                        subWorkOrderVO.setSeq(workOrderBom.getSeq());
                        subWorkOrderVO.setProcessId(workOrderBom.getProcessId());
                        subWorkOrderVO.setProcessCode(workOrderBom.getProcessCode());
                        subWorkOrderVO.setProcessName(workOrderBom.getProcessName());
                        subWorkOrderVO.setItemCode(workOrderBom.getItemCode());
                        subWorkOrderVO.setItemId(workOrderBom.getItemId());
                        Item byId = itemService.getById(workOrderBom.getItemId());
                        subWorkOrderVO.setUnit(byId.getMainUnit());
                        subWorkOrderVO.setItemName(workOrderBom.getItemName());
                        subWorkOrderVO.setSpec(workOrderBom.getItemName());
                        subWorkOrderVO.setProductId(product.getId());
                        //子工单的物料清单
                        List<WorkOrderBom> subWorkOrderBomList = new ArrayList<>();
                        for (ProductBomVO productBomVO : productBomVOList) {
                            WorkOrderBom subWorkOrderBom = new WorkOrderBom();
                            BeanUtils.copyProperties(workOrderBom, subWorkOrderBom);
                            subWorkOrderBom.setItemId(productBomVO.getItemId());
                            subWorkOrderBom.setItemCode(productBomVO.getItemCode());
                            subWorkOrderBom.setItemName(productBomVO.getItemName());
                            subWorkOrderBom.setProductId(productBomVO.getProductId());
                            subWorkOrderBom.setProductLineId(productBomVO.getProductLineId());
                            subWorkOrderBom.setTotalQty(subWorkOrderVO.getPlanQty().multiply(workOrderBom.getQty()));
                            subWorkOrderBomList.add(subWorkOrderBom);
                        }
                        subWorkOrderVO.setPmcNameList(getUserNameById(subWorkOrderVO.getPmc()));
                        subWorkOrderVO.setDirectorNameList(getUserNameById(subWorkOrderVO.getDirector()));
                        subWorkOrderVO.setWorkOrderBomList(subWorkOrderBomList);
                        subWorkOrderVO.setOptionalProductList(productList);
                        subWorkOrderVOList.add(subWorkOrderVO);
                        //递归遍历子工单
                        buildSubWorkOrderListByProduct(subWorkOrderVO);
                        //如果被使用，子工单编号递增
                        orderNoNum++;
                    }
                }
            }
            workOrderVO.setSubWorkOrderVOList(subWorkOrderVOList);
        }
    }

    /**
     * 根据工艺路线生产子工单
     *
     * @param workOrderVO
     */
    private void buildSubWorkOrderListByItem(WorkOrderVO workOrderVO) {
        if (workOrderVO.getOrderLayer() > MAX_ORDER_LAYER) {
            throw new ILSBootException("P-AU-0073");
        }
        List<WorkOrderLine> workOrderLineList = workOrderVO.getWorkOrderLineList();
        String processCode = null;
        String processId = null;
        String processName = null;
        if (workOrderLineList != null && !workOrderLineList.isEmpty()) {
            WorkOrderLine workOrderLine = workOrderLineList.get(0);
            processCode = workOrderLine.getProcessCode();
            processId = workOrderLine.getProcessId();
            processName = workOrderLine.getProcessName();
        }
        //工单物料清单
        List<WorkOrderItemBom> workOrderItemBomList = workOrderVO.getWorkOrderItemBomList();
        List<WorkOrderVO> subWorkOrderTreeVOList = new ArrayList<>();
        if (workOrderItemBomList != null && !workOrderItemBomList.isEmpty()) {
            //子工单序号生成
            int orderNoNum = 1;
            for (WorkOrderItemBom workOrderItemBom : workOrderItemBomList) {
                String itemId = workOrderItemBom.getItemId();
                //查找物料bom数据
                List<ItemBom> itemBomIdList = itemBomService.selectByItemIdAndOrderByUpdateTime(itemId);
                if (!itemBomIdList.isEmpty()) {
                    //获取最新的方案
                    ItemBom itemBom = itemBomIdList.get(0);
                    //获取方案详情
                    List<ItemBomDetail> itemBomDetailList = itemBomDetailService.selectByBomId(itemBom.getId());
                    //判断方案是否需要物料，是则建立子工单
                    if (!itemBomDetailList.isEmpty()) {
                        WorkOrderVO subWorkOrderVO = new WorkOrderVO();
                        BeanUtils.copyProperties(workOrderVO, subWorkOrderVO);
                        subWorkOrderVO.setOrderNo(workOrderVO.getOrderNo() + "-" + orderNoNum);
                        subWorkOrderVO.setFatherOrderNo(workOrderVO.getOrderNo());
                        subWorkOrderVO.setOrderLayer(workOrderVO.getOrderLayer() + 1);
                        BigDecimal multiply = workOrderItemBom.getQty().multiply(workOrderVO.getPlanQty());
                        subWorkOrderVO.setPlanQtyTemp(multiply);
                        subWorkOrderVO.setPlanQty(multiply);
                        Item byId = itemService.getById(workOrderItemBom.getItemId());
                        subWorkOrderVO.setUnit(byId.getMainUnit());
                        subWorkOrderVO.setItemCode(workOrderItemBom.getItemCode());
                        subWorkOrderVO.setItemId(workOrderItemBom.getItemId());
                        subWorkOrderVO.setItemName(workOrderItemBom.getItemName());
                        subWorkOrderVO.setProcessId(processId);
                        subWorkOrderVO.setProcessName(processName);
                        subWorkOrderVO.setProcessCode(processCode);
                        subWorkOrderVO.setBomId(itemBom.getId());
                        subWorkOrderVO.setRouteId(null);
                        subWorkOrderVO.setWorkOrderLineList(null);
                        subWorkOrderVO.setParentWorkOrderLineList(workOrderLineList);
                        subWorkOrderVO.setOptionalItemBomList(itemBomIdList);
                        subWorkOrderVO.setSpec(itemBom.getItemName());
                        //建立子工单的物料清单
                        List<WorkOrderItemBom> subWorkOrderBomList = new ArrayList<>();
                        for (ItemBomDetail itemBomDetail : itemBomDetailList) {
                            WorkOrderItemBom subWorkOrderItemBom = new WorkOrderItemBom();
                            BeanUtils.copyProperties(workOrderItemBom, subWorkOrderItemBom);
                            subWorkOrderItemBom.setItemId(itemBomDetail.getItemId());
                            subWorkOrderItemBom.setItemCode(itemBomDetail.getItemCode());
                            subWorkOrderItemBom.setItemName(itemBomDetail.getItemName());
                            subWorkOrderItemBom.setTotalQty(subWorkOrderVO.getPlanQty().multiply(subWorkOrderItemBom.getQty()));
                            subWorkOrderBomList.add(subWorkOrderItemBom);
                        }
                        subWorkOrderVO.setWorkOrderItemBomList(subWorkOrderBomList);
                        subWorkOrderVO.setPmcNameList(getUserNameById(subWorkOrderVO.getPmc()));
                        subWorkOrderVO.setDirectorNameList(getUserNameById(subWorkOrderVO.getDirector()));
                        subWorkOrderTreeVOList.add(subWorkOrderVO);
                        //递归遍历子工单
                        buildSubWorkOrderListByItem(subWorkOrderVO);
                        //如果被使用，子工单编号递增
                        orderNoNum++;
                    }
                }
            }
            workOrderVO.setSubWorkOrderVOList(subWorkOrderTreeVOList);
        }
    }

    @Autowired
    private UserService sysUserService;

    private String getUserNameById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        String[] idList = id.split(",");
        StringBuilder nameList = new StringBuilder();
        for (String userId : idList) {
            nameList.append(sysUserService.getById(userId).getRealname()).append(",");
        }
        nameList.deleteCharAt(nameList.length() - 1);
        return nameList.toString();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addByWorkOrderList(List<WorkOrderVO> workOrderVOList) {
        Set<WorkOrder> workOrderSet = new HashSet<>(8);
        workOrderVOList.forEach(workOrderVO -> {
            this.addBySubWorkOrder(workOrderVO, null, workOrderSet);
        });
        workOrderSet.forEach(this::sendMsg);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addBySubWorkOrder(WorkOrderVO workOrderVO, String fatherId, Set<WorkOrder> workOrderSet) {
        workOrderVO.setFatherOrderId(fatherId);
        WorkOrder workOrder = saveMain(workOrderVO);
        workOrderSet.add(workOrder);
        List<WorkOrderVO> subWorkOrderVOList = workOrderVO.getSubWorkOrderVOList();
        if (!CommonUtil.isEmptyOrNull(subWorkOrderVOList)) {
            //遍历子工单，递归保存子工单
            for (WorkOrderVO subWorkOrderVO : subWorkOrderVOList) {
                subWorkOrderVO.setId(null);
                addBySubWorkOrder(subWorkOrderVO, workOrder.getId(), workOrderSet);
            }
        }

    }

    /**
     * 获取工单及其子工单详情
     *
     * @param id 工单列表
     * @return
     */
    @Override
    public WorkOrderDetailVO queryWorkOrderListDetail(String id) {
        String firstOrderId = getFirstOrderId(id);
        WorkOrderDetailVO workOrderDetailVO = new WorkOrderDetailVO();
        //当前工单查询
        WorkOrderVO workOrderVO = this.queryWorkOrderDetail(id);
        workOrderDetailVO.setWorkOrderVO(workOrderVO);
        List<WorkOrderProgressVO> workOrderProgressVOList = new ArrayList<>();
        List<WorkOrderProcessProgressVO> workOrderProcessProgressVOList = new ArrayList<>();

        WorkOrderProgressVO workOrderProgressVO = new WorkOrderProgressVO();
        BeanUtils.copyProperties(workOrderVO, workOrderProgressVO);
        workOrderProgressVOList.add(workOrderProgressVO);

        //查询子工单
        this.querySubWorkOrder(firstOrderId, workOrderProgressVOList, workOrderProcessProgressVOList);
        workOrderDetailVO.setWorkOrderProgressVOList(workOrderProgressVOList);
        workOrderDetailVO.setWorkOrderProcessProgressVOList(workOrderProcessProgressVOList);
        return workOrderDetailVO;
    }


    @Override
    public WorkOrderVO querySubWorkOrder(String id, List<WorkOrderProgressVO> workOrderProgressVOList, List<WorkOrderProcessProgressVO> workOrderProcessProgressVOList) {
        WorkOrderVO workOrderVO = this.queryWorkOrderDetail(id);
        this.getWorkOrderProcessProgressVO(id, workOrderVO.getOrderLayer(), workOrderProcessProgressVOList);
        //获取子工单id列表
        List<WorkOrder> subWorkOrderList = workOrderMapper.listByFatherOrderId(id);
        List<WorkOrderVO> subWorkOrderVOList = new ArrayList<>();

        if (subWorkOrderList != null && !subWorkOrderList.isEmpty()) {
            //遍历子工单id列表，递归获取子工单的下级子工单
            subWorkOrderList.forEach(workOrder -> {
                WorkOrderVO subWorkOrder = this.querySubWorkOrder(workOrder.getId(), workOrderProgressVOList, workOrderProcessProgressVOList);
                WorkOrderProgressVO workOrderProgressVO = new WorkOrderProgressVO();
                WorkOrderVO subWorkOrderVO = new WorkOrderVO();
                BeanUtils.copyProperties(subWorkOrder, workOrderProgressVO);
                BeanUtils.copyProperties(subWorkOrder, subWorkOrderVO);
                workOrderProgressVOList.add(workOrderProgressVO);
                subWorkOrderVOList.add(subWorkOrderVO);
            });
        }
        workOrderVO.setSubWorkOrderVOList(subWorkOrderVOList);
        return workOrderVO;
    }

    /**
     * 赋值工单工序进度
     *
     * @param id
     * @param orderLayer
     * @param workOrderProcessProgressVOList
     */
    private void getWorkOrderProcessProgressVO(String id, Integer orderLayer, List<WorkOrderProcessProgressVO> workOrderProcessProgressVOList) {
        List<WorkProcessTaskVO> workProcessTaskVOList = workProcessTaskMapper.queryWorkProcessTaskVOByOrderId(id);
        Collections.sort(workProcessTaskVOList);
        BigDecimal bigDecimal = new BigDecimal(100);
        for (WorkProcessTaskVO workProcessTaskVO : workProcessTaskVOList) {
            WorkOrderProcessProgressVO workOrderProcessProgressVO = new WorkOrderProcessProgressVO();
            BeanUtils.copyProperties(workProcessTaskVO, workOrderProcessProgressVO);
            workOrderProcessProgressVO.setCompletedQty(workProcessTaskVO.getCompletedQty());
            workOrderProcessProgressVO.setScheduledQty(workProcessTaskVO.getScheduledQty());
            workOrderProcessProgressVO.setPublishQty((workProcessTaskVO.getPublishQty()));
            workOrderProcessProgressVO.setOrderLayer(orderLayer);
            workOrderProcessProgressVO.setUnit(workProcessTaskVO.getUnit());
            workOrderProcessProgressVO.setCompletedPercentage(workProcessTaskVO.getCompletedQty().multiply(bigDecimal).divide(workProcessTaskVO.getPlanQty(), 10, RoundingMode.HALF_UP));
            workOrderProcessProgressVOList.add(workOrderProcessProgressVO);
        }
    }

    /**
     * 获取最上层工单的id
     *
     * @param orderId
     * @return
     */
    private String getFirstOrderId(String orderId) {
        WorkOrder workOrder = this.getById(orderId);
        String fatherOrderId = workOrder.getFatherOrderId();
        if (workOrder.getOrderLayer() != 1 && StringUtils.isNotBlank(fatherOrderId)) {
            return getFirstOrderId(fatherOrderId);
        } else {
            return orderId;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(WorkOrderVO workOrderVO) {
        WorkOrder workOrder = new WorkOrder();
        BeanUtils.copyProperties(workOrderVO, workOrder);
        List<WorkOrderLine> workOrderLineList = workOrderLineMapper.selectByMainId(workOrder.getId());
        List<WorkOrderBom> workOrderBomList = workOrderVO.getWorkOrderBomList();
        List<WorkOrderItemBom> workOrderItemBomList = workOrderVO.getWorkOrderItemBomList();

        //更新批次号
        if (workOrderVO.getStatus().equals(WorkOrderStatusEnum.PLAN.getValue()) && StringUtils.isBlank(workOrderVO.getBatchNo()) && WorkOrderBatchTypeEnum.RULE.getValue().equals(workOrder.getBatchType())) {
            // 生成单号
            String batchNo = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.WORK_ORDER_BATCH_NO, workOrder);
            workOrder.setBatchNo(batchNo);
        }

        // 数据合规性检查
        this.checkCondition(workOrder);
        //检查SOP
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.SOP_TEMPLATE_SWITCH);
        boolean switchIdent = ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue());
        if (switchIdent) {
            this.checkSop(workOrderVO);
        }
        // 先查出原来工单
        WorkOrder oldWorkOrder = this.getById(workOrder.getId());
        // 更新主表
        workOrderMapper.updateById(workOrder);

        // 如果不是计划状态，工艺不让修改,主单里面的页面控制不让修改
        String status = workOrder.getStatus();
        if (WorkOrderStatusEnum.PLAN.getValue().equals(status)) {
            //1.先删除子表数据
            List<WorkOrderLine> workOrderLines = workOrderLineMapper.selectByMainId(workOrder.getId());
            workOrderLines.forEach(workOrderLine -> workOrderLineMethodMapper.deleteByMainId(workOrderLine.getId()));
            workOrderLineMapper.deleteByMainId(workOrder.getId());
            workOrderBomMapper.deleteByMainId(workOrder.getId());
            workOrderItemBomMapper.deleteByMainId(workOrder.getId());
            // 保存明细
            this.saveWorkOrderDetail(workOrder, workOrderLineList, workOrderBomList, workOrderItemBomList);
            // 生成工序任务
            this.saveWorkProcessTask(workOrder, workOrderLineList);
        } else {
            if (!BigDecimalUtils.equal(oldWorkOrder.getPlanQty(), workOrder.getPlanQty())) {
                // 更新产品BOM ，或者物料BOM中用量
                this.updateDetailTotalQty(workOrder, workOrderBomList, workOrderItemBomList);
                // 更新工序任务数量
                this.updateWorkProcessTaskQty(workOrder, workOrderLineList);
            }
        }

        // 保存订单明细
        this.saveSaleLine(workOrder, workOrderVO.getSaleOrderLineList());

        //保存自定义列
        workOrderVO.setId(workOrder.getId());
        defineFieldValueService.saveDefineFieldValue(workOrderVO.getLstDefineFields(),
                TableCodeConstants.WORK_ORDER_TABLE_CODE, workOrder.getId());
    }

    /**
     * 保存销售订单行
     *
     * @param workOrder
     * @param saleOrderLineList
     * @date 2021年1月25日
     */
    private void saveSaleLine(WorkOrder workOrder, List<WorkOrderRelateSale> saleOrderLineList) {
        if (StringUtils.isNotBlank(workOrder.getId())) {
            List<WorkOrderRelateSale> lstWorkOrderRelateSale =
                    workOrderRelateSaleMapper.selectByMainId(workOrder.getId());
            for (WorkOrderRelateSale entity : lstWorkOrderRelateSale) {
                String saleOrderLineId = entity.getSaleOrderLineId();
                saleOrderLineMapper.updateDecreaseSaleOrderLinePlanQty(entity.getPlanQty(), saleOrderLineId);
            }
            workOrderRelateSaleMapper.deleteByMainId(workOrder.getId());
        }

        if (ProduceTypeEnum.PRODUCE_SALE.getValue().equals(workOrder.getProductType())) {

            if (CommonUtil.isEmptyOrNull(saleOrderLineList)) {
                return;
            }

            for (WorkOrderRelateSale entity : saleOrderLineList) {
                // 外键设置
                entity.setId(null);
                entity.setOrderId(workOrder.getId());
                entity.setOrderNo(workOrder.getOrderNo());
                CommonUtil.setSysParam(entity, workOrder);
                saleOrderLineMapper.updateAddSaleOrderLinePlanQty(entity.getPlanQty(), entity.getSaleOrderLineId());
            }
            workOrderRelateSaleMapper.insertBatchSomeColumn(saleOrderLineList);
        }

    }

    /**
     * 保存时候保存明细信息
     *
     * @param workOrder
     * @param workOrderLineList
     * @param workOrderBomList
     * @param workOrderItemBomList
     * @date 2020年11月17日
     */
    private void saveWorkOrderDetail(WorkOrder workOrder, List<WorkOrderLine> workOrderLineList,
                                     List<WorkOrderBom> workOrderBomList, List<WorkOrderItemBom> workOrderItemBomList) {
        // 工单工艺
        String workflowType = workOrder.getWorkflowType();
        //删除旧数据
        if (!CommonUtil.isEmptyOrNull(workOrderLineList)) {
            for (WorkOrderLine entity : workOrderLineList) {
                deleteOldData(entity.getId());
            }
        }
        // 工序明细 在工艺路线下保存
        boolean condition = (WorkflowTypeEnum.ROUTE.getValue().equals(workflowType)
                || WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType))
                && !CommonUtil.isEmptyOrNull(workOrderLineList);
        if (condition) {
            List<WorkOrderLine> lstWorkOrderLine = new ArrayList<WorkOrderLine>(8);
            for (WorkOrderLine entity : workOrderLineList) {
                // 外键设置
                entity.setId(null);
                entity.setOrderId(workOrder.getId());
                CommonUtil.setSysParam(entity, workOrder);
                lstWorkOrderLine.add(entity);
            }
            workOrderLineMapper.insertBatchSomeColumn(lstWorkOrderLine);
            //设置关联工位和质检方案
            this.setLineData(workOrderLineList, workflowType, workOrder.getProductId());

            // 设置前后道工序
            this.setProcessLink(lstWorkOrderLine);
        }

        // 工单产品BOM
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)
                && !CommonUtil.isEmptyOrNull(workOrderBomList)) {
            List<WorkOrderBom> lstWorkOrderBom = new ArrayList<WorkOrderBom>(8);
            // 计算工序的总用量
            this.calculateUnitMultiple(workOrder, workOrderBomList);

            for (WorkOrderBom entity : workOrderBomList) {
                // 外键设置
                entity.setId(null);
                entity.setOrderId(workOrder.getId());
                CommonUtil.setSysParam(entity, workOrder);
                lstWorkOrderBom.add(entity);
            }
            workOrderBomMapper.insertBatchSomeColumn(lstWorkOrderBom);

            // 产品BOM复制一份数据到WorkOrderLine
            QueryWrapper<ProductLine> queryWrapper = new QueryWrapper<ProductLine>();
            queryWrapper.eq("product_id", workOrder.getProductId());
            List<ProductLine> lstProductLine = productLineService.list(queryWrapper);
            List<WorkOrderLine> lstWorkOrderLine = new ArrayList<WorkOrderLine>(8);

            for (ProductLine productLine : lstProductLine) {
                WorkOrderLine workOrderLine = new WorkOrderLine();
                BeanUtils.copyProperties(productLine, workOrderLine);
                workOrderLine.setId(null);
                workOrderLine.setOrderId(workOrder.getId());
                CommonUtil.setSysParam(workOrderLine, workOrder);
                lstWorkOrderLine.add(workOrderLine);
            }
            workOrderLineMapper.insertBatchSomeColumn(lstWorkOrderLine);
            //设置关联工位、质检方案和参数模板
            this.setLineData(lstWorkOrderLine, workflowType, workOrder.getProductId());
            // 设置前后道工序
            this.setProcessLink(lstWorkOrderLine);
        }

        // 物料清单BOM
        if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType)
                && !CommonUtil.isEmptyOrNull(workOrderItemBomList)) {
            List<WorkOrderItemBom> lstWorkOrderItemBom = new ArrayList<WorkOrderItemBom>(8);

            for (WorkOrderItemBom entity : workOrderItemBomList) {
                // 外键设置
                entity.setOrderId(workOrder.getId());
                CommonUtil.setSysParam(entity, workOrder);
                lstWorkOrderItemBom.add(entity);
            }
            this.calculateItemBomTotal(workOrder, workOrderItemBomList);
            workOrderItemBomMapper.insertBatchSomeColumn(lstWorkOrderItemBom);
        }
    }

    /**
     * 根据行id删除旧的关联工位、质检任务和参数模板
     */
    private void deleteOldData(String lineId) {
        workOrderLineStationMapper.deleteByMainId(lineId);
        workOrderLineMethodMapper.deleteByMainId(lineId);
        workOrderLineParaMapper.deleteByMainId(lineId);
    }

    /**
     * 根据工序新增关联工位、质检任务和参数模板
     *
     * @param workOrderLineList
     * @param workflowType
     * @param productId
     */
    private void setLineData(List<WorkOrderLine> workOrderLineList, String workflowType, String productId) {
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");
        for (WorkOrderLine workOrderLine : workOrderLineList) {
            List<String> workstationIds = new ArrayList<>();
            //关联工位
            if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
                workstationIds = workProcessTaskServiceImpl.getWorkstationIdsByAdd(productId, workflowType, workOrderLine.getProcessId(), workOrderLine.getSeq());
            } else {
                workstationIds = workProcessTaskServiceImpl.getWorkstationIdsByAdd(workOrderLine.getRouteId(), workflowType, workOrderLine.getProcessId(), workOrderLine.getSeq());
            }
            for (String workstationId : workstationIds) {
                Workstation workstation = workstationMapper.selectById(workstationId);
                WorkOrderLineStation workOrderLineStation = new WorkOrderLineStation();
                BeanUtils.copyProperties(workOrderLine, workOrderLineStation, ignoreProperty);
                workOrderLineStation.setWorkOrderLineId(workOrderLine.getId());
                if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
                    workOrderLineStation.setRelatedType(WorkOrderLineRelatedTypeEnum.PRODUCT_BOM.getValue());
                } else {
                    workOrderLineStation.setRelatedType(WorkOrderLineRelatedTypeEnum.ROUTE.getValue());
                }
                workOrderLineStation.setStationId(workstationId);
                workOrderLineStation.setStationName(workstation.getStationName());
                workOrderLineStationMapper.insert(workOrderLineStation);
            }
            //关联质检方案
            List<?> data = listQcMethodByWorkflowType(workOrderLine.getProcessId(), productId, workOrderLine.getRouteId()).getData();
            for (Object object : data) {
                WorkOrderLineMethod workOrderLineMethod = new WorkOrderLineMethod();
                BeanUtils.copyProperties(workOrderLine, workOrderLineMethod, ignoreProperty);
                workOrderLineMethod.setWorkOrderLineId(workOrderLine.getId());
                if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
                    workOrderLineMethod.setRelatedType(WorkOrderLineRelatedTypeEnum.PRODUCT_BOM.getValue());
                } else {
                    workOrderLineMethod.setRelatedType(WorkOrderLineRelatedTypeEnum.ROUTE.getValue());
                }
                workOrderLineMethod.setQcMethodId(((RouteLineMethodVO) object).getQcMethodId());
                workOrderLineMethod.setQcMethodName(((RouteLineMethodVO) object).getMethodName());
                workOrderLineMethod.setQcType(((RouteLineMethodVO) object).getQcType());
                workOrderLineMethodMapper.insert(workOrderLineMethod);
            }
            //关联参数
            List<WorkOrderLinePara> workOrderLineParaList = listParaByWorkflowType(workOrderLine.getProcessId(), productId, workOrderLine.getRouteId(), workOrderLine.getId(), workOrderLine.getSeq());
            if (!CommonUtil.isEmptyOrNull(workOrderLineParaList)) {
                workOrderLineParaList.forEach(workOrderLinePara -> {
                    workOrderLinePara.setId(null);
                    workOrderLinePara.setProcessCode(workOrderLine.getProcessCode());
                    workOrderLinePara.setProcessId(workOrderLine.getProcessId());
                    workOrderLinePara.setProcessName(workOrderLine.getProcessName());
                });
                workOrderLineParaMapper.insertBatchSomeColumn(workOrderLineParaList);
            }
        }
    }

    /**
     * 更新物料总用量
     *
     * @param workOrder
     * @param workOrderBomList
     * @param workOrderItemBomList
     * @date 2020年12月16日
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void updateDetailTotalQty(WorkOrder workOrder, List<WorkOrderBom> workOrderBomList,
                                      List<WorkOrderItemBom> workOrderItemBomList) {
        String workflowType = workOrder.getWorkflowType();

        // 工单产品BOM
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)
                && !CommonUtil.isEmptyOrNull(workOrderBomList)) {
            this.calculateUnitMultiple(workOrder, workOrderBomList);

            // 用Id指定更新产品BOM计划总数
            QueryWrapper<WorkOrderBom> queryWrapper = new QueryWrapper<>();
            WorkOrderBom updateWorkOrderBom = new WorkOrderBom();
            for (WorkOrderBom entity : workOrderBomList) {
                queryWrapper.clear();
                queryWrapper.eq("id", entity.getId());
                updateWorkOrderBom.setTotalQty(entity.getTotalQty());
                workOrderBomMapper.update(updateWorkOrderBom, queryWrapper);
            }
        }

        // 物料清单BOM
        if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType)
                && !CommonUtil.isEmptyOrNull(workOrderItemBomList)) {
            this.calculateItemBomTotal(workOrder, workOrderItemBomList);
            QueryWrapper<WorkOrderItemBom> itemQueryWrapper = new QueryWrapper<>();
            WorkOrderItemBom updateWorkOrderItemBom = new WorkOrderItemBom();
            for (WorkOrderItemBom entity : workOrderItemBomList) {
                itemQueryWrapper.clear();
                itemQueryWrapper.eq("id", entity.getId());
                updateWorkOrderItemBom.setTotalQty(entity.getTotalQty());
                workOrderItemBomMapper.update(updateWorkOrderItemBom, itemQueryWrapper);
            }
        }
    }

    /**
     * 生成工序任务
     *
     * @param workOrder
     * @param workOrderLineList
     * @param workOrderBomList
     * @param workOrderItemBomList
     * @date 2020年11月19日
     */
    private void saveWorkProcessTask(WorkOrder workOrder, List<WorkOrderLine> workOrderLineList) {
        // 工单在计划状态删除工单任务
        QueryWrapper<WorkProcessTask> delQuery = new QueryWrapper();
        delQuery.eq("order_id", workOrder.getId());
        workProcessTaskMapper.delete(delQuery);
        // 根据工单查询工单工序
        QueryWrapper<WorkOrderLine> workOrderLineQuery = new QueryWrapper();
        workOrderLineQuery.eq("order_id", workOrder.getId());
        workOrderLineQuery.orderByDesc("seq");
        List<WorkOrderLine> lstWorkOrderLine = workOrderLineMapper.selectList(workOrderLineQuery);
        if (CommonUtil.isEmptyOrNull(lstWorkOrderLine)) {
            return;
        }
        List<WorkProcessTask> lstWorkProcessTask = new ArrayList<WorkProcessTask>(lstWorkOrderLine.size());
        for (WorkOrderLine workOrderLine : lstWorkOrderLine) {
            WorkProcessTask workProcessTask = new WorkProcessTask();
            workProcessTask.setBatchNo(workOrder.getBatchNo());
            workProcessTask.setOrderId(workOrder.getId());
            workProcessTask.setOrderNo(workOrder.getOrderNo());
            workProcessTask.setItemId(workOrder.getItemId());
            workProcessTask.setItemCode(workOrder.getItemCode());
            workProcessTask.setItemName(workOrder.getItemName());
            workProcessTask.setSeq(workOrderLine.getSeq());
            workProcessTask.setProcessId(workOrderLine.getProcessId());
            workProcessTask.setProcessCode(workOrderLine.getProcessCode());
            workProcessTask.setProcessName(workOrderLine.getProcessName());
            workProcessTask.setLinkType(workOrderLine.getLinkType());
            workProcessTask.setScheduledQty(BigDecimal.ZERO);
            workProcessTask.setCompletedQty(BigDecimal.ZERO);
            workProcessTask.setPublishQty(BigDecimal.ZERO);
            workProcessTask.setUnit(workOrder.getUnit());
            workProcessTask.setSpec(workOrder.getSpec());
            CommonUtil.setSysParam(workProcessTask, workOrder);
            lstWorkProcessTask.add(workProcessTask);
        }
        this.calculateTaskPlanQty(workOrder, lstWorkProcessTask);
        workProcessTaskMapper.insertBatchSomeColumn(lstWorkProcessTask);
        this.setWorkProcessTask(lstWorkProcessTask);
    }

    /**
     * 更新工序任务数量
     *
     * @param workOrder
     * @param workOrderLineList
     * @date 2021年1月13日
     */
    private void updateWorkProcessTaskQty(WorkOrder workOrder, List<WorkOrderLine> workOrderLineList) {
        // 如果不是计划状态的，重新计算一次计划完成数
        QueryWrapper<WorkProcessTask> workProcessTaskQuery = new QueryWrapper();
        workProcessTaskQuery.eq("order_id", workOrder.getId());
        workProcessTaskQuery.orderByDesc("seq");
        List<WorkProcessTask> lstWorkProcessTask = workProcessTaskMapper.selectList(workProcessTaskQuery);
        this.calculateTaskPlanQty(workOrder, lstWorkProcessTask);

        // 用Id指定更新计划数
        QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        for (WorkProcessTask entity : lstWorkProcessTask) {
            queryWrapper.clear();
            queryWrapper.eq("id", entity.getId());
            updateWorkProcessTask.setPlanQty(entity.getPlanQty());
            workProcessTaskMapper.update(updateWorkProcessTask, queryWrapper);
        }
    }

    /**
     * 计算工序任务的计划数
     *
     * @param workOrder
     * @param lstWorkProcessTask 按倒序的List
     * @date 2020年11月19日
     */
    private void calculateTaskPlanQty(WorkOrder workOrder, List<WorkProcessTask> lstWorkProcessTask) {
        if (CommonUtil.isEmptyOrNull(lstWorkProcessTask)) {
            return;
        }
        // 最后一道工序计划数默认就是工单数量
        WorkProcessTask workProcessTask = lstWorkProcessTask.get(0);
        BigDecimal workOrderPlanQty = workOrder.getPlanQty();
        workProcessTask.setPlanQty(workOrderPlanQty);

        // 把工序转换为key-value方式
        List<WorkOrderLine> lstWorkOrderLine = workOrderLineMapper.selectByMainId(workOrder.getId());
        Map<Integer, WorkOrderLine> map = new HashMap<Integer, WorkOrderLine>(lstWorkOrderLine.size());
        for (WorkOrderLine workOrderLine : lstWorkOrderLine) {
            map.put(workOrderLine.getSeq(), workOrderLine);
        }

        // 计算产出比 outQty/inQty
        WorkOrderLine workOrderLine = map.get(workProcessTask.getSeq());
        BigDecimal ratio = BigDecimalUtils.divide(workOrderLine.getOutQty(), workOrderLine.getInQty(), 6);

        String workFlowType = workOrder.getWorkflowType();
        BigDecimal unitMultiple = BigDecimal.ONE;
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workFlowType)) {
            Product productBom = productService.getById(workOrder.getProductId());
            // 根据物料定义中转换单位获取对应的记录
            List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(workOrder.getItemId());
            ItemUnit productBomUnit = null;
            ItemUnit workOrderUnit = null;
            for (ItemUnit itemUnit : lstItemUnit) {
                if (itemUnit.getConvertUnit().equals(productBom.getUnit())) {
                    productBomUnit = itemUnit;
                }
                if (itemUnit.getConvertUnit().equals(workOrder.getUnit())) {
                    workOrderUnit = itemUnit;
                }
            }

            // 把要生成的数量统一换算成主单位计算,工单需要生产多少份的产品 BOM
            BigDecimal num1 = BigDecimalUtils.multiply(
                    BigDecimalUtils.multiply(workOrder.getPlanQty(), workOrderUnit.getMainUnitQty()),
                    productBomUnit.getConvertQty());

            BigDecimal num2 = BigDecimalUtils.multiply(
                    BigDecimalUtils.multiply(productBom.getQty(), productBomUnit.getMainUnitQty()),
                    workOrderUnit.getConvertQty());

            unitMultiple = BigDecimalUtils.divide(num1, num2, 6);

        }

        // 后续工单计划数计算
        BigDecimal processPlanQty = null;
        for (int i = 1; i < lstWorkProcessTask.size(); i++) {
            WorkProcessTask tempWorkProcessTask = lstWorkProcessTask.get(i);
            WorkOrderLine tempWorkOrderLine = map.get(tempWorkProcessTask.getSeq());
            // 本道工序的产出比
            BigDecimal objRatio =
                    BigDecimalUtils.divide(tempWorkOrderLine.getOutQty(), tempWorkOrderLine.getInQty(), 6);

            if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workFlowType)) {
                processPlanQty = BigDecimalUtils.divide(BigDecimalUtils.multiply(unitMultiple, objRatio), ratio, 6);
            } else {
                processPlanQty = BigDecimalUtils.divide(BigDecimalUtils.multiply(workOrderPlanQty, objRatio), ratio, 6);
            }
            tempWorkProcessTask.setPlanQty(processPlanQty);

        }
    }

    /**
     * 设置工序任务链表
     *
     * @param lstWorkOrderLine
     * @date 2020年11月23日
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void setProcessLink(List<WorkOrderLine> lstWorkOrderLine) {
        List<WorkOrderLine> workOrderLineSortList =
                lstWorkOrderLine.stream().sorted(Comparator.comparing(WorkOrderLine::getSeq)).collect(Collectors.toList());

        WorkOrderLine preNode = workOrderLineSortList.get(0);
        for (int i = 1; i < workOrderLineSortList.size(); i++) {
            WorkOrderLine temp = workOrderLineSortList.get(i);
            temp.setPriorCode(preNode.getId());
            preNode.setNextCode(temp.getId());
            preNode = temp;
        }
        workOrderLineSortList.get(0).setPriorCode(MesCommonConstant.ROUTE_PROCESS_FIRST);
        workOrderLineSortList.get(workOrderLineSortList.size() - 1).setNextCode(MesCommonConstant.ROUTE_PROCESS_END);
        // 更新到数据库中
        QueryWrapper<WorkOrderLine> queryWrapper = new QueryWrapper<>();
        WorkOrderLine updateWorkOrderLine = new WorkOrderLine();
        for (WorkOrderLine workOrderLine : workOrderLineSortList) {
            queryWrapper.clear();
            queryWrapper.eq("id", workOrderLine.getId());
            updateWorkOrderLine.setPriorCode(workOrderLine.getPriorCode());
            updateWorkOrderLine.setNextCode(workOrderLine.getNextCode());
            workOrderLineMapper.update(updateWorkOrderLine, queryWrapper);
        }
    }

    /**
     * 设置工序任务链表
     *
     * @param lstWorkProcessTask
     * @date 2020年11月23日
     */
    private void setWorkProcessTask(List<WorkProcessTask> lstWorkProcessTask) {

        List<WorkProcessTask> workProcessTaskSortList = lstWorkProcessTask.stream()
                .sorted(Comparator.comparing(WorkProcessTask::getSeq)).collect(Collectors.toList());

        WorkProcessTask preNode = workProcessTaskSortList.get(0);
        for (int i = 1; i < workProcessTaskSortList.size(); i++) {
            WorkProcessTask temp = workProcessTaskSortList.get(i);
            temp.setPriorCode(preNode.getId());
            preNode.setNextCode(temp.getId());
            preNode = temp;
        }
        workProcessTaskSortList.get(0).setPriorCode(MesCommonConstant.ROUTE_PROCESS_FIRST);
        workProcessTaskSortList.get(workProcessTaskSortList.size() - 1)
                .setNextCode(MesCommonConstant.ROUTE_PROCESS_END);
        // 更新到数据库中
        QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        for (WorkProcessTask workProcessTask : workProcessTaskSortList) {
            queryWrapper.clear();
            queryWrapper.eq("id", workProcessTask.getId());
            updateWorkProcessTask.setPriorCode(workProcessTask.getPriorCode());
            updateWorkProcessTask.setNextCode(workProcessTask.getNextCode());
            workProcessTaskMapper.update(updateWorkProcessTask, queryWrapper);
        }
    }

    /**
     * 计算物料清单各物料总用量
     *
     * @param workOrder
     * @param workOrderItemBomList
     * @date 2020年12月16日
     */
    private void calculateItemBomTotal(WorkOrder workOrder, List<WorkOrderItemBom> workOrderItemBomList) {
        ItemBom itemBom = itemBomService.getById(workOrder.getBomId());

        // 根据物料定义中转换单位获取对应的记录
        List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(workOrder.getItemId());
        ItemUnit itemBomUnit = null;
        ItemUnit workOrderUnit = null;
        for (ItemUnit itemUnit : lstItemUnit) {
            if (itemUnit.getConvertUnit().equals(itemBom.getUnit())) {
                itemBomUnit = itemUnit;
            }
            if (itemUnit.getConvertUnit().equals(workOrder.getUnit())) {
                workOrderUnit = itemUnit;
            }
        }

        // 把要生成的数量统一换算成主单位计算,工单需要生产多少份的清单 BOM
        BigDecimal num1 =
                BigDecimalUtils.multiply(BigDecimalUtils.multiply(workOrder.getPlanQty(), workOrderUnit.getMainUnitQty()),
                        itemBomUnit.getConvertQty());

        BigDecimal num2 = BigDecimalUtils.multiply(
                BigDecimalUtils.multiply(itemBom.getQty(), itemBomUnit.getMainUnitQty()), workOrderUnit.getConvertQty());

        BigDecimal unitMultiple = BigDecimalUtils.divide(num1, num2, 6);

        for (WorkOrderItemBom workOrderItemBom : workOrderItemBomList) {
            workOrderItemBom.setId(null);
            workOrderItemBom.setTotalQty(
                    BigDecimalUtils.multiply(workOrderItemBom.getQty(), unitMultiple).setScale(4,
                            BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 计算产品 BOM 工序物料的总用量
     *
     * @param workOrder
     * @param workOrderBomList
     * @date 2020年11月17日
     */
    private void calculateUnitMultiple(WorkOrder workOrder, List<WorkOrderBom> workOrderBomList) {

        Product productBom = productService.getById(workOrder.getProductId());

        // 根据物料定义中转换单位获取对应的记录
        List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(workOrder.getItemId());
        ItemUnit productItemUnit = null;
        ItemUnit workOrderUnit = null;
        for (ItemUnit itemUnit : lstItemUnit) {
            if (itemUnit.getConvertUnit().equals(productBom.getUnit())) {
                productItemUnit = itemUnit;
            }
            if (itemUnit.getConvertUnit().equals(workOrder.getUnit())) {
                workOrderUnit = itemUnit;
            }
        }

        // 把要生成的数量统一换算成主单位计算,工单需要生产多少份的产品 BOM
        BigDecimal num1 =
                BigDecimalUtils.multiply(BigDecimalUtils.multiply(workOrder.getPlanQty(), workOrderUnit.getMainUnitQty()),
                        productItemUnit.getConvertQty());

        BigDecimal num2 =
                BigDecimalUtils.multiply(BigDecimalUtils.multiply(productBom.getQty(), productItemUnit.getMainUnitQty()),
                        workOrderUnit.getConvertQty());

        BigDecimal unitMultiple = BigDecimalUtils.divide(num1, num2, 6);

        for (WorkOrderBom workOrderBom : workOrderBomList) {
            workOrderBom.setTotalQty(
                    BigDecimalUtils.multiply(workOrderBom.getQty(), unitMultiple).setScale(4, BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 标准作业模板，检查所选工艺路线或所选生产BOM的每道工序是否挂了启用中的标准作业模板
     *
     * @param workOrder         工单
     * @param workOrderLineList 工序列表
     */
    private void checkSop(WorkOrderVO workOrderVO) {
        String workflowType = workOrderVO.getWorkflowType();


        List<String> processCodeList = new ArrayList<>();
        String entityCode = "";
        String templateType = "";
        if (WorkflowTypeEnum.ROUTE.getValue().equals(workflowType) || WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType)) {
            entityCode = workOrderVO.getRouteId();
            templateType = TemplateTypeEnum.ROUTE.getValue();
            workOrderVO.getWorkOrderLineList().forEach(workOrderLine -> processCodeList.add(workOrderLine.getProcessCode()));
        } else if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
            entityCode = workOrderVO.getProductId();
            templateType = TemplateTypeEnum.PRODUCT_BOM.getValue();
            workOrderVO.getWorkOrderBomList().forEach(workOrderBom -> processCodeList.add(workOrderBom.getProcessCode()));
        }
        for (String processCode : processCodeList) {
            QueryWrapper<SopTemplate> sopQueryWrapper = new QueryWrapper<>();
            sopQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
            sopQueryWrapper.eq("process_code", processCode);
            sopQueryWrapper.eq("entity_code", entityCode);
            sopQueryWrapper.eq("template_type", templateType);
            List<SopTemplate> sopTemplateList = sopTemplateMapper.selectList(sopQueryWrapper);
            if (CollectionUtil.isEmpty(sopTemplateList) || sopTemplateList.size() > 1) {
                throw new ILSBootException("P-SOP-0093", processCode);
            }
        }
        ;
    }

    /**
     * 检查工单保存或者更新是否满足要求
     *
     * @param workOrder
     * @return
     * @date 2021年1月13日
     */
    public boolean checkCondition(WorkOrder workOrder) {
        if (StringUtils.isNotBlank(workOrder.getId())) {
            WorkOrder oldWorkOrder = this.getById(workOrder.getId());
            if (!oldWorkOrder.getStatus().equals(workOrder.getStatus())) {
                throw new ILSBootException("P-OW-0007");
            }
            String status = workOrder.getStatus();
            if (!WorkOrderStatusEnum.PLAN.getValue().equals(status)) {
                if (BigDecimalUtils.greaterThan(oldWorkOrder.getPlanQty(), workOrder.getPlanQty())) {
                    throw new ILSBootException("P-OW-0057");
                }
            }
        }

        // 编码不重复
        QueryWrapper<WorkOrder> queryWrapper = new QueryWrapper<WorkOrder>();
        queryWrapper.eq("order_no", workOrder.getOrderNo());
        if (StringUtils.isNoneBlank(workOrder.getId())) {
            queryWrapper.ne("id", workOrder.getId());
        }
        WorkOrder obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("P-OW-0005");
        }

        //itemId物料定义中的ID
        String itemId = workOrder.getItemId();
        String unit = workOrder.getUnit();

        // 查询物料定义中的转换单位
        List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(itemId);
        List<String> units =
                lstItemUnit.stream().map(itemUnit -> itemUnit.getConvertUnit()).collect(Collectors.toList());

        // 如果工单单位为空或者不是物料定义里面的转换单位
        if (StringUtils.isBlank(unit) || !units.contains(unit)) {
            throw new ILSBootException("P-OW-0001");
        }

        String workflowType = workOrder.getWorkflowType();

        // 校验物料 BOM 为物料定义下的版本
        if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workflowType)) {
            String bomId = workOrder.getBomId();
            ItemBom itemBom = itemBomService.getById(bomId);
            if (itemBom == null || !itemId.equals(itemBom.getItemId())) {
                throw new ILSBootException("P-OW-0003");
            }
        }

        // 校验产品 BOM 为物料定义下的版本
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
            String productId = workOrder.getProductId();
            Product productBom = productService.getById(productId);
            if (productBom == null || !itemId.equals(productBom.getItemId())) {
                throw new ILSBootException("P-OW-0004");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void cancelWorkOrder(String id) {
        WorkOrder workOrder = this.getById(id);
        String status = workOrder.getStatus();
        if (!WorkOrderStatusEnum.PLAN.getValue().equals(status)
                && !WorkOrderStatusEnum.SCHEDULED.getValue().equals(status)) {
            throw new ILSBootException("P-OW-0007");
        }
        workOrder.setStatus(WorkOrderStatusEnum.CANCEL.getValue());
        this.updateById(workOrder);
    }

    @Override
    public WorkOrderVO queryWorkOrderDetail(String id) {
        WorkOrder workOrder = this.getById(id);
        WorkOrderVO workOrderVO = new WorkOrderVO();
        BeanUtils.copyProperties(workOrder, workOrderVO);
        List<WorkOrderLine> workOrderLineList = workOrderLineMapper.selectByMainId(id);
        Collections.sort(workOrderLineList);
        workOrderVO.setWorkOrderLineList(workOrderLineList);
        List<WorkOrderBom> workOrderBomList = workOrderBomMapper.selectByMainId(id);
        workOrderVO.setWorkOrderBomList(workOrderBomList);
        List<WorkOrderItemBom> workOrderItemBomList = workOrderItemBomMapper.selectByMainId(id);
        workOrderVO.setWorkOrderItemBomList(workOrderItemBomList);
        List<WorkOrderRelateSale> saleOrderLineList = workOrderRelateSaleMapper.selectByMainId(id);
        workOrderVO.setSaleOrderLineList(saleOrderLineList);
        workOrderVO.setPmcNameList(getUserNameById(workOrderVO.getPmc()));
        workOrderVO.setDirectorNameList(getUserNameById(workOrderVO.getDirector()));
        return workOrderVO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void batchSaleAdd(List<BatchWorkOrderVO> lstBatchWorkOrderVO) {
        // 定义工单明细变量
        List<WorkOrderLine> workOrderLineList = null;
        List<WorkOrderBom> workOrderBomList = null;
        List<WorkOrderItemBom> workOrderItemBomList = null;
        List<WorkOrderRelateSale> lstSaleOrderLine = null;
        //循环插入数据
        for (BatchWorkOrderVO batchWorkOrderVO : lstBatchWorkOrderVO) {
            WorkOrder workOrder = new WorkOrder();
            BeanUtils.copyProperties(batchWorkOrderVO, workOrder);
            if (WorkOrderBatchTypeEnum.RULE.getValue().equals(workOrder.getBatchType())) {
                // 生成单号
                String batchNo = codeGeneratorService.getNextCode(CommonUtil.getTenantId(),
                        MesCommonConstant.WORK_ORDER_BATCH_NO, workOrder);
                workOrder.setBatchNo(batchNo);
            }
            workOrder.setAuditStatus(AuditStatusEnum.AUDIT_NEW.getValue());
            workOrder.setStatus(WorkOrderStatusEnum.PLAN.getValue());
            workOrder.setProductType(ProduceTypeEnum.PRODUCE_SALE.getValue());
            // 数据合规性检查
            this.checkCondition(workOrder);
            // 插入主表
            workOrderMapper.insert(workOrder);
            String workFlowType = workOrder.getWorkflowType();
            //工艺路线
            if (WorkflowTypeEnum.ROUTE.getValue().equals(workFlowType)
                    || WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workFlowType)) {
                List<RouteLineVO> lstRouteLine = routeLineService.selectByMainId(workOrder.getRouteId());
                workOrderLineList = lstRouteLine.stream().map(routeLine -> {
                    WorkOrderLine workOrderLine = new WorkOrderLine();
                    BeanUtils.copyProperties(routeLine, workOrderLine);
                    workOrderLine.setId(null);
                    return workOrderLine;
                }).collect(Collectors.toList());
            }
            //工艺路线+物料清单
            if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(workFlowType)) {
                List<ItemBomUnitVO> lstItemBom = itemBomDetailService.selectDetailInfoByMainId(workOrder.getBomId());
                workOrderItemBomList = lstItemBom.stream().map(itemBom -> {
                    WorkOrderItemBom workOrderItemBom = new WorkOrderItemBom();
                    BeanUtils.copyProperties(itemBom, workOrderItemBom);
                    workOrderItemBom.setId(null);
                    return workOrderItemBom;
                }).collect(Collectors.toList());
            }
            //产品BOM
            if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workFlowType)) {
                List<ProductLineBomVO> lstProductLineBomVO =
                        productService.queryProductLineBomById(workOrder.getProductId());
                workOrderBomList = lstProductLineBomVO.stream().map(productLineBom -> {
                    WorkOrderBom workOrderBom = new WorkOrderBom();
                    BeanUtils.copyProperties(productLineBom, workOrderBom);
                    workOrderBom.setId(null);
                    return workOrderBom;
                }).collect(Collectors.toList());
            }
            lstSaleOrderLine = this.convertWorkOrderRelateSale(batchWorkOrderVO);
            // 保存明细
            this.saveWorkOrderDetail(workOrder, workOrderLineList, workOrderBomList, workOrderItemBomList);
            // 生成工序任务
            this.saveWorkProcessTask(workOrder, workOrderLineList);
            // 保存订单明细
            this.saveSaleLine(workOrder, lstSaleOrderLine);
            //检查SOP
            BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.SOP_TEMPLATE_SWITCH);
            boolean switchIdent = ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue());
            if (switchIdent) {
                WorkOrderVO workOrderVO = new WorkOrderVO();
                BeanUtils.copyProperties(workOrder, workOrderVO);
                workOrderVO.setWorkOrderBomList(workOrderBomList);
                workOrderVO.setWorkOrderItemBomList(workOrderItemBomList);
                workOrderVO.setWorkOrderLineList(workOrderLineList);
                this.checkSop(workOrderVO);
            }
            // 清空变量引用
            workOrderLineList = null;
            workOrderBomList = null;
            workOrderItemBomList = null;
            lstSaleOrderLine = null;
        }
    }

    /**
     * 转换为工单关联销售订单行明细
     *
     * @param batchWorkOrderVO
     * @return
     * @date 2021年1月26日
     */
    private List<WorkOrderRelateSale> convertWorkOrderRelateSale(BatchWorkOrderVO batchWorkOrderVO) {
        List<WorkOrderRelateSale> lstSaleOrderLine = new ArrayList(1);
        WorkOrderRelateSale workOrderRelateSale = new WorkOrderRelateSale();
        workOrderRelateSale.setSaleOrderId(batchWorkOrderVO.getSaleOrderId());
        workOrderRelateSale.setSaleOrderNo(batchWorkOrderVO.getSaleOrderNo());
        workOrderRelateSale.setSaleOrderLineId(batchWorkOrderVO.getSaleOrderLineId());
        workOrderRelateSale.setLineNumber(batchWorkOrderVO.getLineNumber());
        workOrderRelateSale.setItemId(batchWorkOrderVO.getItemId());
        workOrderRelateSale.setItemCode(batchWorkOrderVO.getItemCode());
        workOrderRelateSale.setItemName(batchWorkOrderVO.getItemName());
        workOrderRelateSale.setSpec(batchWorkOrderVO.getSpec());
        workOrderRelateSale.setPlanQty(batchWorkOrderVO.getPlanQty());
        workOrderRelateSale.setUnit(batchWorkOrderVO.getUnit());
        workOrderRelateSale.setRequiredDate(batchWorkOrderVO.getCreateTime());
        lstSaleOrderLine.add(workOrderRelateSale);

        return lstSaleOrderLine;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void finish(List<String> idList) {
        //查找检查开关
        boolean switchIdent;
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.WORK_ORDER_FINISH_CHECK_SWITCH);
        switchIdent = ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue());
        for (String id : idList) {
            WorkOrder workOrder = baseMapper.selectById(id);
            //跳过已完工的任务
            if (!WorkOrderStatusEnum.FINISH.getValue().equals(workOrder.getStatus())) {
                //是否检查工单任务
                if (switchIdent) {
                    QueryWrapper<WorkPlanTask> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("order_id", id);
                    queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
                    List<WorkPlanTask> workPlanTaskList = workPlanTaskMapper.selectList(queryWrapper);
                    //判断标识
                    boolean checkTask = false;
                    for (WorkPlanTask workPlanTask : workPlanTaskList) {
                        String exeStatus = workPlanTask.getExeStatus();
                        //检查是否不符合完工条件
                        if (PlanTaskExeStatusEnum.NOT_START.getValue().equals(exeStatus)
                                || PlanTaskExeStatusEnum.PRODUCE.getValue().equals(exeStatus)
                                || PlanTaskExeStatusEnum.SUSPEND.getValue().equals(exeStatus)) {
                            checkTask = true;
                            break;
                        }
                    }
                    //不符合完工条件，提示错误
                    if (checkTask) {
                        throw new ILSBootException("P-AU-0076");
                    }
                }
                //更新工单状态
                WorkOrder updateWorkOrder = new WorkOrder();
                updateWorkOrder.setId(workOrder.getId());
                updateWorkOrder.setStatus(WorkOrderStatusEnum.FINISH.getValue());
                updateWorkOrder.setRealEndTime(new Date());
                baseMapper.updateById(updateWorkOrder);
            }
        }
    }

    @Override
    public List<WorkOrderDetailVO> getProgressProductsProcess() {
        //查询开工状态的工单
        QueryWrapper<WorkOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", WorkOrderStatusEnum.STARTED.getValue());
        List<WorkOrder> workOrderList = baseMapper.selectList(queryWrapper);
        List<WorkOrderDetailVO> workOrderDetailVOList = new ArrayList<>();
        //查询工序进度详情
        workOrderList.forEach(workOrder -> {
            WorkOrderVO workOrderVO = new WorkOrderVO();
            BeanUtils.copyProperties(workOrder, workOrderVO);
            WorkOrderDetailVO workOrderDetailVO = new WorkOrderDetailVO();
            List<WorkOrderProcessProgressVO> workOrderProcessProgressVOList = new ArrayList<>();
            workOrderDetailVO.setWorkOrderVO(workOrderVO);
            getWorkOrderProcessProgressVO(workOrder.getId(), workOrder.getOrderLayer(), workOrderProcessProgressVOList);
            workOrderDetailVO.setWorkOrderProcessProgressVOList(workOrderProcessProgressVOList);
            workOrderDetailVOList.add(workOrderDetailVO);
        });
        return workOrderDetailVOList;
    }

    @Override
    public List<WorkOrderLinePara> listParaByWorkflowType(String processId, String productId, String routeId, String workOrderLineId, Integer seq) {
        List<WorkOrderLinePara> workOrderLineParaList = new ArrayList<>();
        //产品bom
        if (StringUtils.isNotBlank(productId)) {
            List<ProductRouteParaVO> productRouteParaVOList = productRouteParaHeadService.queryByProductId(productId);
            productRouteParaVOList.forEach(productRouteParaVO -> {
                ProductLine productLine = productLineService.getById(productRouteParaVO.getProductLineId());
                //工序id匹配
                if (productLine.getProcessId().equals(processId)) {
                    productRouteParaVO.getProductRouteParaDetailList().forEach(detail -> {
                        WorkOrderLinePara workOrderLinePara = new WorkOrderLinePara();
                        BeanUtils.copyProperties(detail, workOrderLinePara);
                        workOrderLinePara.setWorkOrderLineId(workOrderLineId);
                        workOrderLinePara.setRelatedType(WorkOrderParaRelatedTypeEnum.ROUTE_ITEM_BOM.getValue());
                        workOrderLinePara.setSeq(seq);
                        workOrderLineParaList.add(workOrderLinePara);
                    });
                }
            });
        } else {
            if (StringUtils.isBlank(routeId)) {
                throw new ILSBootException("缺少参数！");
            }
            List<RouteLineParaVO> routeLineParaVOList = routeLineParaHeadService.queryByRouteId(routeId);
            routeLineParaVOList.forEach(routeLineParaVO -> {
                RouteLine routeLine = routeLineService.getById(routeLineParaVO.getRouteLineId());
                //工序id匹配
                if (routeLine.getProcessId().equals(processId)) {
                    routeLineParaVO.getRouteLineParaDetailList().forEach(detail -> {
                        WorkOrderLinePara workOrderLinePara = new WorkOrderLinePara();
                        BeanUtils.copyProperties(detail, workOrderLinePara);
                        workOrderLinePara.setWorkOrderLineId(workOrderLineId);
                        workOrderLinePara.setRelatedType(WorkOrderParaRelatedTypeEnum.ROUTE.getValue());
                        workOrderLinePara.setSeq(seq);
                        workOrderLineParaList.add(workOrderLinePara);
                    });
                }
            });
        }
        return workOrderLineParaList;
    }

    @Override
    public ResultDataVO listQcMethodByWorkflowType(String processId, String productId, String routeId) {
        ResultDataVO resultDataVO = new ResultDataVO();
        //产品bom
        if (StringUtils.isNotBlank(productId)) {
            //产品bom关联的质检方案
            List<ProductRouteMethodVO> productRouteMethodVOList = productRouteMethodMapper.selectByProductId(productId);
            List<RouteLineMethodVO> lineList = new ArrayList<>();
            productRouteMethodVOList.forEach(productRouteMethodVO -> {
                ProductLine productLine = productLineService.getById(productRouteMethodVO.getProductLineId());
                //工序id匹配
                if (productLine.getProcessId().equals(processId)) {
                    RouteLineMethodVO routeLineMethodVO = new RouteLineMethodVO();
                    routeLineMethodVO.setMethodName(productRouteMethodVO.getMethodName());
                    routeLineMethodVO.setQcType(productRouteMethodVO.getQcType());
                    lineList.add(routeLineMethodVO);
                }
            });
            resultDataVO.setData(lineList);
        } else {
            if (StringUtils.isBlank(routeId)) {
                throw new ILSBootException("缺少参数！");
            }
            //工艺路线
            List<RouteLineMethodVO> routeLineMethodVOList = routeLineMethodService.selectByRouteId(routeId);
            List<RouteLineMethodVO> lineList = new ArrayList<>();
            routeLineMethodVOList.forEach(routeLineMethodVO -> {
                RouteLine routeLine = routeLineService.getById(routeLineMethodVO.getRouteLineId());
                //工序id匹配
                if (routeLine.getProcessId().equals(processId)) {
                    RouteLineMethodVO emptyRouteLineMethodVO = new RouteLineMethodVO();
                    emptyRouteLineMethodVO.setMethodName(routeLineMethodVO.getMethodName());
                    emptyRouteLineMethodVO.setQcMethodId(routeLineMethodVO.getQcMethodId());
                    emptyRouteLineMethodVO.setQcType(routeLineMethodVO.getQcType());
                    lineList.add(emptyRouteLineMethodVO);
                }
            });
            resultDataVO.setData(lineList);
        }
        return resultDataVO;
    }

    @Override
    public ResultDataVO listQcMethodByOrderId(String processId, String orderId) {
        ResultDataVO resultDataVO = new ResultDataVO();
        List<WorkOrderLine> workOrderLineList = workOrderLineMapper.selectByMainId(orderId);
        List<RouteLineMethodVO> lineList = new ArrayList<>();

        for (WorkOrderLine workOrderLine : workOrderLineList) {
            if (workOrderLine.getProcessId().equals(processId)) {
                List<WorkOrderLineMethod> workOrderLineMethods = workOrderLineMethodMapper.selectByMainId(workOrderLine.getId());
                for (WorkOrderLineMethod workOrderLineMethod : workOrderLineMethods) {
                    RouteLineMethodVO emptyRouteLineMethodVO = new RouteLineMethodVO();
                    emptyRouteLineMethodVO.setMethodName(workOrderLineMethod.getQcMethodName());
                    emptyRouteLineMethodVO.setQcType(workOrderLineMethod.getQcType());
                    lineList.add(emptyRouteLineMethodVO);
                }
            }
        }
        resultDataVO.setData(lineList);

        return resultDataVO;
    }

    @Override
    public Page<RequiredMaterialVO> getRequiredMaterial(Page<RequiredMaterialVO> page, RequiredMaterialVO requiredMaterialVO) {
        QueryWrapper<RequiredMaterialVO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(requiredMaterialVO.getItemName())) {
            queryWrapper.like("item_name", requiredMaterialVO.getItemName());
        }
        if (StringUtils.isNotBlank(requiredMaterialVO.getOrderNo())) {
            queryWrapper.like("order_no", requiredMaterialVO.getOrderNo());
        }
        if (null != requiredMaterialVO.getPlanStartTime() && null != requiredMaterialVO.getPlanEndTime()) {
            queryWrapper.gt("plan_start_time", DateUtil.beginOfDay(requiredMaterialVO.getPlanStartTime()));
            queryWrapper.le("plan_end_time", DateUtil.endOfDay(requiredMaterialVO.getPlanEndTime()));
        }

        Page<RequiredMaterialVO> voPage = workOrderMapper.getRequiredMaterial(page, queryWrapper);
        List<RequiredMaterialVO> records = voPage.getRecords();
        records.forEach(data -> {
            if (null != data.getPlanStartTime() && null != data.getPlanEndTime()) {
                data.setRequiredTime(DateUtil.formatDate(data.getPlanStartTime()) + " ~ " + DateUtil.formatDate(data.getPlanEndTime()));
            }
        });
        return voPage;
    }

    @Override
    public void sendMsg(WorkOrder workOrder) {
        List<String> directorList = Arrays.asList(workOrder.getDirector().split(","));
        List<String> pmcList = Arrays.asList(workOrder.getPmc().split(","));
        List<String> receiverIds = new ArrayList<>();
        receiverIds.addAll(pmcList);
        receiverIds.addAll(directorList);
        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, workOrder.getCreateBy(), workOrder);
        msgParamsVO.setTenantId(TenantContext.getTenant());
        msgHandleServer.sendMsg(MesCommonConstant.MSG_WORK_ORDER, MesCommonConstant.INFORM, msgParamsVO);
    }
}
