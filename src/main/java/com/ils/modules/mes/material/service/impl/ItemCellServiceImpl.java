package com.ils.modules.mes.material.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.common.util.RedisUtil;
import com.ils.modules.mes.base.factory.entity.Supplier;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.mapper.SupplierMapper;
import com.ils.modules.mes.base.factory.mapper.UnitMapper;
import com.ils.modules.mes.base.material.entity.ItemStock;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.mapper.ItemStockMapper;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.mapper.QcMethodMapper;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.mapper.WorkProduceMaterialRecordMapper;
import com.ils.modules.mes.execution.mapper.WorkProduceRecordMapper;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskMapper;
import com.ils.modules.mes.execution.vo.WorkProduceMaterialRecordVO;
import com.ils.modules.mes.label.entity.LabelManageLine;
import com.ils.modules.mes.label.service.LabelManageLineService;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemDeliveryGoodsRecord;
import com.ils.modules.mes.material.entity.ItemTakeDeliveryRecord;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import com.ils.modules.mes.material.mapper.ItemCellMapper;
import com.ils.modules.mes.material.mapper.ItemDeliveryGoodsRecordMapper;
import com.ils.modules.mes.material.mapper.ItemTakeDeliveryRecordMapper;
import com.ils.modules.mes.material.mapper.ItemTransferRecordMapper;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.material.service.ItemTransferRecordService;
import com.ils.modules.mes.material.vo.*;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.mapper.WorkOrderMapper;
import com.ils.modules.mes.qc.mapper.QcTaskRelateSampleMapper;
import com.ils.modules.mes.qc.service.QcTaskService;
import com.ils.modules.mes.qc.vo.QcTaskRecodTraceVO;
import com.ils.modules.mes.qc.vo.QcTaskSaveVO;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.entity.SopStep;
import com.ils.modules.mes.sop.service.SopControlService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;
import com.ils.modules.system.entity.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ils.modules.mes.base.sop.controller.SopTemplateController.EXECUTE_AUTHORITY_USER;
import static com.ils.modules.mes.execution.service.impl.WorkProduceRecordServiceImpl.formatDate;

/**
 * @Description: ????????????
 * @Author: wyssss
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Service
public class ItemCellServiceImpl extends ServiceImpl<ItemCellMapper, ItemCell> implements ItemCellService {
    private static final String WORK_PRODUCE_MATERIAL_RECORD = "batch";
    /**
     * ????????????????????????????????????????????????
     */
    private static final String ITEM_TRANSFER_RECORD_LOCK_PREFIX = "item_transfer_recode";
    /**
     * ????????????????????????
     */
    private final long EXPIRE_TIME = 1000L;
    /**
     * ??????beanUtil????????????????????????????????????
     */
    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";
    /**
     * ?????????????????????
     */
    private final String ITEM_CELL_LOCK_PREFIX = "item_cell";
    @Autowired
    private ItemService itemService;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private ItemTakeDeliveryRecordMapper itemTakeDeliveryRecordMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private ItemTransferRecordMapper itemTransferRecordMapper;
    @Autowired
    private WorkProduceRecordMapper workProduceRecordMapper;
    @Autowired
    private WorkProduceMaterialRecordMapper workProduceMaterialRecordMapper;
    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;
    @Autowired
    private UnitMapper unitMapper;
    @Autowired
    private QcTaskRelateSampleMapper qcTaskRelateSampleMapper;
    @Autowired
    private ItemTransferRecordService itemTransferRecordService;
    @Autowired
    private ItemDeliveryGoodsRecordMapper itemDeliveryGoodsRecordMapper;
    @Autowired
    private QcMethodMapper qcMethodMapper;
    @Autowired
    private LabelManageLineService labelManageLineService;
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private QcTaskService qcTaskService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveItemCell(ItemCell itemCell) {
        baseMapper.insert(itemCell);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateItemCell(ItemCell itemCell) {
        baseMapper.updateById(itemCell);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delItemCell(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchItemCell(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNoCodeItemCell(ItemCell itemCell) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        //????????????????????????????????????,??????????????????????????????????????????????????????????????????
        String qcStatus = this.getQcStatusWhenItemEnterFactory(itemCell);
        //??????????????????
        itemCellQueryWrapper.eq("item_id", itemCell.getItemId());
        itemCellQueryWrapper.eq("storage_id", itemCell.getStorageId());
        itemCellQueryWrapper.eq("qc_status", qcStatus);
        itemCellQueryWrapper.eq("manage_way", ItemManageWayEnum.NONE_MANAGE.getValue());
        itemCellQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        ItemCell itemCellOld = baseMapper.selectOne(itemCellQueryWrapper);
        //????????????
        itemUnitConversion(itemCell);
        if (itemCellOld != null) {
            itemCellOld.setQty(itemCellOld.getQty().add(itemCell.getQty()));
            baseMapper.updateById(itemCellOld);
            //????????????????????????
            itemCellOld.setQty(itemCell.getQty());
            saveItemTakeDeliveryRecord(itemCellOld);
        } else {
            //?????????????????????????????????
            setItemCellDefaultAttribute(itemCell);
            itemCell.setQcStatus(qcStatus);
            //??????????????????
            fillItemCell(itemCell, itemCell.getStorageCode());
            itemCell.setManageWay(ItemManageWayEnum.NONE_MANAGE.getValue());
            baseMapper.insert(itemCell);
            saveItemTakeDeliveryRecord(itemCell);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveBatchCodeItemCell(ItemCell itemCell) {
        //????????????????????????????????????,??????????????????????????????????????????????????????????????????
        String qcStatus = this.getQcStatusWhenItemEnterFactory(itemCell);
        //??????????????????
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("item_id", itemCell.getItemId());
        itemCellQueryWrapper.eq("storage_id", itemCell.getStorageId());
        itemCellQueryWrapper.eq("batch", itemCell.getBatch());
        itemCellQueryWrapper.eq("qc_status", qcStatus);
        itemCellQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        //????????????
        itemUnitConversion(itemCell);
        ItemCell itemCellOld = baseMapper.selectOne(itemCellQueryWrapper);
        if (itemCellOld != null) {
            itemCellOld.setQty(itemCellOld.getQty().add(itemCell.getQty()));
            baseMapper.updateById(itemCellOld);
            //????????????????????????
            itemCellOld.setQty(itemCell.getQty());
            saveItemTakeDeliveryRecord(itemCellOld);
        } else {
            //?????????????????????????????????
            setItemCellDefaultAttribute(itemCell);
            itemCell.setQcStatus(qcStatus);
            //????????????????????????
            itemUnitConversion(itemCell);
            //??????????????????
            fillItemCell(itemCell, itemCell.getStorageCode());
            itemCell.setManageWay(ItemManageWayEnum.BATCH_MANAGE.getValue());
            baseMapper.insert(itemCell);
            saveItemTakeDeliveryRecord(itemCell);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveQrcodeItemCell(ItemCell itemCell) {
        Integer qrCodeCount = baseMapper.queryCountQrcodeInAllPlace(itemCell.getQrcode());
        if (qrCodeCount > 0) {
            throw new ILSBootException("P-OW-0054");
        }
        //????????????????????????????????????,??????????????????????????????????????????????????????????????????
        String qcStatus = this.getQcStatusWhenItemEnterFactory(itemCell);
        //?????????????????????????????????
        this.setItemCellDefaultAttribute(itemCell);
        itemCell.setQcStatus(qcStatus);
        //??????????????????
        this.fillItemCell(itemCell, itemCell.getStorageCode());
        //??????????????????
        itemCell.setManageWay(ItemManageWayEnum.QRCODE_MANAGE.getValue());
        baseMapper.insert(itemCell);
        //????????????????????????
        saveItemTakeDeliveryRecord(itemCell);
        //???????????????????????????????????????????????????????????????
        LabelManageLine labelManageLine = labelManageLineService.queryByCode(itemCell.getQrcode());
        if (null != labelManageLine) {
            labelManageLine.setUseStatus(LabelUseStatusEnum.USED.getValue());
            labelManageLineService.updateById(labelManageLine);
        }
    }

    @Override
    public IPage<ItemCellBatchVO> queryBatchList(String itemId, String storageId, String unit,
                                                 Page<ItemCellBatchVO> page) {
        return baseMapper.queryBatchList(itemId, storageId, unit, page);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param itemCell
     */
    private void setItemCellDefaultAttribute(ItemCell itemCell) {
        itemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
        itemCell.setBusinessStatus(ItemCellBusinessStatusEnum.NONE.getValue());
        itemCell.setItemType(ItemTypeEunm.STORAGE.getValue());
        itemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param itemCell
     */
    private void itemUnitConversion(ItemCell itemCell) {
        // ??????????????????????????????????????????????????????
        List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(itemCell.getItemId());
        ItemUnit itemCellUnit = null;
        for (ItemUnit itemUnit : lstItemUnit) {
            if (itemUnit.getConvertUnit().equals(itemCell.getUnitId())) {
                itemCellUnit = itemUnit;
            }
        }
        // ???????????????????????????????????????????????????
        BigDecimal itemCellMainCount =
                BigDecimalUtils.divide(BigDecimalUtils.multiply(itemCell.getQty(), itemCellUnit.getMainUnitQty()),
                        itemCellUnit.getConvertQty(), 6);
        itemCell.setQty(itemCellMainCount);
        itemCell.setUnitId(itemCellUnit.getMainUnit());
        Unit unit = unitMapper.selectById(itemCellUnit.getMainUnit());
        itemCell.setUnitName(unit.getUnitName());
    }

    /**
     * ????????????????????????????????????
     *
     * @param itemCell
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveItemTakeDeliveryRecord(ItemCell itemCell) {
        ItemTakeDeliveryRecord itemTakeDeliveryRecord = new ItemTakeDeliveryRecord();
        itemTakeDeliveryRecord.setReceiveType("2");
        itemTakeDeliveryRecord.setItemCellId(itemCell.getId());
        itemTakeDeliveryRecord.setQrcode(itemCell.getQrcode());
        itemTakeDeliveryRecord.setBatch(itemCell.getBatch());
        itemTakeDeliveryRecord.setItemId(itemCell.getItemId());
        itemTakeDeliveryRecord.setItemCode(itemCell.getItemCode());
        itemTakeDeliveryRecord.setItemName(itemCell.getItemName());
        itemTakeDeliveryRecord.setQty(itemCell.getQty());
        itemTakeDeliveryRecord.setUnitId(itemCell.getUnitId());
        itemTakeDeliveryRecord.setUnitName(itemCell.getUnitName());
        itemTakeDeliveryRecord.setStorageCode(itemCell.getStorageCode());
        itemTakeDeliveryRecord.setStorageName(itemCell.getStorageName());
        itemTakeDeliveryRecord.setProduceDate(itemCell.getProduceDate());
        itemTakeDeliveryRecord.setValidDate(itemCell.getValidDate());
        itemTakeDeliveryRecord.setSupplierId(itemCell.getSupplierId());
        //????????????????????????
        Supplier supplier = supplierMapper.selectById(itemCell.getSupplierId());
        if (supplier != null) {
            itemTakeDeliveryRecord.setSupplierCode(supplier.getSupplierCode());
            itemTakeDeliveryRecord.setSupplierName(supplier.getSupplierName());
        }
        itemTakeDeliveryRecord.setSupplierBatch(itemCell.getSupplierBatch());
        itemTakeDeliveryRecord.setQualityStatus(itemCell.getQcStatus());
        itemTakeDeliveryRecord.setAttach(itemCell.getAttach());
        itemTakeDeliveryRecord.setNote(itemCell.getNote());
        itemTakeDeliveryRecordMapper.insert(itemTakeDeliveryRecord);
    }

    @Autowired
    private SopControlService sopControlService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void stocksOfInStorage(ItemTransferRecordVO itemTransferRecordVO) {
        String lockKey;
        String requestId;
        Map<String, String> lockMap = new HashMap<String, String>(8);
        try {
            for (ItemTransferRecord itemTransferRecord : itemTransferRecordVO.getItemTransferRecordList()) {
                //??????uuid??????
                requestId = UUID.fastUUID().toString();
                lockKey = ITEM_TRANSFER_RECORD_LOCK_PREFIX + itemTransferRecord.getId();
                if (!lockMap.containsKey(lockKey)) {
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        throw new ILSBootException("??????{0}??????????????????,???????????????", itemTransferRecord.getItemCode());
                    }
                    lockMap.put(lockKey, requestId);
                }
                //????????????????????????
                String[] ignoreProperty = IGNORE_PROPERTY.split(",");
                ItemCell itemCell = new ItemCell();
                BeanUtils.copyProperties(itemTransferRecord, itemCell, ignoreProperty);
                itemCell.setQcStatus(itemTransferRecord.getQualityStatus());
                itemCell.setNote(itemTransferRecord.getInNote());
                ItemStock itemStock = itemStockMapper.selectByMainId(itemCell.getItemId()).get(0);
                itemCell.setProduceDate(new Date());
                if (null != itemStock.getValidTime()) {
                    itemCell.setValidDate(formatDate(itemStock.getValidUnit(), itemStock.getValidTime(), new Date()));
                } else {
                    itemCell.setValidDate(new Date());
                }
                //??????????????????
                QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
                if (ItemManageWayEnum.BATCH_MANAGE.getValue().equals(itemCell.getManageWay())) {
                    itemCellQueryWrapper.eq("batch", itemCell.getBatch());
                }
                itemCellQueryWrapper.eq("manage_way", itemCell.getManageWay());
                itemCellQueryWrapper.eq("qc_status", itemCell.getQcStatus());
                itemCellQueryWrapper.eq("item_id", itemCell.getItemId());
                itemCellQueryWrapper.eq("storage_code", itemTransferRecordVO.getHopeInStorageCode());
                itemCellQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
                //???????????????????????????????????????
                ItemCell itemCell1 = baseMapper.selectOne(itemCellQueryWrapper);
                if (itemCell1 != null) {
                    try {
                        lockKey = ITEM_CELL_LOCK_PREFIX + itemCell1.getId();
                        requestId = UUID.fastUUID().toString();
                        //????????????????????????
                        if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                            throw new ILSBootException("P-STO-0099", itemCell1.getItemCode());
                        }
                        itemCell1.setQty(itemCell1.getQty().add(itemCell.getQty()));
                        baseMapper.updateById(itemCell1);
                    } finally {
                        //?????????
                        redisUtil.releaseDistributedLock(lockKey, requestId);
                    }
                } else {
                    //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    itemCell.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
                    itemCell.setPositionStatus(itemTransferRecord.getQualityStatus());
                    itemCell.setBusinessStatus(ItemCellBusinessStatusEnum.NONE.getValue());
                    itemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
                    itemCell.setItemType(ItemTypeEunm.STORAGE.getValue());
                    itemCell.setHopeInStorageCode("");
                    itemCell.setHopeInStorageName("");
                    this.fillItemCell(itemCell, itemTransferRecordVO.getHopeInStorageCode());
                    baseMapper.insert(itemCell);
                }
                //??????????????????
                itemTransferRecord.setTransferStatus(ItemTransferStatusEnum.IN_STORAGED.getValue());
                itemTransferRecord.setInStorageEmployee(CommonUtil.getLoginUser().getRealname());
                itemTransferRecord.setInStorageName(itemTransferRecordVO.getHopeInStorageName());
                itemTransferRecord.setInStorageCode(itemTransferRecordVO.getHopeInStorageCode());
                itemTransferRecord.setInTime(new Date());
                //?????????????????????????????????????????????
                BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.INVENTORY_STORAGE_EVENT_RELATE_SWITH);
                if (StorageEventRelateSwitchEnum.OPEN_IN_STORAGE.getValue().equals(bizConfig.getConfigValue())) {
                    itemTransferRecord.setBillCode(itemTransferRecordVO.getBillCode());
                    itemTransferRecord.setEventObject(itemTransferRecordVO.getEventObject());
                    itemTransferRecord.setEventId(itemTransferRecordVO.getEventId());
                    itemTransferRecord.setEventName(itemTransferRecordVO.getEventName());
                }
                //????????????id???????????????id
                if (StringUtils.isNotEmpty(itemTransferRecordVO.getControlId())) {
                    SopControl sopControl = sopControlService.getById(itemTransferRecordVO.getControlId());
                    itemTransferRecord.setSopControlId(itemTransferRecordVO.getControlId());
                    itemTransferRecord.setSopStepId(sopControl.getSopStepId());
                    itemTransferRecord.setTaskId(sopControl.getRelatedTaskId());
                }
                itemTransferRecordMapper.updateById(itemTransferRecord);
            }
        } finally {
            //?????????
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void labelCodeStorage(ItemCellStorageVO itemCellStorageVO) {
        String lockKey = null;
        String requestId = null;
        Map<String, String> lockMap = new HashMap<String, String>(8);
        try {
            for (ItemCell itemCell : itemCellStorageVO.getItemCellList()) {
                this.redisLock(lockMap, itemCell);
                QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
                itemCellQueryWrapper.eq("qrcode", itemCell.getQrcode());
                ItemCell itemCell1 = baseMapper.selectOne(itemCellQueryWrapper);
                if (itemCell1 == null) {
                    throw new ILSBootException("P-OW-0060");
                }
                if (!ItemCellQrcodeStatusEnum.FACTORY.getValue().equals(itemCell1.getQrcodeStatus())) {
                    throw new ILSBootException("P-OW-0059");
                }
                this.checkQcStatus(itemCell1, itemCellStorageVO.getHopeInStorageCode());
                itemCell1.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
                itemCell1.setHopeInStorageCode("")
                        .setHopeInStorageName("");
                this.fillItemCell(itemCell1, itemCellStorageVO.getHopeInStorageCode());
                baseMapper.updateById(itemCell1);
                //??????????????????
                itemTransferRecordService.saveItemCellInStorageRecord(itemCell1, itemCellStorageVO.getEventId(), itemCellStorageVO.getEventName(), itemCellStorageVO.getEventObject(), itemCellStorageVO.getBillCode(), itemCellStorageVO.getControlId());
            }
        } finally {
            //??????redis???
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param itemCell
     * @param storageCode
     */
    private void checkQcStatus(ItemCell itemCell, String storageCode) {
        String qcStatus = itemCell.getQcStatus();
        ItemCellFinishedStorageVO finishedStorageByStorageCode = wareHouseService.findFinishedStorageByStorageCode(storageCode);
        String one = ZeroOrOneEnum.ONE.getStrCode();
        String storageQcStatus = null;
        if (!finishedStorageByStorageCode.getSecondQcControl().equals(one)) {
            //???????????? 1,??????????????????2??????????????????
            storageQcStatus = finishedStorageByStorageCode.getSecondQcStatus();
        } else if (!finishedStorageByStorageCode.getFirstQcControl().equals(one)) {
            //???????????? 1,??????????????????2??????????????????
            storageQcStatus = finishedStorageByStorageCode.getFirstQcStatus();
        } else if (finishedStorageByStorageCode.getWareHouseQcControl().equals(one)) {
            //??????  1,??????0??????
            storageQcStatus = finishedStorageByStorageCode.getWareHouseQcStatus();
        }
        if (StringUtils.isNoneBlank(storageQcStatus) && !storageQcStatus.contains(qcStatus)) {
            throw new ILSBootException("???????????????????????????????????????????????????????????????????????????");
        }
    }

    @Override
    public ItemCell queryItemCellByQrCodeInStorage(String qrcode, String storageCode) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("qrcode", qrcode);
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        itemCellQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        ItemCell itemCell = baseMapper.selectOne(itemCellQueryWrapper);
        //???????????????
        if (itemCell == null) {
            return null;
        }
        if (!ItemCellPositionStatusEnum.TRANSPORT.getValue().equals(itemCell.getPositionStatus())) {
            throw new ILSBootException("P-SOP-0102");
        }
        //????????????????????????????????????????????????????????????
        if (itemCell.getHopeInStorageCode() != null) {
            WareHouse wareHouse = wareHouseService.queryByStorageCode(storageCode);
            if (StringUtils.isNoneBlank(itemCell.getHopeInStorageCode()) && !wareHouse.getHouseCode().equals(itemCell.getHopeInStorageCode())) {
                throw new ILSBootException("P-OW-0066");
            }
        }
        return itemCell;
    }

    @Override
    public ItemCell queryItemCellByQrCodeOutStorage(String qrcode) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("qrcode", qrcode);
        itemCellQueryWrapper.eq("position_status", ItemCellPositionStatusEnum.STORAGE.getValue());
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        itemCellQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        ItemCell itemCell = baseMapper.selectOne(itemCellQueryWrapper);
        return itemCell;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void labelCodeOutStorage(ItemCellStorageVO itemCellStorageVO) {
        Map<String, String> lockMap = new HashMap<String, String>(8);
        try {
            for (ItemCell itemCell : itemCellStorageVO.getItemCellList()) {
                this.redisLock(lockMap, itemCell);
                ItemCell itemCellUpdate = new ItemCell();
                itemCellUpdate.setId(itemCell.getId());
                itemCellUpdate.setStorageId("")
                        .setStorageCode("")
                        .setStorageName("")
                        .setAreaCode("")
                        .setAreaName("")
                        .setHouseCode("")
                        .setHouseName("")
                        .setHopeInStorageName(itemCellStorageVO.getHopeInStorageName())
                        .setHopeInStorageCode(itemCellStorageVO.getHopeInStorageCode())
                        .setPositionStatus(ItemCellPositionStatusEnum.TRANSPORT.getValue());
                baseMapper.updateById(itemCellUpdate);
                //??????????????????
                BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.TAG_STORAGE_EVENT_RELATE_SWITH);
                boolean isOpenEvent = StorageEventRelateSwitchEnum.OPEN_OUT_STORAGE.getValue().equals(bizConfig.getConfigValue());
                itemTransferRecordService.saveItemCellOutStorageRecord(itemCell, itemCellStorageVO.getEventId(), itemCellStorageVO.getEventName(),
                        itemCellStorageVO.getEventObject(), itemCellStorageVO.getHopeInStorageName(), itemCellStorageVO.getHopeInStorageCode(), itemCellStorageVO.getBillCode(), isOpenEvent, itemCellStorageVO.getControlId());
            }
        } finally {
            //??????redis???
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void stocksOfOutStorage(ItemCellStorageVO itemCellStorageVO) {
        String lockKey = null;
        String requestId = null;
        Map<String, String> lockMap = new HashMap<String, String>(8);
        try {
            for (ItemCellOutStorageVO itemCell2 : itemCellStorageVO.getItemCellList()) {
                ItemCell itemCell = baseMapper.selectById(itemCell2.getId());
                //????????????????????????????????????????????????????????????????????????????????????????????????
                if (itemCell.getQty().compareTo(itemCell2.getOutQty()) < 0) {
                    throw new ILSBootException("P-OW-0062");
                }
                if (itemCell2.getQty().compareTo(new BigDecimal(0)) < 0) {
                    throw new ILSBootException("P-OW-0061");
                }
                lockKey = ITEM_CELL_LOCK_PREFIX + itemCell2.getId();
                requestId = UUID.fastUUID().toString();
                if (!lockMap.containsKey(lockKey)) {
                    //???????????????????????????????????????????????????????????????????????????
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        throw new ILSBootException("??????{0}??????????????????,???????????????", itemCell2.getItemCode());
                    }
                    lockMap.put(lockKey, requestId);
                }
                //??????????????????
                itemCell2.setQty(itemCell2.getOutQty());
                //??????????????????????????????
                BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.INVENTORY_STORAGE_EVENT_RELATE_SWITH);
                boolean isOpenEvent = StorageEventRelateSwitchEnum.OPEN_OUT_STORAGE.getValue().equals(bizConfig.getConfigValue());
                itemTransferRecordService.saveItemCellOutStorageRecord(itemCell2, itemCellStorageVO.getEventId(), itemCellStorageVO.getEventName(), itemCellStorageVO.getEventObject(),
                        itemCellStorageVO.getHopeInStorageName(), itemCellStorageVO.getHopeInStorageCode(), itemCellStorageVO.getBillCode(), isOpenEvent, itemCellStorageVO.getControlId());
                itemCell.setQty(itemCell.getQty().subtract(itemCell2.getOutQty()));
                baseMapper.updateById(itemCell);
            }
        } finally {
            //??????redis???
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param itemCell
     * @param storageCode
     */
    private void fillItemCell(ItemCell itemCell, String storageCode) {
        ItemCellFinishedStorageVO storageVO = wareHouseService.findFinishedStorageByStorageCode(storageCode);
        itemCell.setStorageId(storageVO.getSecondStorageId());
        itemCell.setStorageCode(storageVO.getSecondStorageCode());
        itemCell.setStorageName(storageVO.getSecondStorageName());
        itemCell.setAreaCode(storageVO.getFirstStorageCode());
        itemCell.setAreaName(storageVO.getFirstStorageName());
        itemCell.setHouseCode(storageVO.getWareHouseCode());
        itemCell.setHouseName(storageVO.getWareHouseName());
    }

    @Override
    public List<ItemCell> queryStocksOfOutStorage(String deliveryLocation) {
        return baseMapper.queryOutStocksItemCell(deliveryLocation);
    }

    @Override
    public IPage<QrCodeItemCellFollowVO> queryMaterialLabelFollow(Page<QrCodeItemCellFollowVO> page, QueryWrapper<QrCodeItemCellFollowVO> queryWrapper) {
        return baseMapper.queryMaterialLabelFollow(page, queryWrapper);
    }

    @Override
    public TraceItemCellQrcodeVO traceItemCellWithQrcode(String id) {
        ItemCell itemCell = baseMapper.selectById(id);
        TraceItemCellQrcodeVO traceItemCellQrcodeVO = queryAllRecordByQrcodeOrBatch("qrcode", itemCell.getQrcode());
        BeanUtils.copyProperties(itemCell, traceItemCellQrcodeVO);
        return traceItemCellQrcodeVO;
    }

    @Override
    public TraceItemCellQrcodeVO batchItemCellTraceById(String itemId) {
        ItemCell itemCell = baseMapper.selectById(itemId);
        TraceItemCellQrcodeVO traceItemCellQrcodeVO = queryAllRecordByQrcodeOrBatch("batch", itemCell.getBatch());
        BeanUtils.copyProperties(itemCell, traceItemCellQrcodeVO);
        return traceItemCellQrcodeVO;
    }

    @Override
    public RfTraceRecordVO rfQrcodeItemCellTrace(String qrcode) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("qrcode", qrcode);
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        ItemCell itemCell = baseMapper.selectOne(itemCellQueryWrapper);
        if (itemCell == null) {
            throw new ILSBootException("P-OW-0067");
        }

        RfTraceRecordVO rfTraceRecordVO = new RfTraceRecordVO();
        BeanUtils.copyProperties(itemCell, rfTraceRecordVO);

        //?????????????????????
        this.getRecordByItemCellId(rfTraceRecordVO, false);

        return rfTraceRecordVO;
    }

    @Override
    public RfTraceRecordVO rfBatchItemCellTrace(String batch, String itemCellId) {
        ItemCell itemCell = baseMapper.selectById(itemCellId);
        RfTraceRecordVO rfTraceRecordVO = new RfTraceRecordVO();
        BeanUtils.copyProperties(itemCell, rfTraceRecordVO);

        //????????????????????????
        this.getRecordByItemCellId(rfTraceRecordVO, true);

        return rfTraceRecordVO;
    }

    @Override
    public List<DashBoardDataVO> queryNowProcessItemQty(String processId) {
        return baseMapper.querDashBoardItemQty(processId);
    }

    @Override
    public JSONArray queryAccountedForItemProcessed() {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("is_deleted ", ZeroOrOneEnum.ZERO.getIcode());
        itemCellQueryWrapper.eq("position_status", ItemCellPositionStatusEnum.MAKE.getValue());
        itemCellQueryWrapper.groupBy("item_name", "item_code", "item_id");
        itemCellQueryWrapper.select("sum(qty) as qty,item_name,item_code,item_id");
        List<ItemCell> itemCellList = baseMapper.selectList(itemCellQueryWrapper);
        BigDecimal allQty = BigDecimal.ZERO;
        JSONArray itemCellJson = new JSONArray();
        for (ItemCell itemCell : itemCellList) {
            allQty = allQty.add(itemCell.getQty());
        }
        for (ItemCell itemCell : itemCellList) {
            JSONObject itemJson = new JSONObject();
            itemJson.put("item_name", itemCell.getItemName());
            itemJson.put("item_code", itemCell.getItemCode());
            itemJson.put("qty", itemCell.getQty());
            itemJson.put("percentage", itemCell.getQty().divide(allQty, 6, BigDecimal.ROUND_HALF_UP));
            itemCellJson.add(itemJson);
        }
        return itemCellJson;
    }

    @Override
    public List<ProcessMaterialQuantityVO> queryProcessedItemQty() {
        return baseMapper.queryProcessItemQty();
    }

    @Override
    public List<DashBoardProcessItemQtyVO> selectDashBoardProcessItemQtyVO() {
        return baseMapper.queryDashBoardProcessItemQtyVO();
    }

    /**
     * @param code
     * @param codeValue
     * @return
     */
    private TraceItemCellQrcodeVO queryAllRecordByQrcodeOrBatch(String code, String codeValue) {
        TraceItemCellQrcodeVO traceItemCellQrcodeVO = new TraceItemCellQrcodeVO();
        //??????????????????
        QueryWrapper<ItemTransferRecord> itemTransferRecordQueryWrapper = new QueryWrapper<>();
        itemTransferRecordQueryWrapper.eq(code, codeValue);
        itemTransferRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<ItemTransferRecord> itemTransferRecordList = itemTransferRecordMapper.selectList(itemTransferRecordQueryWrapper);
        traceItemCellQrcodeVO.setItemTransferRecordList(itemTransferRecordList);
        //??????????????????
        List<QcTaskRecodTraceVO> qcTaskRecordTraceVOList = qcTaskRelateSampleMapper.qcTaskItemTraceRecord(code, codeValue);
        traceItemCellQrcodeVO.setQcTaskRecordTraceVOList(qcTaskRecordTraceVOList);
        //????????????
        QueryWrapper<WorkProduceRecord> workProduceRecordQueryWrapper = new QueryWrapper<>();
        if (code.equals(WORK_PRODUCE_MATERIAL_RECORD)) {
            workProduceRecordQueryWrapper.eq("batch_no", codeValue);
        } else {
            workProduceRecordQueryWrapper.eq(code, codeValue);
        }
        workProduceRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WorkProduceRecord> workProduceRecordList = workProduceRecordMapper.selectList(workProduceRecordQueryWrapper);
        traceItemCellQrcodeVO.setWorkProduceRecordList(workProduceRecordList);
        // ????????????
        List<WorkProduceMaterialRecordVO> workProduceMaterialRecordVOList = new ArrayList<>(16);
        Map<String, List<WorkProduceRecord>> workProduceRecordMap = workProduceRecordList.stream().collect(Collectors.groupingBy(WorkProduceRecord::getProduceTaskId));
        if (!CommonUtil.isEmptyOrNull(workProduceRecordList)) {
            for (String key : workProduceRecordMap.keySet()) {
                QueryWrapper<WorkProduceMaterialRecord> workProduceMaterialRecordQueryWrapper = new QueryWrapper<>();
                workProduceMaterialRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
                workProduceMaterialRecordQueryWrapper.eq("produce_task_id", key);
                List<WorkProduceMaterialRecord> workProduceMaterialRecordList = workProduceMaterialRecordMapper.selectList(workProduceMaterialRecordQueryWrapper);
                for (WorkProduceMaterialRecord workProduceMaterialRecord : workProduceMaterialRecordList) {
                    WorkProduceMaterialRecordVO workProduceMaterialRecordVO = new WorkProduceMaterialRecordVO();
                    BeanUtils.copyProperties(workProduceMaterialRecord, workProduceMaterialRecordVO);
                    workProduceMaterialRecordVO.setProcessName(workProduceRecordMap.get(key).get(0).getProcessName());
                    workProduceMaterialRecordVO.setWorkStation(workProduceRecordMap.get(key).get(0).getStationName());
                    workProduceMaterialRecordVOList.add(workProduceMaterialRecordVO);
                }
            }
        }
        traceItemCellQrcodeVO.setFeedingRecordList(workProduceMaterialRecordVOList);
        //????????????
        List<WorkProduceMaterialRecordVO> recordVOList = new ArrayList<>(16);
        initWorkProduceMaterialRecord(code, codeValue, recordVOList);
        traceItemCellQrcodeVO.setFlowRecordList(recordVOList);
        //???????????????
        List<EntryAndExitRecordVO> entryAndExitRecordVOList = new ArrayList<>(16);
        initEntryAndExitRecord(code, codeValue, entryAndExitRecordVOList);
        traceItemCellQrcodeVO.setEntryAndExitRecordVOList(entryAndExitRecordVOList);
        return traceItemCellQrcodeVO;
    }

    /**
     * ??????????????????
     *
     * @param code         ????????????????????????
     * @param codeValue
     * @param recordVOList
     */
    private void initWorkProduceMaterialRecord(String code, String codeValue, List<WorkProduceMaterialRecordVO> recordVOList) {
        QueryWrapper<WorkProduceMaterialRecord> workProduceMaterialRecordQueryWrapper = new QueryWrapper<>();
        if (code.equals(WORK_PRODUCE_MATERIAL_RECORD)) {
            workProduceMaterialRecordQueryWrapper.eq("batch_no", codeValue);
        } else {
            workProduceMaterialRecordQueryWrapper.eq(code, codeValue);
        }
        workProduceMaterialRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WorkProduceMaterialRecord> workProduceMaterialRecordList = workProduceMaterialRecordMapper.selectList(workProduceMaterialRecordQueryWrapper);
        for (WorkProduceMaterialRecord workProduceMaterialRecord : workProduceMaterialRecordList) {
            WorkProduceMaterialRecordVO workProduceMaterialRecordVO = new WorkProduceMaterialRecordVO();
            WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(workProduceMaterialRecord.getProduceTaskId());
            if (workProduceTask != null) {
                BeanUtils.copyProperties(workProduceMaterialRecord, workProduceMaterialRecordVO);
                workProduceMaterialRecordVO.setWorkStation(workProduceTask.getStationName());
                workProduceMaterialRecordVO.setProcessName(workProduceTask.getProcessName());
                workProduceMaterialRecordVO.setOrderNo(workProduceTask.getOrderNo());
                workProduceMaterialRecordVO.setTaskCode(workProduceTask.getTaskCode());
                recordVOList.add(workProduceMaterialRecordVO);
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param code                     ????????????????????????
     * @param codeValue
     * @param entryAndExitRecordVOList
     */
    private void initEntryAndExitRecord(String code, String codeValue, List<EntryAndExitRecordVO> entryAndExitRecordVOList) {
        QueryWrapper<ItemDeliveryGoodsRecord> itemDeliveryGoodsRecordQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ItemTakeDeliveryRecord> itemTakeDeliveryRecordQueryWrapper = new QueryWrapper<>();
        itemDeliveryGoodsRecordQueryWrapper.eq(code, codeValue);
        itemTakeDeliveryRecordQueryWrapper.eq(code, codeValue);
        itemDeliveryGoodsRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        itemTakeDeliveryRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<ItemDeliveryGoodsRecord> itemDeliveryGoodsRecordList = itemDeliveryGoodsRecordMapper.selectList(itemDeliveryGoodsRecordQueryWrapper);
        List<ItemTakeDeliveryRecord> itemTakeDeliveryRecords = itemTakeDeliveryRecordMapper.selectList(itemTakeDeliveryRecordQueryWrapper);
        itemDeliveryGoodsRecordList.forEach(itemDeliveryGoodsRecord -> {
            EntryAndExitRecordVO entryAndExitRecordVO = new EntryAndExitRecordVO();
            BeanUtils.copyProperties(itemDeliveryGoodsRecord, entryAndExitRecordVO);
            entryAndExitRecordVO.setType(EntryAndExitEnum.EXIT.getDesc());
            entryAndExitRecordVOList.add(entryAndExitRecordVO);
        });
        itemTakeDeliveryRecords.forEach(itemTakeDeliveryRecord -> {
            EntryAndExitRecordVO entryAndExitRecordVO = new EntryAndExitRecordVO();
            BeanUtils.copyProperties(itemTakeDeliveryRecord, entryAndExitRecordVO);
            entryAndExitRecordVO.setType(EntryAndExitEnum.ENTRY.getDesc());
            entryAndExitRecordVOList.add(entryAndExitRecordVO);
        });
    }

    @Override
    public IPage<ItemCell> listPage(QueryWrapper<ItemCell> queryWrapper, Page<ItemCell> page) {
        IPage<ItemCell> itemCellPage = baseMapper.listPage(queryWrapper, page);
        return itemCellPage;
    }

    @Override
    public IPage<ItemCell> listPageStorage(QueryWrapper<ItemCell> queryWrapper, Page<ItemCell> page) {
        IPage<ItemCell> itemCellPage = baseMapper.itemStockQtyPage(queryWrapper, page);
        return itemCellPage;
    }

    @Override
    public void labelCodeDeliveryGoods(List<ItemCell> itemCellList) {
        Map<String, String> lockMap = new HashMap<>(6);
        try {
            for (ItemCell itemCell : itemCellList) {
                ItemCell originalItemCell = baseMapper.selectById(itemCell.getId());
                // ???????????????
                if (!ItemTypeEunm.STORAGE.getValue().equals(originalItemCell.getItemType())) {
                    throw new ILSBootException("P-OW-0076");
                }
                this.checkCondition(itemCell, originalItemCell);
                //????????????
                this.redisLock(lockMap, itemCell);
                //???????????????
                originalItemCell.setQrcodeStatus(ItemCellQrcodeStatusEnum.DELIVERED.getValue());
                baseMapper.updateById(originalItemCell);
                //????????????
                ItemDeliveryGoodsRecord itemDeliveryGoodsRecord = new ItemDeliveryGoodsRecord();
                String[] ignoreProperty = IGNORE_PROPERTY.split(",");
                BeanUtils.copyProperties(originalItemCell, itemDeliveryGoodsRecord, ignoreProperty);
                itemDeliveryGoodsRecord.setItemCellId(originalItemCell.getId());
                itemDeliveryGoodsRecord.setQualityStatus(originalItemCell.getQcStatus());
                itemDeliveryGoodsRecord.setWorkOrderCode(originalItemCell.getOrderNo());
                itemDeliveryGoodsRecord.setDeliverType(ItemDeliverTypeEnum.GENERAL.getValue());
                itemDeliveryGoodsRecordMapper.insert(itemDeliveryGoodsRecord);
            }
        } finally {
            //??????redis???
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void stocksOfDeliveryGoods(List<ItemCellOutStorageVO> itemCellOutStorageVOList) {
        Map<String, String> lockMap = new HashMap<>(6);
        try {
            for (ItemCellOutStorageVO itemCellOutStorageVO : itemCellOutStorageVOList) {
                ItemCell originalItemCell = baseMapper.selectById(itemCellOutStorageVO.getId());
                //???????????????
                itemCellOutStorageVO.setQty(itemCellOutStorageVO.getOutQty());
                this.checkCondition(itemCellOutStorageVO, originalItemCell);
                //????????????
                this.redisLock(lockMap, itemCellOutStorageVO);
                //???????????????
                originalItemCell.setQty(originalItemCell.getQty().subtract(itemCellOutStorageVO.getOutQty()));
                baseMapper.updateById(originalItemCell);
                //????????????
                ItemDeliveryGoodsRecord itemDeliveryGoodsRecord = new ItemDeliveryGoodsRecord();
                String[] ignoreProperty = IGNORE_PROPERTY.split(",");
                BeanUtils.copyProperties(originalItemCell, itemDeliveryGoodsRecord, ignoreProperty);
                itemDeliveryGoodsRecord.setQualityStatus(originalItemCell.getQcStatus());
                itemDeliveryGoodsRecord.setDeliverType(ItemDeliverTypeEnum.GENERAL.getValue());
                itemDeliveryGoodsRecord.setQty(itemCellOutStorageVO.getQty());
                itemDeliveryGoodsRecord.setItemCellId(originalItemCell.getId());
                itemDeliveryGoodsRecord.setWorkOrderCode(originalItemCell.getOrderNo());
                itemDeliveryGoodsRecordMapper.insert(itemDeliveryGoodsRecord);
            }
        } finally {
            //??????redis???
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    private void redisLock(Map<String, String> lockMap, ItemCell itemCell) {
        String lockKey;
        String requestId;
        lockKey = ITEM_CELL_LOCK_PREFIX + itemCell.getId();
        requestId = UUID.fastUUID().toString();
        if (!lockMap.containsKey(lockKey)) {
            //???????????????????????????????????????????????????????????????????????????
            if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                throw new ILSBootException("??????{0}??????????????????,???????????????", itemCell.getItemCode());
            }
            lockMap.put(lockKey, requestId);
        }
    }

    private void checkCondition(ItemCell itemCell, ItemCell originalItemCell) {
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (originalItemCell.getQty().compareTo(new BigDecimal(0)) < 0) {
            throw new ILSBootException("?????????????????????");
        }
        if (originalItemCell.getQty().compareTo(itemCell.getQty()) < 0) {
            throw new ILSBootException("?????????????????????????????????");
        }
        if (!ItemCellPositionStatusEnum.STORAGE.getValue().equals(originalItemCell.getQrcodeStatus())) {
            throw new ILSBootException("???????????????????????????????????????");
        }
        if (!ItemQcStatusEnum.QUALIFIED.getValue().equals(originalItemCell.getQcStatus())) {
            throw new ILSBootException("????????????????????????????????????");
        }
        if (!ItemCellBusinessStatusEnum.NONE.getValue().equals(originalItemCell.getBusinessStatus())) {
            throw new ILSBootException("??????????????????????????????");
        }
    }

    /**
     * ?????????????????????id????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param itemId
     * @return
     */
    private String getQcStatusWhenItemEnterFactory(ItemCell itemCell) {
        //????????????????????????????????????
        List<QcMethod> qcMethodList = qcMethodMapper.queryQcMeThodByItemIdAndQcType(QcTaskQcTypeEnum.ADMISSION_CHECK.getValue(), itemCell.getItemId());
        String qcStatus;
        if (CommonUtil.isEmptyOrNull(qcMethodList)) {
            qcStatus = ItemQcStatusEnum.QUALIFIED.getValue();
        } else {
            qcStatus = ItemQcStatusEnum.WAIT_TEST.getValue();
            this.buildQcTask(qcMethodList.get(0), itemCell);
        }
        return qcStatus;
    }


    private void buildQcTask(QcMethod qcMethod, ItemCell itemCell) {
        //?????????????????????????????????
        QcTaskSaveVO qcTaskSaveVO = new QcTaskSaveVO();
        qcTaskSaveVO.setQcType(qcMethod.getQcType());

        qcTaskSaveVO.setMethodId(qcMethod.getId());
        qcTaskSaveVO.setMethodName(qcMethod.getMethodName());

        qcTaskSaveVO.setLocationType(ZeroOrOneEnum.ONE.getStrCode());
        qcTaskSaveVO.setLocationCode(itemCell.getStorageCode());
        qcTaskSaveVO.setLocationId(itemCell.getStorageId());
        qcTaskSaveVO.setLocationName(itemCell.getStorageName());

        qcTaskSaveVO.setItemCode(itemCell.getItemCode());
        qcTaskSaveVO.setItemId(itemCell.getItemId());
        qcTaskSaveVO.setItemName(itemCell.getItemName());
        qcTaskSaveVO.setRelatedReceiptType(ZeroOrOneEnum.ZERO.getStrCode());

        qcTaskSaveVO.setTaskEmployeeIds("");
        qcTaskService.saveQcTask(qcTaskSaveVO);
    }

    private void getRecordByItemCellId(RfTraceRecordVO rfTraceRecordVO, boolean isBatch) {
        QueryWrapper<WorkProduceRecord> workProduceRecordQueryWrapper = new QueryWrapper<>();

        workProduceRecordQueryWrapper.eq("item_cell_state_id", rfTraceRecordVO.getId());
        workProduceRecordQueryWrapper.orderByDesc("create_time");
        workProduceRecordQueryWrapper.last("limit 1");
        WorkProduceRecord workProduceRecord = workProduceRecordMapper.selectOne(workProduceRecordQueryWrapper);

        //????????????
        if (workProduceRecord != null) {
            rfTraceRecordVO.setWorkEmployee(workProduceRecord.getEmployeeName());
            rfTraceRecordVO.setWorkStation(workProduceRecord.getStationName());
            rfTraceRecordVO.setWorkTime(workProduceRecord.getCreateTime());

        }

        // ??????????????????
        WorkOrder workOrder = workOrderMapper.selectById(rfTraceRecordVO.getOrderId());
        if (workOrder != null) {
            rfTraceRecordVO.setOrderNo(workOrder.getOrderNo());
            rfTraceRecordVO.setRouteId(workOrder.getRouteId());
            rfTraceRecordVO.setBomId(workOrder.getBomId());
            rfTraceRecordVO.setProductId(workOrder.getProductId());
        }

        //????????????????????????
        List<TransferRecordVO> transferRecordVOList = baseMapper.queryTansferRecordById(rfTraceRecordVO.getId());
        rfTraceRecordVO.setTransferRecordVOList(transferRecordVOList);

        //??????????????????
        List<QcInfoDetailVO> qcInfoDetailVOList = new ArrayList<>(16);
        if (isBatch) {
            if (StringUtils.isNotBlank(rfTraceRecordVO.getBatch())) {
                qcInfoDetailVOList = baseMapper.queryQcInfoDetailByBatch(rfTraceRecordVO.getBatch());
            }
        } else {
            if (StringUtils.isNotBlank(rfTraceRecordVO.getQrcode())) {
                qcInfoDetailVOList = baseMapper.queryQcInfoDetailByQrcode(rfTraceRecordVO.getQrcode());
            }
        }
        rfTraceRecordVO.setQcInfoDetailVOList(qcInfoDetailVOList);
    }
}
