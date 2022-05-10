package com.ils.modules.mes.execution.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.vo.MaterialRecordReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.mapper.WorkProduceMaterialRecordMapper;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskMapper;
import com.ils.modules.mes.execution.service.WorkProduceMaterialRecordService;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.execution.vo.GroupMaterialRecordVO;
import com.ils.modules.mes.execution.vo.ItemCellExtendInfo;
import com.ils.modules.mes.execution.vo.MaterialRecordQtyVO;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import com.ils.modules.mes.produce.entity.WorkOrderItemBom;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.service.WorkOrderBomService;
import com.ils.modules.mes.produce.service.WorkOrderItemBomService;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;

/**
 * @Description: 投料记录
 * @Author: fengyi
 * @Date: 2020-12-10
 * @Version: V1.0
 */
@Service
public class WorkProduceMaterialRecordServiceImpl extends ServiceImpl<WorkProduceMaterialRecordMapper, WorkProduceMaterialRecord> implements WorkProduceMaterialRecordService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private WorkProduceTaskService workProduceTaskService;

    @Autowired
    private ItemCellService itemCellService;

    @Autowired
    private WorkOrderBomService workOrderBomService;
    @Autowired
    private WorkOrderItemBomService workOrderItemBomService;

    @Autowired
    private WareStorageService wareStorageService;


    @Autowired
    private WareHouseService wareHouseService;

    @Autowired
    @Lazy
    private WorkPlanTaskService workPlanTaskService;

    @Autowired
    private WorkProcessTaskService workProcessTaskService;

    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkProduceMaterialRecord(WorkProduceMaterialRecord workProduceMaterialRecord) {
        baseMapper.insert(workProduceMaterialRecord);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkProduceMaterialRecord(WorkProduceMaterialRecord workProduceMaterialRecord) {
        baseMapper.updateById(workProduceMaterialRecord);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkProduceMaterialRecord(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkProduceMaterialRecord(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage<GroupMaterialRecordVO> queryMaterialPageList(String produceTaskId, String keyWord,
                                                              Page<GroupMaterialRecordVO> page, String item, String mainItem) {
        QueryWrapper<GroupMaterialRecordVO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(item)) {
            queryWrapper.eq("mes_item.id", item);
        }
        if (StringUtils.isNotEmpty(mainItem)) {
            queryWrapper.ne("mes_item.id", mainItem);
        }
        if (StringUtils.isNoneBlank(keyWord)) {
            queryWrapper.like("(concat(ifnull(mes_item.item_code, ''), ifnull(mes_item.item_name, ''))", keyWord);
        }
        queryWrapper.eq("mes_item.is_qrcode", ZeroOrOneEnum.ZERO.getStrCode());
        WorkProduceTask produceTask = workProduceTaskService.getById(produceTaskId);
        WorkOrder workOrder = workOrderService.getById(produceTask.getOrderId());
        String flowType = workOrder.getWorkflowType();
        IPage<GroupMaterialRecordVO> pageList = null;
        // 产品BOM关联物料
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(flowType)) {
            queryWrapper.eq("c .order_id", workOrder.getId());
            queryWrapper.eq("c .seq", produceTask.getSeq());
            queryWrapper.eq("c .process_id", produceTask.getProcessId());
            pageList = baseMapper.queryGroupProductBomMaterialPageList(produceTaskId, page, queryWrapper, null);
            // 工序任务
            WorkPlanTask workPlanTask = workPlanTaskService.getById(produceTask.getPlanTaskId());
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());

            // bom 方式下 本次生成任务数量：工序任务数量=本次物料最大用量数量：工序上物料用量总量
            List<GroupMaterialRecordVO> lstGroupMaterialRecord = pageList.getRecords();
            if (!CommonUtil.isEmptyOrNull(lstGroupMaterialRecord)) {
                for (GroupMaterialRecordVO groupMaterialRecordVO : lstGroupMaterialRecord) {
                    BigDecimal materialTotalQty = BigDecimalUtils.divide(
                            BigDecimalUtils.multiply(groupMaterialRecordVO.getProcessTotalQty(), produceTask.getPlanQty()),
                            workProcessTask.getPlanQty(), 6);
                    groupMaterialRecordVO.setProcessTotalQty(materialTotalQty);
                }
            }
            // 物料清单物料
        } else if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(flowType)) {
            queryWrapper.eq("mes_work_order_item_bom.order_id", workOrder.getId());
            pageList = baseMapper.queryItemMaterialPageList(page, queryWrapper);
            // 没有标签码的物料
        } else {
            pageList = baseMapper.queryItemPage(page, queryWrapper);
        }
        return pageList;
    }

    @Override
    public ItemCellExtendInfo queryItemCell(String produceTaskId, String qrcode, String stationId, String itemId) {
        QueryWrapper<ItemCell> queryWrapper = new QueryWrapper();
        queryWrapper.eq("qrcode", qrcode);
        queryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        List<ItemCell> lstItemCell = itemCellService.list(queryWrapper);

        // 校验物料单元
        this.checkQcItemCell(lstItemCell, itemId);
        // 校验是否是物料清单中物料或者是产品BOM物料
        ItemCell itemCell = lstItemCell.get(0);
        ItemCellExtendInfo itemCellInfo = new ItemCellExtendInfo();
        BeanUtils.copyProperties(itemCell, itemCellInfo);
        this.checkExistItemOrBom(produceTaskId, itemCell.getItemId(), itemCellInfo);
        // 投料仓校验
        this.checkStationHouse(stationId, itemCell.getStorageId());

        return itemCellInfo;
    }

    /**
     * 扫码时用码校验物料单位
     *
     * @param qccode
     * @date 2020年12月15日
     */
    private void checkQcItemCell(List<ItemCell> lstItemCell, String itemId) {

        if (CommonUtil.isEmptyOrNull(lstItemCell)) {
            throw new ILSBootException("P-OW-0022");
        }
        if (StringUtils.isNotEmpty(itemId)) {
            if (!itemId.equals(lstItemCell.get(0).getItemId())) {
                throw new ILSBootException("该二维码对应的物料不属于模板中管控的物料类型");
            }
        }

        if (lstItemCell.size() > 1) {
            throw new ILSBootException("P-OW-0029");
        }
        ItemCell itemCell = lstItemCell.get(0);

        if (!ItemCellPositionStatusEnum.STORAGE.getValue().equals(itemCell.getPositionStatus())) {
            throw new ILSBootException("P-OW-0023");
        }

        if (!ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(itemCell.getQcStatus())) {
            throw new ILSBootException("P-OW-0024");
        }

        if (!ItemCellBusinessStatusEnum.NONE.getValue().equals(itemCell.getBusinessStatus())) {
            throw new ILSBootException("P-OW-0025");
        }
    }

    /**
     * 校验物料是否存在清单或者BOM中
     *
     * @param produceTaskId
     * @date 2020年12月15日
     */
    private void checkExistItemOrBom(String produceTaskId, String itemId, ItemCellExtendInfo itemCellInfo) {
        WorkProduceTask produceTask = workProduceTaskService.getById(produceTaskId);
        WorkOrder workOrder = workOrderService.getById(produceTask.getOrderId());
        itemCellInfo.setOrderNo(workOrder.getOrderNo());
        itemCellInfo.setStationCode(produceTask.getStationCode());
        itemCellInfo.setStationName(produceTask.getStationName());
        itemCellInfo.setProcessCode(produceTask.getProcessCode());
        itemCellInfo.setProcessName(produceTask.getProcessName());
        String flowType = workOrder.getWorkflowType();
        itemCellInfo.setWorkflowType(flowType);
        int count = 0;
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(flowType)) {
            QueryWrapper<WorkOrderBom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", workOrder.getId());
            queryWrapper.eq("seq", produceTask.getSeq());
            queryWrapper.eq("process_id", produceTask.getProcessId());
            queryWrapper.eq("item_id", itemId);
            List<WorkOrderBom> lstWorkOrderBom = workOrderBomService.list(queryWrapper);
            if (CommonUtil.isEmptyOrNull(lstWorkOrderBom)) {
                throw new ILSBootException("P-OW-0028");
            }
            // 计算标签码在物料上的
            WorkOrderBom workOrderBom = lstWorkOrderBom.get(0);

            // 工序任务
            WorkPlanTask workPlanTask = workPlanTaskService.getById(produceTask.getPlanTaskId());
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
            // 获取已投数量，
            MaterialRecordQtyVO materialRecordQtyVO = baseMapper.queryMaterialRecordQty(produceTask.getId(), itemId);
            BigDecimal feedTotalQty = BigDecimal.ZERO;
            if (materialRecordQtyVO != null) {
                feedTotalQty =
                        BigDecimalUtils.subtract(materialRecordQtyVO.getFeedQty(), materialRecordQtyVO.getUndoQty());
                itemCellInfo.setUndoQty(materialRecordQtyVO.getUndoQty());
            } else {
                itemCellInfo.setUndoQty(BigDecimal.ZERO);
            }

            itemCellInfo.setFeedQty(feedTotalQty);
            // bom 方式下 本次生成任务数量：工序任务数量=本次物料最大用量数量：工序上物料用量总量
            BigDecimal materialTotalQty = BigDecimalUtils.divide(
                    BigDecimalUtils.multiply(workOrderBom.getTotalQty(), produceTask.getPlanQty()),
                    workProcessTask.getPlanQty(), 6);
            itemCellInfo.setProcessTotalQty(materialTotalQty);
        } else if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(flowType)) {
            QueryWrapper<WorkOrderItemBom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", workOrder.getId());
            queryWrapper.eq("item_id", itemId);
            count = workOrderItemBomService.count(queryWrapper);
            if (count == 0) {
                throw new ILSBootException("P-OW-0027");
            }
        }
    }

    /**
     * 校验线边仓
     *
     * @param stationId
     * @param storageId
     * @date 2020年12月15日
     */
    private void checkStationHouse(String stationId, String storageId) {
        // 如果存在线边仓就判断是否有指定的
        int count = wareStorageService.queryWareHouseCount(stationId, null);
        if (count > 0) {
            count = wareStorageService.queryWareHouseCount(stationId, storageId);
            if (count == 0) {
                throw new ILSBootException("P-OW-0026");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void qrCodeAdd(WorkProduceMaterialRecord workProduceMaterialRecord) {
        if (StringUtils.isBlank(workProduceMaterialRecord.getQrcode())) {
            throw new ILSBootException("P-OW-0032");
        }

        if (!MaterialRecordFeedTypeEnum.FEED.getValue().equals(workProduceMaterialRecord.getFeedType())) {
            throw new ILSBootException("P-OW-0039");
        }

        WorkProduceTask produceTask = workProduceTaskService.getById(workProduceMaterialRecord.getProduceTaskId());

        this.checkProduceTaskStatus(produceTask);

        WorkOrder workOrder = workOrderService.getById(produceTask.getOrderId());

        QueryWrapper<ItemCell> queryWrapper = new QueryWrapper();
        queryWrapper.eq("qrcode", workProduceMaterialRecord.getQrcode());
        queryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        List<ItemCell> lstItemCell = itemCellService.list(queryWrapper);
        // 校验物料单元
        this.checkQcItemCell(lstItemCell, null);
        ItemCell itemCell = lstItemCell.get(0);

        // 投料仓校验
        this.checkStationHouse(produceTask.getStationId(), itemCell.getStorageId());

        // 投料时校验是否超过需求数量
        this.checkExceedQty(workProduceMaterialRecord.getFeedQty(), produceTask, workOrder,
                workProduceMaterialRecord.getItemId());

        if (BigDecimalUtils.greaterThan(workProduceMaterialRecord.getFeedQty(), itemCell.getQty())) {
            throw new ILSBootException("P-OW-0034");
        }

        workProduceMaterialRecord.setItemCellStateId(itemCell.getId());
        workProduceMaterialRecord.setFeedStorageId(itemCell.getStorageId());
        workProduceMaterialRecord.setQcStatus(itemCell.getQcStatus());

        this.save(workProduceMaterialRecord);

        QueryWrapper<ItemCell> updateQueryWrapper = new QueryWrapper();
        updateQueryWrapper.eq("id", itemCell.getId());
        ItemCell updateItemCell = new ItemCell();
        updateItemCell.setQty(BigDecimalUtils.subtract(itemCell.getQty(), workProduceMaterialRecord.getFeedQty()));
        if (BigDecimalUtils.equal(itemCell.getQty(), workProduceMaterialRecord.getFeedQty())) {
            updateItemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.OPERATION.getValue());
        }
        itemCellService.update(updateItemCell, updateQueryWrapper);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void noCodeAdd(List<WorkProduceMaterialRecord> lstWorkProduceMaterialRecord) {

        WorkProduceTask produceTask =
                workProduceTaskService.getById(lstWorkProduceMaterialRecord.get(0).getProduceTaskId());

        this.checkProduceTaskStatus(produceTask);

        WorkOrder workOrder = workOrderService.getById(produceTask.getOrderId());

        Item item = itemService.getById(lstWorkProduceMaterialRecord.get(0).getItemId());
        boolean isBatch = ZeroOrOneEnum.ONE.getStrCode().equals(item.getBatch());

        BigDecimal currentFeedQty = BigDecimal.ZERO;
        for (WorkProduceMaterialRecord workProduceMaterialRecord : lstWorkProduceMaterialRecord) {
            if (isBatch && StringUtils.isBlank(workProduceMaterialRecord.getBatchNo())) {
                throw new ILSBootException("P-OW-0035");
            }

            if (StringUtils.isBlank(workProduceMaterialRecord.getQcStatus())) {
                throw new ILSBootException("P-OW-0036");
            }
            // 判断投料状态，
            if (!MaterialRecordFeedTypeEnum.FEED.getValue().equals(workProduceMaterialRecord.getFeedType())) {
                throw new ILSBootException("P-OW-0039");
            }

            currentFeedQty = BigDecimalUtils.add(currentFeedQty, workProduceMaterialRecord.getFeedQty());
        }
        // 投料时校验是否超过需求数量
        String itemId = lstWorkProduceMaterialRecord.get(0).getItemId();
        this.checkExceedQty(currentFeedQty, produceTask, workOrder, itemId);

        for (WorkProduceMaterialRecord workProduceMaterialRecord : lstWorkProduceMaterialRecord) {
            QueryWrapper<ItemCell> queryWrapper = new QueryWrapper();
            queryWrapper.eq("item_id", workProduceMaterialRecord.getItemId());
            queryWrapper.eq("storage_id", workProduceMaterialRecord.getFeedStorageId());
            queryWrapper.eq("qc_status", workProduceMaterialRecord.getQcStatus());
            if (isBatch) {
                queryWrapper.eq("batch", workProduceMaterialRecord.getBatchNo());
            }
            List<ItemCell> lstItemCell = itemCellService.list(queryWrapper);
            if (CommonUtil.isEmptyOrNull(lstItemCell)) {
                throw new ILSBootException("P-OW-0037");
            }
            if (lstItemCell.size() > 1) {
                throw new ILSBootException("P-OW-0038");
            }
            ItemCell itemCell = lstItemCell.get(0);
            workProduceMaterialRecord.setItemCellStateId(itemCell.getId());
            this.save(workProduceMaterialRecord);

            if (BigDecimalUtils.greaterThan(workProduceMaterialRecord.getFeedQty(), itemCell.getQty())) {
                throw new ILSBootException("P-OW-0034");
            }

            QueryWrapper<ItemCell> updateQueryWrapper = new QueryWrapper();
            updateQueryWrapper.eq("id", itemCell.getId());
            ItemCell updateItemCell = new ItemCell();
            updateItemCell.setQty(BigDecimalUtils.subtract(itemCell.getQty(), workProduceMaterialRecord.getFeedQty()));
            if (BigDecimalUtils.equal(itemCell.getQty(), workProduceMaterialRecord.getFeedQty())) {
                updateItemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.OPERATION.getValue());
            }
            itemCellService.update(updateItemCell, updateQueryWrapper);
        }


    }

    /**
     * 如果是取消的任务不能进行投料
     *
     * @param produceTask
     * @date 2020年12月25日
     */
    private void checkProduceTaskStatus(WorkProduceTask produceTask) {
        String exeStatus = produceTask.getExeStatus();
        if (PlanTaskExeStatusEnum.CANCEL.getValue().equals(exeStatus)) {
            throw new ILSBootException("P-OW-0044");
        }

    }

    /**
     * 检验投料数量是否超
     *
     * @param currentFeedQty
     * @param produceTask
     * @param workOrder
     * @date 2020年12月16日
     */
    private void checkExceedQty(BigDecimal currentFeedQty, WorkProduceTask produceTask,
                                WorkOrder workOrder, String itemId) {
        //获取已投数量，
        MaterialRecordQtyVO materialRecordQtyVO =
                baseMapper.queryMaterialRecordQty(
                        produceTask.getId(), itemId);
        BigDecimal feedTotalQty = BigDecimal.ZERO;
        if (materialRecordQtyVO != null) {
            feedTotalQty = BigDecimalUtils.subtract(materialRecordQtyVO.getFeedQty(), materialRecordQtyVO.getUndoQty());
        }
        //加上本次投入数量
        feedTotalQty = BigDecimalUtils.add(currentFeedQty, feedTotalQty);

        //如果总投入数量是负数，绝对值不能大于已投数量
        boolean isLessZero = BigDecimalUtils.lessThan(feedTotalQty, BigDecimal.ZERO);
        if (isLessZero) {
            throw new ILSBootException("P-OW-0030");
        }

        // 查看是否充许超投开关是否开启
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_TASK_EXCEED_OPERATION_SWITCH);

        // 检查物料合规性
        String flowType = workOrder.getWorkflowType();
        BigDecimal requiredTotalQty = BigDecimal.ZERO;
        // 产品BOM工艺校验
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(flowType)) {
            QueryWrapper<WorkOrderBom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", workOrder.getId());
            queryWrapper.eq("seq", produceTask.getSeq());
            queryWrapper.eq("process_id", produceTask.getProcessId());
            queryWrapper.eq("item_id", itemId);
            WorkOrderBom workOrderBom = workOrderBomService.getOne(queryWrapper);
            if (workOrderBom == null) {
                throw new ILSBootException("P-OW-0028");
            }
            if (StringUtils.isBlank(bizConfig.getConfigValue())
                    || !ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue())) {
                // 工序任务
                WorkPlanTask workPlanTask = workPlanTaskService.getById(produceTask.getPlanTaskId());
                WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
                requiredTotalQty = BigDecimalUtils.divide(
                        BigDecimalUtils.multiply(workOrderBom.getTotalQty(), produceTask.getPlanQty()),
                        workProcessTask.getPlanQty(), 6);
                if (BigDecimalUtils.greaterThan(feedTotalQty, requiredTotalQty)) {
                    throw new ILSBootException("P-OW-0031");
                }
            }
            // 物料BOM工艺校验
        } else if (WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue().equals(flowType)) {
            QueryWrapper<WorkOrderItemBom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", workOrder.getId());
            queryWrapper.eq("item_id", itemId);
            WorkOrderItemBom workOrderItemBom = workOrderItemBomService.getOne(queryWrapper);
            if (workOrderItemBom == null) {
                throw new ILSBootException("P-OW-0027");
            }
            if (StringUtils.isBlank(bizConfig.getConfigValue())
                    || !ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue())) {
                requiredTotalQty = workOrderItemBom.getTotalQty();
                if (BigDecimalUtils.greaterThan(feedTotalQty, requiredTotalQty)) {
                    throw new ILSBootException("P-OW-0031");
                }
            }
        }
    }

    @Override
    public IPage<GroupMaterialRecordVO> queryGroupRecordPageList(String produceTaskId, String controlId,
                                                                 Page<GroupMaterialRecordVO> page) {

        WorkProduceTask produceTask = workProduceTaskService.getById(produceTaskId);
        WorkOrder workOrder = workOrderService.getById(produceTask.getOrderId());
        String flowType = workOrder.getWorkflowType();
        IPage<GroupMaterialRecordVO> pageList = null;
        QueryWrapper<GroupMaterialRecordVO> queryWrapper = new QueryWrapper();
        // 产品BOM关联物料
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(flowType)) {
            queryWrapper.eq("c .order_id", workOrder.getId());
            queryWrapper.eq("c .seq", produceTask.getSeq());
            queryWrapper.eq("c .process_id", produceTask.getProcessId());
            pageList = baseMapper.queryGroupProductBomMaterialPageList(produceTaskId, page, queryWrapper, controlId);

            // 工序任务
            WorkPlanTask workPlanTask = workPlanTaskService.getById(produceTask.getPlanTaskId());
            WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());

            // bom 方式下 本次生成任务数量：工序任务数量=本次物料最大用量数量：工序上物料用量总量
            List<GroupMaterialRecordVO> lstGroupMaterialRecord = pageList.getRecords();
            if (!CommonUtil.isEmptyOrNull(lstGroupMaterialRecord)) {
                for (GroupMaterialRecordVO groupMaterialRecordVO : lstGroupMaterialRecord) {
                    BigDecimal materialTotalQty = BigDecimalUtils.divide(
                            BigDecimalUtils.multiply(groupMaterialRecordVO.getProcessTotalQty(), produceTask.getPlanQty()),
                            workProcessTask.getPlanQty(), 6);
                    groupMaterialRecordVO.setProcessTotalQty(materialTotalQty);
                }
            }
            // 物料清单物料
        } else {
            pageList = baseMapper.queryGroupMaterialRecordPageList(produceTaskId, controlId, page);
        }
        return pageList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void qrCodeUndo(WorkProduceMaterialRecord workProduceMaterialRecord) {
        // 校验撤回状态
        if (!MaterialRecordFeedTypeEnum.UNDO.getValue().equals(workProduceMaterialRecord.getFeedType())) {
            throw new ILSBootException("P-OW-0039");
        }

        if (StringUtils.isBlank(workProduceMaterialRecord.getQrcode())
                || StringUtils.isBlank(workProduceMaterialRecord.getBackQrcode())) {
            throw new ILSBootException("P-OW-0032");
        }
        // 如果相等的话就撤回到原物料上
        if (workProduceMaterialRecord.getQrcode().equals(workProduceMaterialRecord.getBackQrcode())) {
            ItemCell itemCell = itemCellService.getById(workProduceMaterialRecord.getItemCellStateId());
            boolean isQrStatus = !(ItemCellQrcodeStatusEnum.FACTORY.getValue().equals(itemCell.getQrcodeStatus())
                    || ItemCellQrcodeStatusEnum.OPERATION.getValue().equals(itemCell.getQrcodeStatus()));
            if (isQrStatus) {
                throw new ILSBootException("P-OW-0040");
            }
            if (!ItemCellPositionStatusEnum.STORAGE.getValue().equals(itemCell.getPositionStatus())) {
                throw new ILSBootException("P-OW-0023");
            }
            if (!ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(itemCell.getQcStatus())) {
                throw new ILSBootException("P-OW-0024");
            }
            if (!ItemCellBusinessStatusEnum.NONE.getValue().equals(itemCell.getBusinessStatus())) {
                throw new ILSBootException("P-OW-0025");
            }

            this.checkUndoQty(workProduceMaterialRecord);

            // 默认质检状态合格
            workProduceMaterialRecord.setQcStatus(ProduceRecordQcStatusEnum.QUALIFIED.getValue());
            this.save(workProduceMaterialRecord);

            QueryWrapper<ItemCell> updateQueryWrapper = new QueryWrapper<>();
            updateQueryWrapper.eq("id", itemCell.getId());
            ItemCell updateItemCell = new ItemCell();
            updateItemCell.setQty(BigDecimalUtils.add(itemCell.getQty(), workProduceMaterialRecord.getFeedQty()));
            updateItemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
            itemCellService.update(updateItemCell, updateQueryWrapper);
        } else {
            QueryWrapper<ItemCell> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qrcode", workProduceMaterialRecord.getBackQrcode());
            queryWrapper.eq("qrcode_status", Arrays.asList(ItemCellQrcodeStatusEnum.FACTORY.getValue(),
                    ItemCellQrcodeStatusEnum.OPERATION.getValue()));
            List<ItemCell> lstItemCell = itemCellService.list(queryWrapper);
            if (!CommonUtil.isEmptyOrNull(lstItemCell)) {
                throw new ILSBootException("P-OW-0043");
            }
            this.save(workProduceMaterialRecord);

            WorkProduceTask workProduceTask =
                    workProduceTaskMapper.selectById(workProduceMaterialRecord.getProduceTaskId());

            Item item = itemService.getById(workProduceTask.getItemId());
            String manageWay = CommonUtil.getItemManageWay(item);

            ItemCell itemCell = new ItemCell();
            itemCell.setItemId(workProduceMaterialRecord.getItemId());
            itemCell.setItemCode(workProduceMaterialRecord.getItemCode());
            itemCell.setItemName(workProduceMaterialRecord.getItemName());
            itemCell.setSpec(item.getSpec());
            itemCell.setQty(workProduceMaterialRecord.getFeedQty());
            itemCell.setUnitId(workProduceMaterialRecord.getUnit());
            if (StringUtils.isNotBlank(workProduceMaterialRecord.getUnit())) {
                Unit unit = unitService.getById(workProduceMaterialRecord.getUnit());
                itemCell.setUnitName(unit.getUnitName());
            }
            itemCell.setQcStatus(workProduceMaterialRecord.getQcStatus());
            itemCell.setManageWay(manageWay);
            itemCell.setNote(workProduceMaterialRecord.getNote());
            itemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            this.fillItemCell(itemCell, workProduceTask.getStationId());
            itemCellService.save(itemCell);
        }
    }

    /**
     * 填充物料单位的位置信息
     *
     * @param storageVO
     * @param itemCell
     * @date 2020年12月14日
     */
    private void fillItemCell(ItemCell itemCell, String stationId) {
        ItemCellFinishedStorageVO storageVO = wareHouseService.findFinishedStorageByStationId(stationId);
        if (storageVO == null || StringUtils.isBlank(storageVO.getWareHouseId())) {
            throw new ILSBootException("P-OW-0021");
        }

        itemCell.setStorageId(storageVO.getSecondStorageId());
        itemCell.setStorageCode(storageVO.getSecondStorageCode());
        itemCell.setStorageName(storageVO.getSecondStorageName());
        itemCell.setAreaCode(storageVO.getFirstStorageCode());
        itemCell.setAreaName(storageVO.getFirstStorageName());
        itemCell.setHouseCode(storageVO.getWareHouseCode());
        itemCell.setHouseName(storageVO.getWareHouseName());
    }


    /**
     * 撤回投料数量校验
     *
     * @param workProduceMaterialRecord
     * @date 2020年12月17日
     */
    private void checkUndoQty(WorkProduceMaterialRecord workProduceMaterialRecord) {
        // 获取已投数量，
        MaterialRecordQtyVO materialRecordQtyVO =
                baseMapper.queryMaterialRecordQtyByQrcode(workProduceMaterialRecord.getProduceTaskId(),
                        workProduceMaterialRecord.getItemId(), workProduceMaterialRecord.getQrcode());
        BigDecimal feedTotalQty = BigDecimal.ZERO;
        if (materialRecordQtyVO != null) {
            feedTotalQty =
                    BigDecimalUtils.subtract(materialRecordQtyVO.getFeedQty(), materialRecordQtyVO.getUndoQty());
        }
        //撤回数量要小于已投数量
        boolean isGreater = BigDecimalUtils.greaterThan(workProduceMaterialRecord.getFeedQty(), feedTotalQty);
        if (isGreater) {
            throw new ILSBootException("P-OW-0041");
        }
        //撤回数量大于零
        boolean isLesszero = BigDecimalUtils.lessThan(workProduceMaterialRecord.getFeedQty(), BigDecimal.ZERO);
        if (isLesszero) {
            throw new ILSBootException("P-OW-0042");
        }
    }

    @Override
    public Page<MaterialRecordReportVO> getMaterialRecord(Page<MaterialRecordReportVO> page,
                                                          MaterialRecordReportVO materialRecordReportVO) {
        StringBuilder addColumns = new StringBuilder();
        QueryWrapper<MaterialRecordReportVO> queryWrapper = new QueryWrapper<>();
        String groupBy = materialRecordReportVO.getGroupBy();
        if (StringUtils.isNotBlank(groupBy)) {
            if (StringUtils.isNotBlank(groupBy) && groupBy.contains(ReportGroupColumnEnum.GROUP_PROCESS.getValue())) {
                queryWrapper.groupBy("b.process_id,b.process_name,b.process_code");
                addColumns.append(",b.process_id,b.process_name,b.process_code");
            }
            if (StringUtils.isNotBlank(groupBy) && groupBy.contains(ReportGroupColumnEnum.GROUP_STATION.getValue())) {
                queryWrapper.groupBy("b.station_id,b.station_name,b.station_code");
                addColumns.append(",b.station_id,b.station_name,b.station_code");
            }
            if (StringUtils.isNotBlank(groupBy) && groupBy.contains(ReportGroupColumnEnum.GROUP_ORDER.getValue())) {
                queryWrapper.groupBy("b.order_no");
                addColumns.append(",b.order_no");
            }
        }
        queryWrapper.groupBy("a.item_name,a.item_code,a.unit");
        Date startTime = materialRecordReportVO.getStartTime();
        Date endTime = materialRecordReportVO.getEndTime();
        Date now = new Date();
        if (null == startTime) {
            startTime = now;
        }
        if (null == endTime) {
            endTime = now;
        }
        queryWrapper.between("a.create_time", DateUtil.beginOfDay(startTime), DateUtil.endOfDay(endTime));
        String addString = addColumns.toString();
        addString=(" a.item_name,a.item_code,c.unit_name,(sum(case a.feed_type when '1' then ifnull(a.feed_qty,0) else 0 end )-sum(case a.feed_type when '2' then ifnull(a.feed_qty,0) else 0 end )) AS qty" + addString);
        Page<MaterialRecordReportVO> materialRecord = baseMapper.getMaterialRecord(addString, page, queryWrapper);
        return materialRecord;
    }
}
