package com.ils.modules.mes.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.material.entity.ItemContainer;
import com.ils.modules.mes.base.material.entity.ItemContainerQty;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.service.ItemContainerService;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.material.vo.ItemContainerQtyVO;
import com.ils.modules.mes.base.material.vo.ItemContainerVO;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.material.entity.*;
import com.ils.modules.mes.material.mapper.*;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.material.service.ItemContainerManageService;
import com.ils.modules.mes.material.vo.*;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.service.SopControlService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 13:43
 */

@Service
public class ItemContainerManageServiceImpl extends ServiceImpl<ItemContainerManageMapper, ItemContainerManage> implements ItemContainerManageService {
    @Autowired
    private ItemCellMapper itemCellMapper;
    @Autowired
    private ItemContainerService itemContainerService;
    @Autowired
    private ItemContainerManageDetailMapper itemContainerManageDetailMapper;
    @Autowired
    private ItemContainerLoadItemRecordMapper itemContainerLoadItemRecordMapper;
    @Autowired
    private ItemContainerTransferRecordMapper itemContainerTransferRecordMapper;
    @Autowired
    private SopControlService sopControlService;
    @Autowired
    private ItemCellService itemCellService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private WareHouseService wareHouseService;

    @Override
    public IPage<ItemContainerManage> listPage(QueryWrapper<ItemContainerManage> queryWrapper, Page<ItemContainerManage> page) {
        return baseMapper.listPage(queryWrapper, page);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void loadItemCell(ItemContainerLoadVO itemContainerLoadVO) {
        ItemContainerManageVO itemContainerManageVO = queryDetailByQrcode(itemContainerLoadVO.getContainerQrcode());
        //????????????
        String containerStatus = this.checkCondition(itemContainerLoadVO);
        itemContainerLoadVO.setContainerStatus(containerStatus);
        Map<String, ItemContainerManageDetail> containerItemCellMap = new HashMap<>(8);
        if (StringUtils.isBlank(itemContainerManageVO.getId())) {
            itemContainerLoadVO.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
            itemContainerLoadVO.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            baseMapper.insert(itemContainerLoadVO);
        } else {
            baseMapper.updateById(itemContainerLoadVO);
            List<ItemContainerManageDetail> itemContainerManageDetailList = itemContainerManageDetailMapper.selectByMainId(itemContainerLoadVO.getId());
            itemContainerManageDetailList.forEach(itemContainerManageDetail -> containerItemCellMap.put(itemContainerManageDetail.getItemCellQrcode(), itemContainerManageDetail));
        }
        //?????????????????????????????????
        List<String> ids = new ArrayList<>();
        itemContainerLoadVO.getItemCellList().forEach(itemContainerManageDetail -> ids.add(itemContainerManageDetail.getItemCellId()));
        List<ItemCell> itemCellList = new ArrayList<>();
        if (!CommonUtil.isEmptyOrNull(ids)) {
            itemCellList = itemCellMapper.selectBatchIds(ids);
        }
        Set<String> qrcodeSet = new HashSet<>();
        Set<String> oldQrcodeSet = new HashSet<>(containerItemCellMap.keySet());
        itemCellList.forEach(itemCell -> {
            //????????????????????????
            this.copyStorage(itemContainerLoadVO, itemCell);
            itemCellMapper.updateById(itemCell);
            //?????????????????????
            qrcodeSet.add(itemCell.getQrcode());
            //?????????????????????
            if (CommonUtil.isEmptyOrNull(oldQrcodeSet) || !oldQrcodeSet.contains(itemCell.getQrcode())) {
                //????????????
                ItemContainerManageDetail itemContainerManageDetail = new ItemContainerManageDetail();
                itemContainerManageDetail.setContainerManageId(itemContainerLoadVO.getId());
                itemContainerManageDetail.setContainerId(itemContainerLoadVO.getContainerId());
                itemContainerManageDetail.setContainerCode(itemContainerLoadVO.getContainerCode());
                itemContainerManageDetail.setContainerQrcode(itemContainerLoadVO.getContainerQrcode());
                itemContainerManageDetail.setItemId(itemCell.getItemId());
                itemContainerManageDetail.setItemName(itemCell.getItemName());
                itemContainerManageDetail.setItemCode(itemCell.getItemCode());
                itemContainerManageDetail.setItemCellId(itemCell.getId());
                itemContainerManageDetail.setItemCellQrcode(itemCell.getQrcode());
                itemContainerManageDetail.setQty(itemCell.getQty());
                itemContainerManageDetail.setSpec(itemCell.getSpec());
                itemContainerManageDetail.setUnitId(itemCell.getUnitId());
                itemContainerManageDetail.setUnitName(itemCell.getUnitName());
                //??????????????????
                ItemContainerLoadItemRecord itemContainerLoadItemRecord = new ItemContainerLoadItemRecord();
                BeanUtils.copyProperties(itemContainerManageDetail, itemContainerLoadItemRecord);
                itemContainerLoadItemRecord.setContainerName(itemContainerManageVO.getContainerName());
                itemContainerLoadItemRecord.setUnitId(itemCell.getUnitId());
                itemContainerLoadItemRecord.setUnitId(itemCell.getUnitName());
                itemContainerLoadItemRecord.setLoadType(ContainerLoadTypeEnum.LOAD.getValue());
                //???????????????
                itemContainerManageDetailMapper.insert(itemContainerManageDetail);
                itemContainerLoadItemRecordMapper.insert(itemContainerLoadItemRecord);
            }
        });
        oldQrcodeSet.addAll(qrcodeSet);
        oldQrcodeSet.removeAll(qrcodeSet);
        String[] ignoreProperty = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId".split(",");
        //????????????????????????
        if (!CommonUtil.isEmptyOrNull(oldQrcodeSet)) {
            oldQrcodeSet.forEach(qrcode -> {
                //??????????????????
                ItemContainerManageDetail itemContainerManageDetail = containerItemCellMap.get(qrcode);
                ItemContainerLoadItemRecord itemContainerLoadItemRecord = new ItemContainerLoadItemRecord();
                BeanUtils.copyProperties(itemContainerManageDetail, itemContainerLoadItemRecord, ignoreProperty);
                itemContainerLoadItemRecord.setContainerName(itemContainerManageVO.getContainerName());
                itemContainerLoadItemRecord.setLoadType(ContainerLoadTypeEnum.UNLOAD.getValue());
                //???????????????
                itemContainerManageDetailMapper.deleteById(itemContainerManageDetail.getId());
                itemContainerLoadItemRecordMapper.insert(itemContainerLoadItemRecord);
                //???????????????????????????
                QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
                itemCellQueryWrapper.eq("qrcode", qrcode);
                ItemCell itemCell = itemCellMapper.selectOne(itemCellQueryWrapper);
                itemCell.setFatherQrcode(null);
                itemCellMapper.updateById(itemCell);
            });
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param itemContainerManage
     * @param itemCell
     */
    private void copyStorage(ItemContainerManage itemContainerManage, ItemCell itemCell) {
        itemCell.setFatherQrcode(itemContainerManage.getContainerQrcode());
        itemCell.setStorageId(itemContainerManage.getStorageId());
        itemCell.setStorageName(itemContainerManage.getStorageName());
        itemCell.setStorageCode(itemContainerManage.getStorageCode());
        itemCell.setAreaCode(itemContainerManage.getAreaCode());
        itemCell.setAreaName(itemContainerManage.getAreaName());
        itemCell.setHouseCode(itemContainerManage.getHouseCode());
        itemCell.setHouseName(itemContainerManage.getHouseName());
        itemCell.setHopeInStorageCode(itemContainerManage.getHopeInWareHouseCode());
        itemCell.setHopeInStorageName(itemContainerManage.getHopeInWareHouseName());
        itemCell.setPositionStatus(itemContainerManage.getPositionStatus());
    }

    /**
     * ????????????
     *
     * @param itemContainerStorageVO
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void outStorage(ItemContainerStorageVO itemContainerStorageVO) {
        List<String> itemContainerManageList = itemContainerStorageVO.getItemContainerManageList();
        for (String manageId : itemContainerManageList) {
            ItemContainerManageVO itemContainerManageVO = queryDetailById(manageId);
            List<ItemContainerManageDetail> itemContainerManageDetailList = itemContainerManageDetailMapper.selectByMainId(manageId);
            //???????????????????????????
            if (!CommonUtil.isEmptyOrNull(itemContainerManageDetailList)) {
                List<String> itemCellIds = new ArrayList<>();
                itemContainerManageDetailList.forEach(itemContainerManageDetail -> itemCellIds.add(itemContainerManageDetail.getItemCellId()));
                List<ItemCell> itemCellList = itemCellMapper.selectBatchIds(itemCellIds);
                List<ItemCellOutStorageVO> itemCellOutStorageVOList = new ArrayList<>();
                itemCellList.forEach(itemCell -> {
                    ItemCellOutStorageVO itemCellOutStorageVO = new ItemCellOutStorageVO();
                    BeanUtils.copyProperties(itemCell, itemCellOutStorageVO);
                    itemCellOutStorageVO.setOutQty(itemCell.getQty());
                    itemCellOutStorageVOList.add(itemCellOutStorageVO);
                });
                ItemCellStorageVO itemCellStorageVO = new ItemCellStorageVO();
                BeanUtils.copyProperties(itemContainerStorageVO, itemCellStorageVO);
                itemCellStorageVO.setItemCellList(itemCellOutStorageVOList);
                itemCellService.labelCodeOutStorage(itemCellStorageVO);
            }
            //????????????
            ItemContainerTransferRecord itemContainerTransferRecord = this.setOutStorageRecord(itemContainerStorageVO, itemContainerManageVO);
            //????????????
            itemContainerManageVO.setStorageId("").setStorageCode("").setStorageName("")
                    .setAreaCode("").setAreaName("")
                    .setHouseCode("").setHouseName("")
                    .setHopeInWareHouseName(itemContainerStorageVO.getHopeInStorageName())
                    .setHopeInWareHouseCode(itemContainerStorageVO.getHopeInStorageCode());
            itemContainerManageVO.setPositionStatus(ItemCellPositionStatusEnum.TRANSPORT.getValue());
            baseMapper.updateById(itemContainerManageVO);
            itemContainerTransferRecordMapper.insert(itemContainerTransferRecord);
        }
    }


    /**
     * ????????????
     *
     * @param itemContainerStorageVO
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void inStorage(ItemContainerStorageVO itemContainerStorageVO) {
        for (String manageId : itemContainerStorageVO.getItemContainerManageList()) {
            ItemContainerManageVO itemContainerManageVO = queryDetailById(manageId);
            List<ItemContainerManageDetail> itemContainerManageDetailList = itemContainerManageDetailMapper.selectByMainId(manageId);
            //???????????????????????????
            if (!CommonUtil.isEmptyOrNull(itemContainerManageDetailList)) {
                List<String> itemCellIds = new ArrayList<>();
                itemContainerManageDetailList.forEach(itemContainerManageDetail -> itemCellIds.add(itemContainerManageDetail.getItemCellId()));
                List<ItemCell> itemCellList = itemCellMapper.selectBatchIds(itemCellIds);

                List<ItemCellOutStorageVO> itemCellOutStorageVOList = new ArrayList<>();
                itemCellList.forEach(itemCell -> {
                    ItemCellOutStorageVO itemCellOutStorageVO = new ItemCellOutStorageVO();
                    BeanUtils.copyProperties(itemCell, itemCellOutStorageVO);
                    itemCellOutStorageVO.setOutQty(itemCell.getQty());
                    itemCellOutStorageVOList.add(itemCellOutStorageVO);
                });

                ItemCellStorageVO itemCellStorageVO = new ItemCellStorageVO();
                BeanUtils.copyProperties(itemContainerStorageVO, itemCellStorageVO);
                itemCellStorageVO.setItemCellList(itemCellOutStorageVOList);
                itemCellService.labelCodeStorage(itemCellStorageVO);
            }
            //????????????
            this.fillItemContainer(itemContainerManageVO, itemContainerStorageVO.getHopeInStorageCode());
            itemContainerManageVO.setPositionStatus(ItemCellPositionStatusEnum.STORAGE.getValue());
            itemContainerManageVO.setQrcodeStatus(ItemCellQrcodeStatusEnum.FACTORY.getValue());
            baseMapper.updateById(itemContainerManageVO);
            //????????????
            ItemContainerTransferRecord itemContainerTransferRecord = this.setInStorageRecord(itemContainerStorageVO, itemContainerManageVO);
            itemContainerTransferRecordMapper.updateById(itemContainerTransferRecord);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param itemContainerManage
     * @param storageCode
     */
    private void fillItemContainer(ItemContainerManage itemContainerManage, String storageCode) {
        ItemCellFinishedStorageVO storageVO = wareHouseService.findFinishedStorageByStorageCode(storageCode);
        itemContainerManage.setStorageId(storageVO.getSecondStorageId());
        itemContainerManage.setStorageCode(storageVO.getSecondStorageCode());
        itemContainerManage.setStorageName(storageVO.getSecondStorageName());
        itemContainerManage.setAreaCode(storageVO.getFirstStorageCode());
        itemContainerManage.setAreaName(storageVO.getFirstStorageName());
        itemContainerManage.setHouseCode(storageVO.getWareHouseCode());
        itemContainerManage.setHouseName(storageVO.getWareHouseName());
    }

    /**
     * ????????????????????????
     *
     * @param itemContainerStorageVO
     * @param itemContainerManageVO
     */
    private ItemContainerTransferRecord setOutStorageRecord(ItemContainerStorageVO itemContainerStorageVO, ItemContainerManageVO itemContainerManageVO) {
        ItemContainerTransferRecord itemContainerTransferRecord = new ItemContainerTransferRecord();
        //??????????????????????????????????????????
        if (StringUtils.isNotEmpty(itemContainerStorageVO.getControlId())) {
            SopControl sopControl = sopControlService.getById(itemContainerStorageVO.getControlId());
            itemContainerTransferRecord.setSopControlId(itemContainerStorageVO.getControlId());
            itemContainerTransferRecord.setSopStepId(sopControl.getSopStepId());
            itemContainerTransferRecord.setTaskId(sopControl.getRelatedTaskId());
        }
        itemContainerTransferRecord.setContainerManageId(itemContainerManageVO.getId());
        itemContainerTransferRecord.setFatherQrcode(itemContainerManageVO.getFatherQrcode());
        itemContainerTransferRecord.setContainerQrcode(itemContainerManageVO.getContainerQrcode());
        itemContainerTransferRecord.setContainerCode(itemContainerManageVO.getContainerCode());
        itemContainerTransferRecord.setContainerName(itemContainerManageVO.getContainerName());
        itemContainerTransferRecord.setContainerStatus(itemContainerManageVO.getContainerStatus());

        itemContainerTransferRecord.setHopeInHouseName(itemContainerStorageVO.getHopeInStorageName());
        itemContainerTransferRecord.setHopeInHouseCode(itemContainerStorageVO.getHopeInStorageCode());
        itemContainerTransferRecord.setQualityStatus(itemContainerManageVO.getQcStatus());
        //??????????????????
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.TAG_STORAGE_EVENT_RELATE_SWITH);
        boolean isOpenEvent = StorageEventRelateSwitchEnum.OPEN_OUT_STORAGE.getValue().equals(bizConfig.getConfigValue());
        if (isOpenEvent) {
            itemContainerTransferRecord.setEventId(itemContainerStorageVO.getEventId());
            itemContainerTransferRecord.setEventName(itemContainerStorageVO.getEventName());
            itemContainerTransferRecord.setEventObject(itemContainerStorageVO.getEventObject());
            itemContainerTransferRecord.setBillCode(itemContainerStorageVO.getBillCode());
        }
        itemContainerTransferRecord.setTransferStatus(ItemTransferStatusEnum.OUT_STORAGED.getValue());
        itemContainerTransferRecord.setOutStorageCode(itemContainerManageVO.getStorageCode());
        itemContainerTransferRecord.setOutStorageName(itemContainerManageVO.getStorageName());
        itemContainerTransferRecord.setOutStorageEmployee(CommonUtil.getLoginUser().getRealname());
        itemContainerTransferRecord.setOutTime(new Date());
        itemContainerTransferRecord.setOutNote(itemContainerManageVO.getNote());
        return itemContainerTransferRecord;
    }

    /**
     * ????????????????????????
     *
     * @param itemContainerStorageVO
     * @param itemContainerManageVO
     */
    private ItemContainerTransferRecord setInStorageRecord(ItemContainerStorageVO itemContainerStorageVO, ItemContainerManageVO itemContainerManageVO) {
        QueryWrapper<ItemContainerTransferRecord> itemContainerTransferRecordQueryWrapper = new QueryWrapper<>();
        itemContainerTransferRecordQueryWrapper.eq("container_manage_id", itemContainerManageVO.getId());
        itemContainerTransferRecordQueryWrapper.eq("transfer_status", ItemTransferStatusEnum.OUT_STORAGED.getValue());
        ItemContainerTransferRecord itemContainerTransferRecord = itemContainerTransferRecordMapper.selectOne(itemContainerTransferRecordQueryWrapper);
        //???????????????sop????????????????????????
        if (StringUtils.isNotEmpty(itemContainerStorageVO.getControlId())) {
            SopControl sopControl = sopControlService.getById(itemContainerStorageVO.getControlId());
            itemContainerTransferRecord.setSopControlId(itemContainerStorageVO.getControlId());
            itemContainerTransferRecord.setSopStepId(sopControl.getSopStepId());
            itemContainerTransferRecord.setTaskId(sopControl.getRelatedTaskId());
        }
        itemContainerTransferRecord.setTransferStatus(ItemTransferStatusEnum.IN_STORAGED.getValue());
        itemContainerTransferRecord.setInStorageEmployee(CommonUtil.getLoginUser().getRealname());
        itemContainerTransferRecord.setInStorageName(itemContainerManageVO.getStorageName());
        itemContainerTransferRecord.setInStorageCode(itemContainerManageVO.getStorageCode());
        itemContainerTransferRecord.setInNote(itemContainerManageVO.getNote());
        itemContainerTransferRecord.setInTime(new Date());
        //???????????????????????????????????????
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.TAG_STORAGE_EVENT_RELATE_SWITH);
        if (StorageEventRelateSwitchEnum.OPEN_IN_STORAGE.getValue().equals(bizConfig.getConfigValue())) {
            itemContainerTransferRecord.setBillCode(itemContainerStorageVO.getBillCode());
            itemContainerTransferRecord.setEventId(itemContainerStorageVO.getEventId());
            itemContainerTransferRecord.setEventName(itemContainerStorageVO.getEventName());
            itemContainerTransferRecord.setEventObject(itemContainerStorageVO.getEventObject());
        }
        return itemContainerTransferRecord;
    }


    @Override
    public ItemContainerManageVO queryDetailById(String id) {
        ItemContainerManageVO itemContainerManageVO = new ItemContainerManageVO();
        ItemContainerManage itemContainerManage = baseMapper.selectById(id);
        BeanUtils.copyProperties(itemContainerManage, itemContainerManageVO);
        List<ItemContainerManageDetail> itemContainerManageDetailList = itemContainerManageDetailMapper.selectByMainId(id);
        Map<String, ItemContainerQty> itemContainerQtyMap = new HashMap<>(8);
        if (!CommonUtil.isEmptyOrNull(itemContainerManageDetailList)) {
            Map<String, List<ItemContainerManageDetail>> itemDetailMap = new HashMap<>(8);
            itemContainerManageDetailList.forEach(itemContainerManageDetail -> {
                String showKey = itemContainerManageDetail.getItemName() + "|" + itemContainerManageDetail.getItemCode();
                List<ItemContainerManageDetail> itemCategory = itemDetailMap.get(showKey);
                ItemContainerQty itemContainerQty = itemContainerQtyMap.get(showKey);
                if (CommonUtil.isEmptyOrNull(itemCategory)) {
                    itemCategory = new ArrayList<>();
                    itemCategory.add(itemContainerManageDetail);
                    itemDetailMap.put(showKey, itemCategory);
                } else {
                    itemCategory.add(itemContainerManageDetail);
                }
                if (itemContainerQty == null) {
                    itemContainerQty = new ItemContainerQty();
                    itemContainerQty.setItemCode(itemContainerManageDetail.getItemCode());
                    itemContainerQty.setItemName(itemContainerManageDetail.getItemName());
                    itemContainerQty.setItemId(itemContainerManageDetail.getItemId());
                    itemContainerQty.setQty(itemContainerManageDetail.getQty());
                    itemContainerQty.setUnit(itemContainerManageDetail.getUnitId());
                    itemContainerQty.setQty((BigDecimal) itemUnitConversion(itemContainerQty).values().toArray()[0]);
                    itemContainerQty.setUnit((String) itemUnitConversion(itemContainerQty).keySet().toArray()[0]);
                    itemContainerQtyMap.put(showKey, itemContainerQty);
                } else {
                    itemContainerQty.setQty(itemContainerQty.getQty().add(itemContainerManageDetail.getQty()));
                }
            });
            if (!CommonUtil.isEmptyOrNull(itemDetailMap.keySet())) {
                List<List<ItemContainerManageDetail>> itemDetailList = new ArrayList<>();
                itemDetailMap.keySet().forEach(key -> itemDetailList.add(itemDetailMap.get(key)));
                itemContainerManageVO.setItemDetailList(itemDetailList);
            }
            if (!CommonUtil.isEmptyOrNull(itemContainerQtyMap.keySet())) {
                itemContainerManageVO.setItemContainerQtyList(new ArrayList<>(itemContainerQtyMap.values()));
            }

        }
        List<ItemContainerTransferRecord> itemContainerTransferRecordList = itemContainerTransferRecordMapper.selectByMainId(id);
        itemContainerManageVO.setItemContainerTransferRecordList(itemContainerTransferRecordList);
        List<ItemContainerLoadItemRecord> itemContainerLoadItemRecordList = itemContainerLoadItemRecordMapper.selectByMainId(id);
        itemContainerManageVO.setItemContainerLoadItemRecordList(itemContainerLoadItemRecordList);
        return itemContainerManageVO;
    }


    @Override
    public ItemContainerManageVO queryDetailByQrcode(String qrcode) {
        QueryWrapper<ItemContainer> itemContainerQueryWrapper = new QueryWrapper<>();
        itemContainerQueryWrapper.eq("qrcode", qrcode);
        ItemContainer itemContainer = itemContainerService.getOne(itemContainerQueryWrapper);
        if (itemContainer == null) {
            throw new ILSBootException("???????????????????????????????????????");
        }
        if (!ZeroOrOneEnum.ONE.getStrCode().equals(itemContainer.getStatus())) {
            throw new ILSBootException("?????????????????????");
        }
        ItemContainerManageVO itemContainerManageVO = new ItemContainerManageVO();
        QueryWrapper<ItemContainerManage> itemContainerManageQueryWrapper = new QueryWrapper<>();
        itemContainerManageQueryWrapper.eq("container_qrcode", qrcode);
        ItemContainerManage itemContainerManage = baseMapper.selectOne(itemContainerManageQueryWrapper);
        if (itemContainerManage == null) {
            itemContainerManageVO.setContainerCode(itemContainer.getContainerCode());
            itemContainerManageVO.setContainerQrcode(itemContainer.getQrcode());
            itemContainerManageVO.setContainerName(itemContainer.getContainerName());
            itemContainerManageVO.setContainerId(itemContainer.getId());
            itemContainerManageVO.setContainerStatus(ContainerStatusEnum.EMPTY.getValue());
        } else {
            itemContainerManageVO = queryDetailById(itemContainerManage.getId());
        }
        return itemContainerManageVO;
    }

    /**
     * ????????????
     *
     * @param itemContainerLoadVO
     */
    private String checkCondition(ItemContainerLoadVO itemContainerLoadVO) {
        //???????????????????????????
        ItemContainerVO itemContainerVO = itemContainerService.selectById(itemContainerLoadVO.getContainerId());
        if (!ZeroOrOneEnum.ONE.getStrCode().equals(itemContainerVO.getStatus())) {
            throw new ILSBootException("?????????????????????");
        }
        if (itemContainerLoadVO.getPositionStatus() != null && !ItemCellPositionStatusEnum.STORAGE.getValue().equals(itemContainerLoadVO.getPositionStatus())) {
            throw new ILSBootException("??????????????????????????????????????????");
        }
        List<ItemContainerQtyVO> itemContainerQtyList = itemContainerVO.getItemContainerQtyList();
        //???????????????????????????
        List<ItemCell> itemCellList = new ArrayList<>();
        if (!CommonUtil.isEmptyOrNull(itemContainerLoadVO.getItemCellList())) {
            List<String> ids = new ArrayList<>();
            itemContainerLoadVO.getItemCellList().forEach(itemContainerManageDetail -> ids.add(itemContainerManageDetail.getItemCellId()));
            itemCellList = itemCellMapper.selectBatchIds(ids);
            itemCellList.forEach(itemCell -> {
                QueryWrapper<ItemContainerManageDetail> itemContainerManageDetailQueryWrapper = new QueryWrapper<>();
                itemContainerManageDetailQueryWrapper.in("item_cell_id", ids);
                itemContainerManageDetailQueryWrapper.ne("container_id", itemContainerVO.getId());
                List<ItemContainerManageDetail> itemContainerManageDetailList = itemContainerManageDetailMapper.selectList(itemContainerManageDetailQueryWrapper);
                if (!CommonUtil.isEmptyOrNull(itemContainerManageDetailList)) {
                    throw new ILSBootException("????????????????????????????????????????????????????????????");
                }
            });
        }
        //???????????????????????????????????????
        if (!CommonUtil.isEmptyOrNull(itemContainerQtyList)) {
            //?????????????????????????????????????????????????????????????????????
            Map<String, BigDecimal> itemQty = new HashMap<>(8);
            itemCellList.forEach(itemCell -> {
                if (!ItemCellPositionStatusEnum.STORAGE.getValue().equals(itemCell.getPositionStatus())) {
                    throw new ILSBootException("????????????????????????????????????????????????");
                }
                BigDecimal bigDecimal = itemQty.get(itemCell.getItemCode());
                if (null == bigDecimal) {
                    itemQty.put(itemCell.getItemCode(), itemCell.getQty());
                } else {
                    bigDecimal = bigDecimal.add(itemCell.getQty());
                    itemQty.put(itemCell.getItemCode(), bigDecimal);
                }
            });
            //?????????????????????????????????????????????????????????????????????
            Set<String> itemCodeSet = new HashSet<>(8);
            Set<String> itemFull = new HashSet<>(8);
            Set<String> itemAvailable = new HashSet<>(8);
            itemContainerQtyList.forEach(itemContainerQty -> {
                BigDecimal qty = itemQty.get(itemContainerQty.getItemCode());
                if (qty == null) {
                    qty = new BigDecimal(-1);
                }
                BigDecimal maxQty = (BigDecimal) itemUnitConversion(itemContainerQty).values().toArray()[0];

                int compare = qty.compareTo(maxQty);
                if (compare > 0) {
                    throw new ILSBootException("?????????????????????????????????");
                } else if (compare == 0) {
                    itemFull.add(itemContainerQty.getItemCode());
                } else {
                    itemAvailable.add(itemContainerQty.getItemCode());
                }
                itemCodeSet.add(itemContainerQty.getItemCode());

            });
            //???????????????????????????0
            Set<String> checkSet = new HashSet<>(itemQty.keySet());
            checkSet.addAll(itemCodeSet);
            checkSet.removeAll(itemCodeSet);
            if (checkSet.size() != 0) {
                throw new ILSBootException("?????????????????????????????????");
            }
            //??????????????????
            if (itemFull.size() == itemCodeSet.size()) {
                return ContainerStatusEnum.FULL.getValue();
            } else if (itemAvailable.size() > 0) {
                return ContainerStatusEnum.AVAILABLE.getValue();
            } else {
                return ContainerStatusEnum.EMPTY.getValue();
            }
        } else {
            if (CommonUtil.isEmptyOrNull(itemContainerVO.getItemContainerQtyList()) && !CommonUtil.isEmptyOrNull(itemContainerLoadVO.getItemCellList())) {
                return ContainerStatusEnum.AVAILABLE.getValue();
            }
            return ContainerStatusEnum.EMPTY.getValue();
        }
    }

    @Override
    public boolean isInContainer(String qrcode) {
        QueryWrapper<ItemContainerManageDetail> itemContainerManageDetailQueryWrapper = new QueryWrapper<>();
        itemContainerManageDetailQueryWrapper.eq("item_cell_qrcode", qrcode);
        List<ItemContainerManageDetail> itemContainerManageDetailList = itemContainerManageDetailMapper.selectList(itemContainerManageDetailQueryWrapper);
        return !CommonUtil.isEmptyOrNull(itemContainerManageDetailList);
    }

    /**
     * ?????????????????????
     *
     * @param itemContainerQty
     * @return
     */
    private Map<String, BigDecimal> itemUnitConversion(ItemContainerQty itemContainerQty) {
        Map<String, BigDecimal> data = new HashMap<>(1);
        BigDecimal maxQty = itemContainerQty.getQty();
        String mainUnitId = itemContainerQty.getUnit();
        // ??????????????????????????????????????????????????????
        List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(itemContainerQty.getItemId());
        ItemUnit itemContainerUnit = null;
        for (ItemUnit itemUnit : lstItemUnit) {
            if (itemUnit.getConvertUnit().equals(itemContainerQty.getUnit())) {
                itemContainerUnit = itemUnit;
            }
        }

        if (itemContainerUnit != null) {
            // ???????????????????????????????????????????????????
            maxQty = BigDecimalUtils.divide(BigDecimalUtils.multiply(itemContainerQty.getQty(), itemContainerUnit.getMainUnitQty()), itemContainerUnit.getConvertQty(), 6);
            mainUnitId = itemContainerUnit.getMainUnit();
        }
        data.put(mainUnitId, maxQty);

        return data;
    }
}
