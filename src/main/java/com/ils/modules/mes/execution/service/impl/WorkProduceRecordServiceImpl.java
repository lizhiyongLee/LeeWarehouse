package com.ils.modules.mes.execution.service.impl;

import java.math.BigDecimal;
import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.material.entity.ItemStock;
import com.ils.modules.mes.base.material.service.ItemStockService;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.service.QcMethodService;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.vo.WorkProduceRecordReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.mapper.WorkProduceRecordMapper;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskMapper;
import com.ils.modules.mes.execution.service.WorkProduceMaterialRecordService;
import com.ils.modules.mes.execution.service.WorkProduceRecordService;
import com.ils.modules.mes.execution.vo.ProcessRecordVO;
import com.ils.modules.mes.execution.vo.QrItemCellInfo;
import com.ils.modules.mes.execution.vo.WorkProduceRecordInfoVO;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.service.WorkOrderBomService;
import com.ils.modules.mes.produce.service.WorkOrderItemBomService;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.produce.service.WorkPlanTaskService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: ????????????
 * @Author: fengyi
 */
@Service
public class WorkProduceRecordServiceImpl extends ServiceImpl<WorkProduceRecordMapper, WorkProduceRecord> implements WorkProduceRecordService {
    /**
     * ????????????????????????1.??????
     */
    public static final String POLICY_TIME_UNIT_ONE = "1";
    /**
     * ????????????????????????2.??????
     */
    public static final String POLICY_TIME_UNIT_TWO = "2";
    /**
     * ????????????????????????3.???
     */
    public static final String POLICY_TIME_UNIT_THREE = "3";
    /**
     * ????????????????????????4.???
     */
    public static final String POLICY_TIME_UNIT_FOUR = "4";
    /**
     * ????????????????????????5.???
     */
    public static final String POLICY_TIME_UNIT_FIVE = "5";

    /**
     * ????????????????????????
     */
    public static final String REPORT_MONTH = "month";
    /**
     * ????????????????????????
     */
    public static final String REPORT_WEEK = "week";
    /**
     * ????????????????????????
     */
    public static final String REPORT_DAY = "day";

    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;

    @Autowired
    private ItemService itemService;
    @Autowired
    private UnitService unitService;

    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private ItemStockService itemStockService;
    @Autowired
    @Lazy
    private WorkPlanTaskService workPlanTaskService;
    @Autowired
    private WorkOrderBomService workOrderBomService;
    @Autowired
    private WorkOrderItemBomService workOrderItemBomService;
    @Autowired
    private WorkProduceMaterialRecordService workProduceMaterialRecordService;

    @Autowired
    private WorkProcessTaskService workProcessTaskService;

    @Autowired
    private ItemCellService itemCellService;

    @Autowired
    private WareHouseService wareHouseService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void unQualifiedAdd(WorkProduceRecord workProduceRecord) {
        // ???????????????????????????
        if (!ProduceRecordQcStatusEnum.UNQUALIFIED.getValue().equals(workProduceRecord.getQcStatus())) {
            throw new ILSBootException("P-OW-0020");
        }

        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(workProduceRecord.getProduceTaskId());
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());

        String itemCellStateId = null;
        Item item = itemService.getById(workProduceTask.getItemId());
        if (MesCommonConstant.ROUTE_PROCESS_END.equals(workProcessTask.getNextCode())) {
            itemCellStateId = this.saveNotCodeItemCell(workProduceRecord, workProduceTask, item, false);
            WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
            workProduceRecord.setUnit(workOrder.getUnit());
            workProduceRecord.setUnitName(unitService.getById(workOrder.getUnit()).getUnitName());
        }
        workProduceRecord.setItemCellStateId(itemCellStateId);
        this.fillWorkProduceRecord(workProduceRecord, workProduceTask);
        this.save(workProduceRecord);

        // ?????????????????????????????????
        QueryWrapper<WorkProduceTask> produceTaskqueryWrapper = new QueryWrapper<>();
        WorkProduceTask updateWorkProduceTask = new WorkProduceTask();
        produceTaskqueryWrapper.eq("id", workProduceTask.getId());
        updateWorkProduceTask
                .setBadQty(BigDecimalUtils.add(workProduceTask.getBadQty(), workProduceRecord.getSubmitQty()));
        workProduceTaskMapper.update(updateWorkProduceTask, produceTaskqueryWrapper);

    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void jointProductNoCodeAdd(WorkProduceRecord workProduceRecord) {
        // ??????????????????????????????????????????
        String produceTaskId = workProduceRecord.getProduceTaskId();
        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        Item item = itemService.getById(workProduceRecord.getItemId());
        // ????????????
        if (ZeroOrOneEnum.ONE.getStrCode().equals(item.getQrcode())) {
            throw new ILSBootException("P-OW-0046");
        }
        //??????????????????
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
        //????????????????????????????????????????????????
        if (ZeroOrOneEnum.ONE.getStrCode().equals(item.getBatch())
                && StringUtils.isBlank(workProduceRecord.getBatchNo())) {
            throw new ILSBootException("P-OW-0019");
        }
        // ??????????????????
        this.checkProduceTaskStatus(workProduceTask);

        // ????????????????????????
        this.saveNotCodeWorkProduceRecord(workProduceRecord, workProduceTask, workProcessTask, item, true);

    }

    @Override
    public ItemCell checkJointProductQrcode(String qrcode, String produceTaskId) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("qrcode", qrcode);
        List<ItemCell> list = itemCellService.list(itemCellQueryWrapper);
        //????????????
        if (CommonUtil.isEmptyOrNull(list)) {
            return null;
        }
        if (list.size() > 1) {
            throw new ILSBootException("??????????????????");
        }
        ItemCell itemCell = list.get(0);
        if (!ItemCellQrcodeStatusEnum.FACTORY.getValue().equals(itemCell.getQrcodeStatus())) {
            throw new ILSBootException("???????????????????????????");
        }
        //????????????
        if (ItemQcStatusEnum.UNQUALIFIED.getValue().equals(itemCell.getQcStatus())) {
            throw new ILSBootException("???????????????????????????????????????");
        }
        QueryWrapper<WorkProduceRecord> workProduceRecordQueryWrapper = new QueryWrapper<>();
        workProduceRecordQueryWrapper.eq("item_cell_state_id", itemCell.getId());
        workProduceRecordQueryWrapper.eq("produce_task_id", produceTaskId);
        workProduceRecordQueryWrapper.eq("product_type", RecordProductTypeEnum.JOINT_PRODUCT.getValue());
        List<WorkProduceRecord> workProduceRecordList = baseMapper.selectList(workProduceRecordQueryWrapper);
        if (CommonUtil.isEmptyOrNull(workProduceRecordList)) {
            throw new ILSBootException("?????????????????????????????????????????????????????????????????????");
        } else {
            return itemCell;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void jointProductQrcodeAdd(WorkProduceRecord workProduceRecord) {
        String produceTaskId = workProduceRecord.getProduceTaskId();

        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
        Item item = itemService.getById(workProduceRecord.getItemId());

        // ??????????????????
        this.checkProduceTaskStatus(workProduceTask);

        // ????????????
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(item.getQrcode())) {
            throw new ILSBootException("P-OW-0046");
        }
        //??????????????????
        QrItemCellInfo qrItemCellInfo = new QrItemCellInfo();
        ItemCell itemCell = this.checkJointProductQrcode(workProduceRecord.getQrcode(), workProduceTask.getId());
        if (null != itemCell) {
            qrItemCellInfo.setExistItemCell(ZeroOrOneEnum.ONE.getStrCode());
            qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_EDIT);
            qrItemCellInfo.setItemCell(itemCell);
        } else {
            qrItemCellInfo.setExistItemCell(ZeroOrOneEnum.ZERO.getStrCode());
            qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_ADD);
            itemCell = new ItemCell();
            itemCell.setItemCode(item.getItemCode());
            itemCell.setItemId(item.getId());
            itemCell.setItemName(item.getItemName());
            itemCell.setSpec(item.getSpec());
            itemCell.setUnitId(item.getMainUnit());
            itemCell.setUnitName(unitService.getById(item.getMainUnit()).getUnitName());
        }
        qrItemCellInfo.setItemCell(itemCell);

        // ??????????????????
        this.saveQrcodeQualifiedWorkProduceRecord(workProduceRecord, workProduceTask, workProcessTask, qrItemCellInfo, true);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void qualifiedAdd(WorkProduceRecord workProduceRecord) {
        // ??????????????????????????????????????????
        if (!ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(workProduceRecord.getQcStatus())) {
            throw new ILSBootException("P-OW-0020");
        }

        String produceTaskId = workProduceRecord.getProduceTaskId();
        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        Item item = itemService.getById(workProduceTask.getItemId());
        // ????????????
        if (ZeroOrOneEnum.ONE.getStrCode().equals(item.getQrcode())) {

            throw new ILSBootException("P-OW-0046");
            // ?????????????????????
        } else {
            this.saveNotExistCode(workProduceRecord, workProduceTask, item);
        }
    }


    /**
     * ????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     * @date 2020???12???10???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void saveNotExistCode(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask, Item item) {

        // ??????????????????????????????????????????
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
        if (MesCommonConstant.ROUTE_PROCESS_END.equals(workProcessTask.getNextCode())) {
            if (ZeroOrOneEnum.ONE.getStrCode().equals(item.getBatch())
                    && StringUtils.isBlank(workProduceRecord.getBatchNo())) {
                throw new ILSBootException("P-OW-0019");
            }
        }

        // ??????????????????
        this.checkProduceTaskStatus(workProduceTask);

        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        // ??????????????????
        String workFlowType = workOrder.getWorkflowType();

        // ????????????????????????????????????
        this.checkItemAll(workProduceTask, workFlowType);

        // ??????????????????????????????
        this.checkExceedTaskPlan(workProduceRecord, workProduceTask);

        //??????????????????????????????
        this.checkPreProcessQty(workProduceRecord, workProduceTask, workProcessTask);

        // ????????????????????????
        this.saveNotCodeWorkProduceRecord(workProduceRecord, workProduceTask, workProcessTask, item, false);

    }

    /**
     * ????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     * @param workProcessTask
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void saveNotCodeWorkProduceRecord(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                                              WorkProcessTask workProcessTask, Item item, boolean isJointProduct) {
        String itemCellStateId = null;
        //??????????????????
        if (isJointProduct || MesCommonConstant.ROUTE_PROCESS_END.equals(workProcessTask.getNextCode())) {
            itemCellStateId = this.saveNotCodeItemCell(workProduceRecord, workProduceTask, item, true);
            if (isJointProduct) {
                workProduceRecord.setUnitName(unitService.getById(item.getMainUnit()).getUnitName());
                workProduceRecord.setUnit(item.getMainUnit());
                workProduceRecord.setProductType(RecordProductTypeEnum.JOINT_PRODUCT.getValue());
            } else {
                WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
                workProduceRecord.setUnit(workOrder.getUnit());
                workProduceRecord.setUnitName(unitService.getById(workOrder.getUnit()).getUnitName());
            }
        }
        workProduceRecord.setItemCellStateId(itemCellStateId);
        //????????????
        this.fillWorkProduceRecord(workProduceRecord, workProduceTask);
        //??????????????????????????????
        String qcStatus = this.queryIfHasQcMethodRelateWorkProduce(workProduceTask.getId());
        workProduceRecord.setQcStatus(qcStatus);
        this.save(workProduceRecord);
        //???????????????????????????
        if (!isJointProduct) {
            this.saveCalQty(workProduceRecord, workProduceTask, workProcessTask);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    private void saveCalQty(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                            WorkProcessTask workProcessTask) {
        // ?????????????????????????????????
        QueryWrapper<WorkProduceTask> produceTaskqueryWrapper = new QueryWrapper<>();
        WorkProduceTask updateWorkProduceTask = new WorkProduceTask();
        produceTaskqueryWrapper.eq("id", workProduceTask.getId());
        updateWorkProduceTask
                .setGoodQty(BigDecimalUtils.add(workProduceTask.getGoodQty(), workProduceRecord.getSubmitQty()));
        workProduceTaskMapper.update(updateWorkProduceTask, produceTaskqueryWrapper);
        // ?????????????????????????????????
        QueryWrapper<WorkProcessTask> queryWrapper = new QueryWrapper<>();
        WorkProcessTask updateWorkProcessTask = new WorkProcessTask();
        queryWrapper.eq("id", workProcessTask.getId());
        updateWorkProcessTask
                .setCompletedQty(BigDecimalUtils.add(workProcessTask.getCompletedQty(), workProduceRecord.getSubmitQty()));
        workProcessTaskService.update(updateWorkProcessTask, queryWrapper);

        if (MesCommonConstant.ROUTE_PROCESS_END.equals(workProcessTask.getNextCode())) {
            WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
            QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
            WorkOrder updateWorkOrder = new WorkOrder();
            workOrderQueryWrapper.eq("id", workOrder.getId());
            updateWorkOrder
                    .setCompletedQty(BigDecimalUtils.add(workOrder.getCompletedQty(), workProduceRecord.getSubmitQty()));
            workOrderService.update(updateWorkOrder, workOrderQueryWrapper);
        }

    }

    /**
     * ?????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     */
    private void fillWorkProduceRecord(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask) {
        workProduceRecord.setOrderId(workProduceTask.getOrderId());
        workProduceRecord.setOrderNo(workProduceTask.getOrderNo());
        if (StringUtils.isBlank(workProduceRecord.getReportType())) {
            workProduceRecord.setReportType(ProduceReportTypeEnum.MAN_REPORT.getValue());
        }
        workProduceRecord.setProcessId(workProduceTask.getProcessId());
        workProduceRecord.setProcessCode(workProduceTask.getProcessCode());
        workProduceRecord.setProcessName(workProduceTask.getProcessName());
        //workProduceRecord???????????????????????????????????????????????????????????????????????????
        if (StringUtils.isBlank(workProduceRecord.getItemId())) {
            workProduceRecord.setItemId(workProduceTask.getItemId());
            workProduceRecord.setItemCode(workProduceTask.getItemCode());
            workProduceRecord.setItemName(workProduceTask.getItemName());
        } else {
            Item byId = itemService.getById(workProduceRecord.getItemId());
            workProduceRecord.setItemCode(byId.getItemCode());
            workProduceRecord.setItemName(byId.getItemName());
        }
        workProduceRecord.setProduceDate(new Date());
        LoginUser loginUser = CommonUtil.getLoginUser();
        workProduceRecord.setEmployeeId(loginUser.getId());
        workProduceRecord.setEmployeeName(loginUser.getRealname());
        workProduceRecord.setTeamId(workProduceTask.getTeamId());
        workProduceRecord.setStationId(workProduceTask.getStationId());
        workProduceRecord.setStationCode(workProduceTask.getStationCode());
        workProduceRecord.setStationName(workProduceTask.getStationName());
    }

    /**
     * ???????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     * @date 2020???12???11???
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private String saveNotCodeItemCell(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                                       Item item, boolean checkQcMethod) {
        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        String stationId = workProduceTask.getStationId();

        //????????????????????????????????????????????????????????????
        String qcStatus;
        if (checkQcMethod) {
            qcStatus = this.queryIfHasQcMethodRelateWorkProduce(workProduceTask.getId());
        } else {
            qcStatus = workProduceRecord.getQcStatus();
        }
        // ??????????????????????????????
        ItemCellFinishedStorageVO storageVO = this.checkWareHouseByStationId(stationId);

        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper();
        itemCellQueryWrapper.eq("item_id", item.getId());
        String manageWay = CommonUtil.getItemManageWay(item);
        if (ItemManageWayEnum.BATCH_MANAGE.getValue().equals(manageWay)) {
            itemCellQueryWrapper.eq("batch", workProduceRecord.getBatchNo());
        }
        itemCellQueryWrapper.eq("qc_status", qcStatus);
        itemCellQueryWrapper.eq("storage_id", storageVO.getSecondStorageId());
        List<ItemCell> lstItemCell = itemCellService.list(itemCellQueryWrapper);
        ItemCell itemCell = null;
        //???????????????
        List<ItemStock> itemStocks = itemStockService.selectByMainId(item.getId());
        ItemStock itemStock = itemStocks.get(0);
        if (CommonUtil.isEmptyOrNull(lstItemCell)) {
            itemCell = new ItemCell();
            if (StringUtils.isBlank(workProduceRecord.getItemId())) {
                itemCell.setItemId(workProduceTask.getItemId());
                itemCell.setItemCode(workProduceTask.getItemCode());
                itemCell.setItemName(workProduceTask.getItemName());
            } else {
                itemCell.setItemId(item.getId());
                itemCell.setItemCode(item.getItemCode());
                itemCell.setItemName(item.getItemName());
            }
            itemCell.setProduceDate(new Date());
            if (null != itemStock.getValidTime()) {
                itemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
            } else {
                itemCell.setValidDate(new Date());
            }
            itemCell.setOrderId(workOrder.getId());
            itemCell.setOrderNo(workOrder.getOrderNo());
            itemCell.setSpec(item.getSpec());
            itemCell.setBatch(workProduceRecord.getBatchNo());
            itemCell.setQty(workProduceRecord.getSubmitQty());
            itemCell.setUnitId(item.getMainUnit());
            Unit unit = unitService.getById(item.getMainUnit());
            itemCell.setUnitName(unit.getUnitName());
            itemCell.setQcStatus(qcStatus);
            itemCell.setManageWay(manageWay);
            itemCell.setNote(workProduceRecord.getNote());
            itemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            itemCell.setBusinessStatus(ItemCellBusinessStatusEnum.NONE.getValue());
            this.fillItemCell(storageVO, itemCell);
            itemCellService.save(itemCell);
        } else {
            itemCell = lstItemCell.get(0);
            // ?????????????????????????????????
            QueryWrapper<ItemCell> updateItemCellQueryWrapper = new QueryWrapper<>();
            ItemCell updateItemCell = new ItemCell();
            updateItemCellQueryWrapper.eq("id", itemCell.getId());
            updateItemCell.setQty(BigDecimalUtils.add(itemCell.getQty(), workProduceRecord.getSubmitQty()));
            itemCellService.update(updateItemCell, updateItemCellQueryWrapper);
        }

        return itemCell.getId();
    }

    /**
     * ??????????????????????????????
     *
     * @param stationId
     * @date 2020???12???14???
     */
    private ItemCellFinishedStorageVO checkWareHouseByStationId(String stationId) {
        ItemCellFinishedStorageVO storageVO = wareHouseService.findFinishedStorageByStationId(stationId);
        if (storageVO == null || StringUtils.isBlank(storageVO.getWareHouseId())) {
            throw new ILSBootException("P-OW-0021");
        }

        return storageVO;
    }

    /**
     * ?????????????????????????????????
     *
     * @param storageVO
     * @param itemCell
     * @date 2020???12???14???
     */
    private void fillItemCell(ItemCellFinishedStorageVO storageVO, ItemCell itemCell) {
        if (storageVO != null) {
            itemCell.setStorageId(storageVO.getSecondStorageId());
            itemCell.setStorageCode(storageVO.getSecondStorageCode());
            itemCell.setStorageName(storageVO.getSecondStorageName());
            itemCell.setAreaCode(storageVO.getFirstStorageCode());
            itemCell.setAreaName(storageVO.getFirstStorageName());
            itemCell.setHouseCode(storageVO.getWareHouseCode());
            itemCell.setHouseName(storageVO.getWareHouseName());
        }
    }

    /**
     * ??????????????????
     *
     * @param workProduceTask
     */
    private void checkProduceTaskStatus(WorkProduceTask workProduceTask) {

        if (!PlanTaskExeStatusEnum.PRODUCE.getValue().equals(workProduceTask.getExeStatus())) {
            throw new ILSBootException("P-OW-0018");
        }

    }

    /**
     * ??????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     * @param workFlowType
     */
    private void checkItemAll(WorkProduceTask workProduceTask, String workFlowType) {
        // ??????BOM??????????????????????????????
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workFlowType)) {
            QueryWrapper<WorkOrderBom> workOrderBomQueryWrapper = new QueryWrapper();
            workOrderBomQueryWrapper.eq("seq", workProduceTask.getSeq());
            workOrderBomQueryWrapper.eq("order_id", workProduceTask.getOrderId());
            workOrderBomQueryWrapper.eq("process_id", workProduceTask.getProcessId());
            List<WorkOrderBom> lstWorkOrderBom = workOrderBomService.list(workOrderBomQueryWrapper);
            if (CommonUtil.isEmptyOrNull(lstWorkOrderBom)) {
                return;
            }
            QueryWrapper<WorkProduceMaterialRecord> materialRecordQueryWrapper = new QueryWrapper();
            materialRecordQueryWrapper.eq("produce_task_id", workProduceTask.getId());
            List<WorkProduceMaterialRecord> lstMaterialRecord =
                    workProduceMaterialRecordService.list(materialRecordQueryWrapper);
            if (CommonUtil.isEmptyOrNull(lstMaterialRecord)) {
                throw new ILSBootException("P-OW-0015");
            }
            Set<String> itemIdSet = new HashSet<String>();
            for (WorkProduceMaterialRecord item : lstMaterialRecord) {
                itemIdSet.add(item.getItemId());
            }
            for (WorkOrderBom itemBom : lstWorkOrderBom) {
                if (!itemIdSet.contains(itemBom.getItemId())) {
                    throw new ILSBootException("P-OW-0015");
                }
            }

        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     */
    private void checkExceedTaskPlan(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask) {
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_TASK_EXCEED_PLAN_SWITCH);
        if (ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue())) {
            QueryWrapper<WorkProduceRecord> workProduceRecordQueryWrapper = new QueryWrapper();
            workProduceRecordQueryWrapper.eq("produce_task_id", workProduceRecord.getProduceTaskId());
            workProduceRecordQueryWrapper.eq("qc_status", ProduceRecordQcStatusEnum.QUALIFIED.getValue());
            List<WorkProduceRecord> lstWorkProduceRecord = this.list(workProduceRecordQueryWrapper);
            BigDecimal produceTotal = BigDecimal.ZERO;
            for (WorkProduceRecord entity : lstWorkProduceRecord) {
                produceTotal = BigDecimalUtils.add(produceTotal, entity.getSubmitQty());
            }
            produceTotal = BigDecimalUtils.add(produceTotal, workProduceRecord.getSubmitQty());

            if (BigDecimalUtils.greaterThan(produceTotal, workProduceTask.getPlanQty())) {
                throw new ILSBootException("P-OW-0016");
            }
        }

    }

    /**
     * ???????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     */
    private void checkPreProcessQty(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                                    WorkProcessTask workProcessTask) {
        String priorCode = workProcessTask.getPriorCode();
        // ?????????????????????
        if (MesCommonConstant.ROUTE_PROCESS_FIRST.equals(priorCode)) {
            return;
        }
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_TASK_ADJUST_PREPROCESS_SWITCH);
        if (ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue())) {

            WorkProcessTask preWorkProcessTask = workProcessTaskService.getById(priorCode);
            BigDecimal preCompletedQty = preWorkProcessTask.getCompletedQty();
            BigDecimal preTotalQty = preWorkProcessTask.getPlanQty();
            BigDecimal totalQty = workProcessTask.getPlanQty();
            BigDecimal completeQty =
                    BigDecimalUtils.add(workProcessTask.getCompletedQty(), workProduceRecord.getSubmitQty());
            // completeQty/totalQty>preCompletedQty/preTotalQty==>completeQty*preTotalQty>preCompletedQty*totalQty
            Boolean isGreat = BigDecimalUtils.greaterThan(BigDecimalUtils.multiply(completeQty, preTotalQty),
                    BigDecimalUtils.multiply(preCompletedQty, totalQty));

            if (isGreat) {
                throw new ILSBootException("P-OW-0017");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkProduceRecord(WorkProduceRecord workProduceRecord) {
        baseMapper.updateById(workProduceRecord);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkProduceRecord(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkProduceRecord(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public void checkPreProduceRecord(String produceTaskId) {
        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        // ??????????????????
        String workFlowType = workOrder.getWorkflowType();
        // ??????????????????
        this.checkItemAll(workProduceTask, workFlowType);

        // ??????????????????????????????
        this.checkWareHouseByStationId(workProduceTask.getStationId());

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void qualifiedQrcodeAdd(WorkProduceRecordInfoVO workProduceRecordInfoVO) {
        if (!ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(workProduceRecordInfoVO.getQcStatus())) {
            throw new ILSBootException("P-OW-0024");
        }

        String produceTaskId = workProduceRecordInfoVO.getProduceTaskId();

        String qrcode = workProduceRecordInfoVO.getQrcode();

        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
        QrItemCellInfo qrItemCellInfo = this.checkQrcodeQualifiedItemCell(workProduceTask, workProcessTask, qrcode);
        // ??????????????????
        boolean existItemCell = ZeroOrOneEnum.ONE.getStrCode().equals(qrItemCellInfo.getExistItemCell());
        // ?????????????????????????????????0
        BigDecimal itemCellQty = BigDecimal.ZERO;
        if (existItemCell) {
            itemCellQty = qrItemCellInfo.getItemCell().getQty();
        }
        BigDecimal totalSubmitQty = BigDecimalUtils.add(workProduceRecordInfoVO.getSubmitQty(), itemCellQty);
        if (BigDecimalUtils.lessThan(totalSubmitQty, BigDecimal.ZERO)) {
            throw new ILSBootException("P-OW-0052");
        }
        // ??????????????????????????????????????????
        this.checkQrcodePreProcessQty(workProduceRecordInfoVO, workProduceTask, workProcessTask);

        // ????????????????????????
        this.checkExceedTaskPlan(workProduceRecordInfoVO, workProduceTask);

        Item item = itemService.getById(workProduceRecordInfoVO.getItemId());
        ItemCell itemCell = qrItemCellInfo.getItemCell();
        itemCell.setItemId(workProduceTask.getItemId());
        itemCell.setItemCode(workProduceTask.getItemCode());
        itemCell.setItemName(workProduceTask.getItemName());
        itemCell.setSpec(item.getSpec());
        itemCell.setUnitId(item.getMainUnit());
        itemCell.setUnitName(unitService.getById(item.getMainUnit()).getUnitName());
        // ??????????????????
        this.saveQrcodeQualifiedWorkProduceRecord(workProduceRecordInfoVO, workProduceTask, workProcessTask, qrItemCellInfo, false);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void unQualifiedQrcodeAdd(WorkProduceRecordInfoVO workProduceRecordInfoVO) {
        if (!ProduceRecordQcStatusEnum.UNQUALIFIED.getValue().equals(workProduceRecordInfoVO.getQcStatus())) {
            throw new ILSBootException(" P-OW-0053");
        }

        String produceTaskId = workProduceRecordInfoVO.getProduceTaskId();

        String qrcode = workProduceRecordInfoVO.getQrcode();

        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
        QrItemCellInfo qrItemCellInfo = this.checkQrcodeUnqualifiedItemCell(workProduceTask, workProcessTask, qrcode);
        // ??????????????????
        boolean existItemCell = ZeroOrOneEnum.ONE.getStrCode().equals(qrItemCellInfo.getExistItemCell());
        // ?????????????????????????????????0
        BigDecimal itemCellQty = BigDecimal.ZERO;
        if (existItemCell) {
            itemCellQty = qrItemCellInfo.getItemCell().getQty();
        }
        BigDecimal totalSubmitQty = BigDecimalUtils.add(workProduceRecordInfoVO.getSubmitQty(), itemCellQty);
        if (BigDecimalUtils.lessThan(totalSubmitQty, BigDecimal.ZERO)) {
            throw new ILSBootException("P-OW-0052");
        }

        // ??????????????????
        this.saveQrcodeUnQualifiedWorkProduceRecord(workProduceRecordInfoVO, workProduceTask, workProcessTask,
                qrItemCellInfo);

    }

    @Override
    public QrItemCellInfo queryQrcodeItemCell(String produceTaskId, String qrcode, String qcStatus) {
        if (!ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(qcStatus)
                && !ProduceRecordQcStatusEnum.UNQUALIFIED.getValue().equals(qcStatus)) {
            throw new ILSBootException("P-OW-0064");
        }
        WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(produceTaskId);
        WorkPlanTask workPlanTask = workPlanTaskService.getById(workProduceTask.getPlanTaskId());
        WorkProcessTask workProcessTask = workProcessTaskService.getById(workPlanTask.getProcessTaskId());
        QrItemCellInfo qrItemCellInfo = null;
        if (ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(qcStatus)) {
            qrItemCellInfo = this.checkQrcodeQualifiedItemCell(workProduceTask, workProcessTask, qrcode);
        } else {
            qrItemCellInfo = this.checkQrcodeUnqualifiedItemCell(workProduceTask, workProcessTask, qrcode);
        }
        return qrItemCellInfo;
    }

    /**
     * ????????? ?????????
     *
     * @param workProduceTask
     * @param workProcessTask
     * @param qrcode
     * @return
     * @date 2021???1???20???
     */
    private QrItemCellInfo checkQrcodeUnqualifiedItemCell(WorkProduceTask workProduceTask,
                                                          WorkProcessTask workProcessTask, String qrcode) {
        QrItemCellInfo qrItemCellInfo = new QrItemCellInfo();
        // ??????????????????
        this.checkProduceTaskStatus(workProduceTask);
        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        // ??????????????????
        String workFlowType = workOrder.getWorkflowType();
        // ??????????????????
        this.checkItemAll(workProduceTask, workFlowType);
        // ??????????????????
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("qrcode", qrcode);
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        List<ItemCell> lstItemCell = itemCellService.list(itemCellQueryWrapper);
        // ???????????????
        if (CommonUtil.isEmptyOrNull(lstItemCell)) {
            String priorCode = workProcessTask.getPriorCode();
            BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_RECORD_NOTFIRST_PREPROCESS_SWITCH);
            if (!MesCommonConstant.ROUTE_PROCESS_FIRST.equals(priorCode)) {
                if (StringUtils.isBlank(bizConfig.getConfigValue())
                        || ZeroOrOneEnum.ZERO.getStrCode().equals(bizConfig.getConfigValue())) {
                    throw new ILSBootException("P-OW-0047");
                }
            }
            qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_ADD);
            qrItemCellInfo.setExistItemCell(ZeroOrOneEnum.ZERO.getStrCode());
        } else {
            if (lstItemCell.size() > 1) {
                throw new ILSBootException("P-OW-0029");
            }
            ItemCell itemCell = lstItemCell.get(0);

            // ??????????????????????????????????????????????????????
            if (!workProduceTask.getItemId().equals(itemCell.getItemId())) {
                throw new ILSBootException("P-OW-0049");
            }
            // ????????????????????????????????????????????????
            if (!workProduceTask.getOrderId().equals(itemCell.getOrderId())) {
                throw new ILSBootException("P-OW-0050");
            }
            // ????????????????????????????????????????????????
            String currentProcessCode = workProcessTask.getId();
            if (!currentProcessCode.equals(itemCell.getFromProcess())
                    && !currentProcessCode.equals(itemCell.getNowProcess())) {
                String processName = "";
                if (StringUtils.isNoneBlank(itemCell.getNowProcess())) {
                    WorkProcessTask itemCellProcessTask = workProcessTaskService.getById(itemCell.getNowProcess());
                    processName = itemCellProcessTask.getProcessName();
                }
                throw new ILSBootException("P-OW-0051", processName);
            }

            // ???????????????????????????????????????
            if (currentProcessCode.equals(itemCell.getNowProcess())) {
                if (ProduceRecordQcStatusEnum.UNQUALIFIED.getValue().equals(itemCell.getQcStatus())) {
                    throw new ILSBootException("P-OW-0048");
                } else {
                    qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_ADD);
                }
            } else {
                if (ProduceRecordQcStatusEnum.UNQUALIFIED.getValue().equals(itemCell.getQcStatus())) {
                    qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_EDIT);
                } else {
                    qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_FLAG);
                }
            }
            qrItemCellInfo.setExistItemCell(ZeroOrOneEnum.ONE.getStrCode());
            qrItemCellInfo.setItemCell(itemCell);
        }
        return qrItemCellInfo;
    }

    /**
     * ????????? ??????
     *
     * @param workProduceTask
     * @param workProcessTask
     * @param qrcode
     * @return
     * @date 2021???1???20???
     */
    private QrItemCellInfo checkQrcodeQualifiedItemCell(WorkProduceTask workProduceTask,
                                                        WorkProcessTask workProcessTask, String qrcode) {
        QrItemCellInfo qrItemCellInfo = new QrItemCellInfo();
        // ??????????????????
        this.checkProduceTaskStatus(workProduceTask);
        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        // ??????????????????
        String workFlowType = workOrder.getWorkflowType();
        // ??????????????????
        this.checkItemAll(workProduceTask, workFlowType);
        // ??????????????????
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("qrcode", qrcode);
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        List<ItemCell> lstItemCell = itemCellService.list(itemCellQueryWrapper);
        // ???????????????
        if (CommonUtil.isEmptyOrNull(lstItemCell)) {
            String priorCode = workProcessTask.getPriorCode();
            BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_RECORD_NOTFIRST_PREPROCESS_SWITCH);
            if (!MesCommonConstant.ROUTE_PROCESS_FIRST.equals(priorCode)) {
                throw new ILSBootException("P-OW-0047");
            }
            qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_ADD);
            qrItemCellInfo.setExistItemCell(ZeroOrOneEnum.ZERO.getStrCode());
            ItemCell itemCell = new ItemCell();
            qrItemCellInfo.setItemCell(itemCell);
        } else {
            if (lstItemCell.size() > 1) {
                throw new ILSBootException("P-OW-0029");
            }
            ItemCell itemCell = lstItemCell.get(0);
            // ??????????????????????????????????????????
            if (!ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(itemCell.getQcStatus())) {
                throw new ILSBootException("P-OW-0048");
            }

            // ??????????????????????????????????????????????????????
            if (!workProduceTask.getItemId().equals(itemCell.getItemId())) {
                throw new ILSBootException("P-OW-0049");
            }
            // ????????????????????????????????????????????????
            if (!workProduceTask.getOrderId().equals(itemCell.getOrderId())) {
                throw new ILSBootException("P-OW-0050");
            }
            // ????????????????????????????????????????????????
            String currentProcessCode = workProcessTask.getId();
            if (!currentProcessCode.equals(itemCell.getFromProcess())
                    && !currentProcessCode.equals(itemCell.getNowProcess())) {
                String processName = "";
                if (StringUtils.isNoneBlank(itemCell.getNowProcess())) {
                    WorkProcessTask itemCellProcessTask = workProcessTaskService.getById(itemCell.getNowProcess());
                    processName = itemCellProcessTask.getProcessName();
                }
                throw new ILSBootException("P-OW-0051", processName);
            }
            // ????????????????????????????????????
            if (currentProcessCode.equals(itemCell.getNowProcess())) {
                qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_ADD);
            } else {
                qrItemCellInfo.setEditPage(MesCommonConstant.TASK_RECORD_PAGE_TYPE_EDIT);
            }
            qrItemCellInfo.setExistItemCell(ZeroOrOneEnum.ONE.getStrCode());
            qrItemCellInfo.setItemCell(itemCell);
        }
        return qrItemCellInfo;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     * @param workProcessTask
     * @param qrItemCellInfo
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void saveQrcodeQualifiedWorkProduceRecord(WorkProduceRecord workProduceRecord,
                                                      WorkProduceTask workProduceTask,
                                                      WorkProcessTask workProcessTask,
                                                      QrItemCellInfo qrItemCellInfo,
                                                      boolean isJointProduct) {

        String itemCellStateId = this.saveQrcodeItemCell(workProduceRecord, workProduceTask, workProcessTask, qrItemCellInfo, isJointProduct);

        if (!isJointProduct && MesCommonConstant.ROUTE_PROCESS_END.equals(workProcessTask.getNextCode())) {
            WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
            workProduceRecord.setUnit(workOrder.getUnit());
            workProduceRecord.setUnitName(unitService.getById(workOrder.getUnit()).getUnitName());
        }
        workProduceRecord.setItemCellStateId(itemCellStateId);
        this.fillWorkProduceRecord(workProduceRecord, workProduceTask);
        if (isJointProduct) {
            workProduceRecord.setUnit(qrItemCellInfo.getItemCell().getUnitId());
            workProduceRecord.setUnitName(qrItemCellInfo.getItemCell().getUnitName());
            workProduceRecord.setProductType(RecordProductTypeEnum.JOINT_PRODUCT.getValue());
        }
        //??????????????????????????????
        String qcStatus = this.queryIfHasQcMethodRelateWorkProduce(workProduceTask.getId());
        workProduceRecord.setQcStatus(qcStatus);
        this.save(workProduceRecord);
        if (!isJointProduct) {
            this.saveCalQty(workProduceRecord, workProduceTask, workProcessTask);
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     * @param workProcessTask
     * @param qrItemCellInfo
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void saveQrcodeUnQualifiedWorkProduceRecord(WorkProduceRecord workProduceRecord,
                                                        WorkProduceTask workProduceTask, WorkProcessTask workProcessTask, QrItemCellInfo qrItemCellInfo) {

        String itemCellStateId =
                this.saveQrcodeUnQualifiedItemCell(workProduceRecord, workProduceTask, workProcessTask, qrItemCellInfo);

        if (MesCommonConstant.ROUTE_PROCESS_END.equals(workProcessTask.getNextCode())) {
            WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
            workProduceRecord.setUnit(workOrder.getUnit());
            workProduceRecord.setUnitName(unitService.getById(workOrder.getUnit()).getUnitName());
        }
        workProduceRecord.setItemCellStateId(itemCellStateId);
        this.fillWorkProduceRecord(workProduceRecord, workProduceTask);
        this.save(workProduceRecord);

        // ????????????????????????????????????
        QueryWrapper<WorkProduceTask> produceTaskqueryWrapper = new QueryWrapper<>();
        WorkProduceTask updateWorkProduceTask = new WorkProduceTask();
        produceTaskqueryWrapper.eq("id", workProduceTask.getId());
        updateWorkProduceTask
                .setBadQty(BigDecimalUtils.add(workProduceTask.getBadQty(), workProduceRecord.getSubmitQty()));
        workProduceTaskMapper.update(updateWorkProduceTask, produceTaskqueryWrapper);

    }

    @Transactional(rollbackFor = RuntimeException.class)
    private String saveQrcodeItemCell(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                                      WorkProcessTask workProcessTask, QrItemCellInfo qrItemCellInfo, boolean isJointProduct) {
        //??????????????????????????????????????????
        String qcStatus = this.queryIfHasQcMethodRelateWorkProduce(workProduceTask.getId());
        String nextCode = workProcessTask.getNextCode();
        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        ItemCell itemCell = null;
        String existItemCell = qrItemCellInfo.getExistItemCell();
        List<ItemStock> itemStocks = itemStockService.selectByMainId(qrItemCellInfo.getItemCell().getItemId());
        ItemStock itemStock = itemStocks.get(0);
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(existItemCell)) {
            itemCell = new ItemCell();
            itemCell.setItemId(qrItemCellInfo.getItemCell().getItemId());
            itemCell.setItemCode(qrItemCellInfo.getItemCell().getItemCode());
            itemCell.setItemName(qrItemCellInfo.getItemCell().getItemName());
            itemCell.setSpec(qrItemCellInfo.getItemCell().getSpec());
            itemCell.setOrderId(workProduceTask.getOrderId());
            itemCell.setOrderNo(workProduceTask.getOrderNo());
            itemCell.setProduceDate(new Date());
            if (null != itemStock.getValidTime()) {
                itemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
            } else {
                itemCell.setValidDate(new Date());
            }
            itemCell.setQrcode(workProduceRecord.getQrcode());
            itemCell.setBatch(workProduceRecord.getBatchNo());
            itemCell.setQty(workProduceRecord.getSubmitQty());
            itemCell.setQcStatus(qcStatus);
            itemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
            itemCell.setManageWay(ItemManageWayEnum.QRCODE_MANAGE.getValue());
            itemCell.setNote(workProduceRecord.getNote());
            itemCell.setBusinessStatus(ItemCellBusinessStatusEnum.NONE.getValue());
            itemCell.setNowProcess(workProcessTask.getNextCode());
            itemCell.setFromProcess(workProcessTask.getId());
            if (isJointProduct || MesCommonConstant.ROUTE_PROCESS_END.equals(nextCode)) {
                ItemCellFinishedStorageVO storageVO = this.checkWareHouseByStationId(workProduceTask.getStationId());
                this.fillItemCell(storageVO, itemCell);
                itemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            } else {
                itemCell.setPositionStatus(ItemCellPositionStatusEnum.MAKE.getValue());
            }
            itemCellService.save(itemCell);
        } else {
            itemCell = qrItemCellInfo.getItemCell();
            QueryWrapper<ItemCell> updateItemCellQueryWrapper = new QueryWrapper<>();
            ItemCell updateItemCell = new ItemCell();
            updateItemCellQueryWrapper.eq("id", itemCell.getId());
            if (isJointProduct || MesCommonConstant.ROUTE_PROCESS_END.equals(nextCode)) {
                ItemCellFinishedStorageVO storageVO = this.checkWareHouseByStationId(workProduceTask.getStationId());
                this.fillItemCell(storageVO, updateItemCell);
                updateItemCell.setUnitId(workOrder.getUnit());
                Unit unit = unitService.getById(workOrder.getUnit());
                updateItemCell.setUnitName(unit.getUnitName());
                updateItemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            }
            updateItemCell.setProduceDate(new Date());
            if (null != itemStock.getValidTime()) {
                updateItemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
            } else {
                updateItemCell.setValidDate(new Date());
            }
            updateItemCell.setNowProcess(workProcessTask.getNextCode());
            updateItemCell.setFromProcess(workProcessTask.getId());
            updateItemCell.setQcStatus(qcStatus);
            itemCell.setProduceDate(new Date());
            if (null != itemStock.getValidTime()) {
                itemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
            } else {
                itemCell.setValidDate(new Date());
            }
            if (ZeroOrOneEnum.ZERO.getStrCode().equals(qrItemCellInfo.getEditPage())) {
                updateItemCell.setQty(workProduceRecord.getSubmitQty());
            } else {
                updateItemCell.setQty(BigDecimalUtils.add(itemCell.getQty(), workProduceRecord.getSubmitQty()));
            }
            itemCellService.update(updateItemCell, updateItemCellQueryWrapper);
        }
        return itemCell.getId();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    private String saveQrcodeUnQualifiedItemCell(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                                                 WorkProcessTask workProcessTask, QrItemCellInfo qrItemCellInfo) {
        String nextCode = workProcessTask.getNextCode();
        WorkOrder workOrder = workOrderService.getById(workProduceTask.getOrderId());
        String existItemCell = qrItemCellInfo.getExistItemCell();
        ItemCell itemCell = null;
        List<ItemStock> itemStocks = itemStockService.selectByMainId(workProduceTask.getItemId());
        ItemStock itemStock = itemStocks.get(0);
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(existItemCell)) {
            itemCell = new ItemCell();
            itemCell.setItemId(workProduceTask.getItemId());
            itemCell.setItemCode(workProduceTask.getItemCode());
            itemCell.setOrderId(workProduceTask.getOrderId());
            itemCell.setQrcode(workProduceRecord.getQrcode());
            itemCell.setOrderNo(workProduceTask.getOrderNo());
            itemCell.setItemName(workProduceTask.getItemName());
            itemCell.setSpec(workOrder.getSpec());
            itemCell.setBatch(workProduceRecord.getBatchNo());
            itemCell.setQty(workProduceRecord.getSubmitQty());
            itemCell.setQcStatus(workProduceRecord.getQcStatus());
            itemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
            itemCell.setManageWay(ItemManageWayEnum.QRCODE_MANAGE.getValue());
            itemCell.setNote(workProduceRecord.getNote());
            itemCell.setBusinessStatus(ItemCellBusinessStatusEnum.NONE.getValue());
            itemCell.setNowProcess(workProcessTask.getNextCode());
            itemCell.setFromProcess(workProcessTask.getId());
            itemCell.setProduceDate(new Date());
            if (null != itemStock.getValidTime()) {
                itemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
            } else {
                itemCell.setValidDate(new Date());
            }
            if (MesCommonConstant.ROUTE_PROCESS_END.equals(nextCode)) {
                ItemCellFinishedStorageVO storageVO = this.checkWareHouseByStationId(workProduceTask.getStationId());
                this.fillItemCell(storageVO, itemCell);
                itemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            } else {
                itemCell.setPositionStatus(ItemCellPositionStatusEnum.MAKE.getValue());
            }
            itemCellService.save(itemCell);
        } else {
            itemCell = qrItemCellInfo.getItemCell();
            QueryWrapper<ItemCell> updatItemCellQueryWrapper = new QueryWrapper<>();
            ItemCell updateItemCell = new ItemCell();
            updatItemCellQueryWrapper.eq("id", itemCell.getId());
            if (MesCommonConstant.ROUTE_PROCESS_END.equals(nextCode)) {
                updateItemCell.setUnitId(workOrder.getUnit());
                Unit unit = unitService.getById(workOrder.getUnit());
                updateItemCell.setUnitName(unit.getUnitName());
                ItemCellFinishedStorageVO storageVO = this.checkWareHouseByStationId(workProduceTask.getStationId());
                this.fillItemCell(storageVO, updateItemCell);
                updateItemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            }
            updateItemCell.setNowProcess(workProcessTask.getNextCode());
            updateItemCell.setFromProcess(workProcessTask.getId());
            updateItemCell.setQcStatus(workProduceRecord.getQcStatus());
            updateItemCell.setProduceDate(new Date());
            if (null != itemStock.getValidTime()) {
                updateItemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
            } else {
                updateItemCell.setValidDate(new Date());
            }
            if (ProduceRecordQcStatusEnum.QUALIFIED.getValue().equals(itemCell.getQcStatus())) {
                updateItemCell.setQty(workProduceRecord.getSubmitQty());
            } else {
                updateItemCell.setQty(BigDecimalUtils.add(itemCell.getQty(), workProduceRecord.getSubmitQty()));
            }

            itemCellService.update(updateItemCell, updatItemCellQueryWrapper);
        }
        return itemCell.getId();
    }

    /**
     * ?????????????????????????????????
     *
     * @param workProduceRecord
     * @param workProduceTask
     */
    private void checkQrcodePreProcessQty(WorkProduceRecord workProduceRecord, WorkProduceTask workProduceTask,
                                          WorkProcessTask workProcessTask) {
        String priorCode = workProcessTask.getPriorCode();
        // ?????????????????????
        if (MesCommonConstant.ROUTE_PROCESS_FIRST.equals(priorCode)) {
            return;
        }
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.PRODUCE_RECORD_QRCODE_ADJUST_PREPROCESS_SWITCH);
        if (ZeroOrOneEnum.ONE.getStrCode().equals(bizConfig.getConfigValue())) {
            QueryWrapper<ProcessRecordVO> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("a.process_task_id", Arrays.asList(priorCode, workProcessTask.getId()));
            queryWrapper.eq("c.qrcode", workProduceRecord.getQrcode());
            queryWrapper.eq("c.qc_status", ProduceRecordQcStatusEnum.QUALIFIED.getValue());
            queryWrapper.groupBy(" a.process_task_id");
            List<ProcessRecordVO> lstProcessRecordVO = baseMapper.queryProcessSubmitQty(queryWrapper);
            if (CommonUtil.isEmptyOrNull(lstProcessRecordVO)) {
                throw new ILSBootException("P-OW-0017");
            }

            WorkProcessTask preWorkProcessTask = workProcessTaskService.getById(priorCode);
            BigDecimal prePlanQty = preWorkProcessTask.getPlanQty();
            BigDecimal currentPlanQty = workProcessTask.getPlanQty();
            ProcessRecordVO preProcessRecordVO = null;
            ProcessRecordVO currentProcessRecordVO = null;
            for (ProcessRecordVO processRecordVO : lstProcessRecordVO) {
                if (priorCode.equals(processRecordVO.getProcessTaskId())) {
                    preProcessRecordVO = processRecordVO;
                }

                if (workProcessTask.getId().equals(processRecordVO.getProcessTaskId())) {
                    currentProcessRecordVO = processRecordVO;
                }
            }
            BigDecimal currentSubmitTotalQty = BigDecimal.ZERO;
            if (currentProcessRecordVO != null) {
                currentSubmitTotalQty = currentProcessRecordVO.getSubmitTotalQty();
            }
            BigDecimal submitTotalQty =
                    BigDecimalUtils.add(currentSubmitTotalQty, workProduceRecord.getSubmitQty());

            // submitTotalQty/currentPlanQty>preSubmitTotalQty/prePlanQty==>completeQty*preTotalQty>preCompletedQty*totalQty
            Boolean isGreat = BigDecimalUtils.greaterThan(BigDecimalUtils.multiply(submitTotalQty, prePlanQty),
                    BigDecimalUtils.multiply(preProcessRecordVO.getSubmitTotalQty(), currentPlanQty));
            if (isGreat) {
                throw new ILSBootException("P-OW-0056");
            }
        }
    }

    @Autowired
    private QcMethodService qcMethodService;

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param id
     * @return
     */
    private String queryIfHasQcMethodRelateWorkProduce(String id) {
        //????????????????????????????????????
        List<QcMethod> qcMethodList = qcMethodService.queryQcMeThodByItemIdAndQcType(QcTaskQcTypeEnum.PRODUCTION_CHECK.getValue(), id);
        if (CollectionUtil.isNotEmpty(qcMethodList)) {
            return ItemQcStatusEnum.WAIT_TEST.getValue();
        }
        return ItemQcStatusEnum.QUALIFIED.getValue();
    }

    public static Date formatDate(String unit, Integer validTime, Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        switch (unit) {
            case POLICY_TIME_UNIT_ONE:
                instance.add(Calendar.MINUTE, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_TWO:
                instance.add(Calendar.HOUR, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_THREE:
                instance.add(Calendar.DATE, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_FOUR:
                instance.add(Calendar.MONTH, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_FIVE:
                instance.add(Calendar.YEAR, validTime);
                return instance.getTime();
            default:
                return null;
        }
    }

    @Override
    public Page<WorkProduceRecordReportVO> getReportData(WorkProduceRecord workProduceRecord,
                                                         Page<WorkProduceRecord> page,
                                                         HttpServletRequest req) {
        QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
        String dateString = getQueryWrapper(workProduceRecord, req, queryWrapper);
        //??????????????????????????????reportDate?????????
        Page<WorkProduceRecord> recordPage = this.page(page, queryWrapper);
        Page<WorkProduceRecordReportVO> dataPage = new Page<>();
        BeanUtils.copyProperties(recordPage, dataPage);
        dataPage.setRecords(new ArrayList<>());
        for (WorkProduceRecord record : recordPage.getRecords()) {
            WorkProduceRecordReportVO workProduceRecordReportVO = new WorkProduceRecordReportVO();
            BeanUtils.copyProperties(record, workProduceRecordReportVO);
            workProduceRecordReportVO.setReportDate(dateString);
            workProduceRecordReportVO.setId(UUID.randomUUID().toString().replace("-", ""));
            dataPage.getRecords().add(workProduceRecordReportVO);
        }
        return dataPage;
    }


    private String getQueryWrapper(WorkProduceRecord workProduceRecord, HttpServletRequest req, QueryWrapper<WorkProduceRecord> queryWrapper) {
        queryWrapper.ne("qc_status", ProduceRecordQcStatusEnum.UNQUALIFIED.getValue());
        if (StringUtils.isNotBlank(workProduceRecord.getItemName())) {
            queryWrapper.like("item_name", workProduceRecord.getItemName());
        }
        if (StringUtils.isNotBlank(workProduceRecord.getEmployeeId())) {
            queryWrapper.eq("employee_id", workProduceRecord.getEmployeeId());
        }
        StringBuilder addColumns = new StringBuilder();
        queryWrapper.groupBy("item_name,item_code,item_id,unit_name");
        queryWrapper.orderByAsc("item_name");
        //????????????
        String groupBy = req.getParameter("groupBy");
        if (StringUtils.isNotBlank(groupBy) && groupBy.contains(ReportGroupColumnEnum.GROUP_PROCESS.getValue())) {
            queryWrapper.groupBy("process_id,process_name,process_code");
            addColumns.append(",process_id,process_name,process_code");
        }
        if (StringUtils.isNotBlank(groupBy) && groupBy.contains(ReportGroupColumnEnum.GROUP_STATION.getValue())) {
            queryWrapper.groupBy("station_id,station_name,station_code");
            addColumns.append(",station_id,station_name,station_code");
        }
        if (StringUtils.isNotBlank(groupBy) && groupBy.contains(ReportGroupColumnEnum.GROUP_EMPLOYEE.getValue())) {
            queryWrapper.groupBy("employee_id,employee_name,employee_code");
            addColumns.append(",employee_id,employee_name,employee_code");
        }
        queryWrapper.select("item_name,item_code,item_id,unit_name,sum(submit_qty) as submit_qty" + addColumns.toString());

        //??????????????????????????????
        String type = req.getParameter("type");
        String dateRange = req.getParameter("dateRange");
        Date date = new Date();
        String dateString;
        String startDay;
        String endDay;
        switch (type) {
            case REPORT_MONTH:
                if (StringUtils.isNotBlank(dateRange)) {
                    date = DateUtil.parse(dateRange, DatePattern.NORM_MONTH_FORMAT);
                }
                startDay = DateUtil.format(DateUtil.beginOfMonth(date), DatePattern.NORM_DATE_FORMAT);
                endDay = DateUtil.format(DateUtil.endOfMonth(date), DatePattern.NORM_DATE_FORMAT);
                queryWrapper.between("produce_date", startDay, endDay);
                dateString = DateUtil.format(date, FastDateFormat.getInstance("yyyy???MM???"));
                break;
            case REPORT_WEEK:
                if (StringUtils.isNotBlank(dateRange)) {
                    date = DateUtil.parse(dateRange, DatePattern.NORM_DATE_FORMAT);
                }
                startDay = DateUtil.format(DateUtil.beginOfWeek(date), DatePattern.NORM_DATE_FORMAT);
                endDay = DateUtil.format(DateUtil.endOfWeek(date), DatePattern.NORM_DATE_FORMAT);
                queryWrapper.between("produce_date", startDay, endDay);
                dateString = startDay + "???" + endDay;
                break;
            case REPORT_DAY:
                if (StringUtils.isNotBlank(dateRange)) {
                    date = DateUtil.parse(dateRange, DatePattern.NORM_DATE_FORMAT);
                }
                queryWrapper.eq("produce_date", DateUtil.format(date, DatePattern.NORM_DATE_FORMAT));
                dateString = DateUtil.format(date, DatePattern.CHINESE_DATE_FORMAT);
                break;
            default:
                throw new ILSBootException("???????????????????????????");
        }
        return dateString;
    }

    @Override
    public ModelAndView exportReportXls(WorkProduceRecord workProduceRecord,
                                        HttpServletRequest req) {
        QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
        String dateString = getQueryWrapper(workProduceRecord, req, queryWrapper);
        List<WorkProduceRecord> workProduceRecordList = this.list(queryWrapper);
        List<WorkProduceRecordReportVO> exportList = new ArrayList<>(workProduceRecordList.size());
        workProduceRecordList.forEach(record -> {
            WorkProduceRecordReportVO workProduceRecordReportVO = new WorkProduceRecordReportVO();
            BeanUtils.copyProperties(record, workProduceRecordReportVO);
            workProduceRecordReportVO.setReportDate(dateString);
            exportList.add(workProduceRecordReportVO);
        });

        String title = "????????????";
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject("fileName", title);
        mv.addObject("entity", WorkProduceRecordReportVO.class);
        mv.addObject("params", new ExportParams(title, "?????????:" + sysUser.getRealname(), title));
        mv.addObject("data", exportList);
        return mv;
    }
}
