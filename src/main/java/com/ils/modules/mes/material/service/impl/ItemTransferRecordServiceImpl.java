package com.ils.modules.mes.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.api.vo.Result;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.mapper.ItemMapper;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareHouseMapper;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.ItemManageWayEnum;
import com.ils.modules.mes.enums.ItemTransferStatusEnum;
import com.ils.modules.mes.enums.SopControlLogic;
import com.ils.modules.mes.enums.StorageEventRelateSwitchEnum;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import com.ils.modules.mes.material.mapper.ItemTransferRecordMapper;
import com.ils.modules.mes.material.service.ItemTransferRecordService;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.service.SopControlService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.BizConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 转移记录
 * @Author: wyssss
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Service
public class ItemTransferRecordServiceImpl extends ServiceImpl<ItemTransferRecordMapper, ItemTransferRecord> implements ItemTransferRecordService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private WareHouseMapper wareHouseMapper;

    @Autowired
    private WareStorageService wareStorageService;

    @Override
    public void saveItemTransferRecord(ItemTransferRecord itemTransferRecord) {
        baseMapper.insert(itemTransferRecord);
    }

    @Override
    public void updateItemTransferRecord(ItemTransferRecord itemTransferRecord) {
        baseMapper.updateById(itemTransferRecord);
    }

    @Override
    public void delItemTransferRecord(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchItemTransferRecord(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }


    @Override
    public void saveItemTransferRecordInOrOutStorage(ItemCell itemCell, String type) {
        Item item = itemMapper.selectById(itemCell.getItemId());
        ItemTransferRecord itemTransferRecord = new ItemTransferRecord();
        itemTransferRecord.setItemCellId(itemCell.getId());
        itemTransferRecord.setTransferStatus(type);
        itemTransferRecord.setQrcode(itemCell.getQrcode());
        itemTransferRecord.setBatch(itemCell.getBatch());
        itemTransferRecord.setItemId(itemCell.getItemId());
        itemTransferRecord.setItemCode(item.getItemCode());
        itemTransferRecord.setItemName(item.getItemName());
        itemTransferRecord.setQty(itemCell.getQty());
        itemTransferRecord.setUnitId(itemCell.getUnitId());
        itemTransferRecord.setUnitName(itemCell.getUnitName());
        itemTransferRecord.setProduceDate(itemCell.getProduceDate());
        itemTransferRecord.setValidDate(itemCell.getValidDate());
        itemTransferRecord.setQualityStatus(itemCell.getQcStatus());
        if (ItemTransferStatusEnum.OUT_STORAGED.getValue().equals(type)) {
            itemTransferRecord.setOutStorageName(itemCell.getStorageName());
            itemTransferRecord.setOutStorageCode(itemCell.getStorageCode());
            itemTransferRecord.setOutStorageEmployee(CommonUtil.getLoginUser().getUsername());
            itemTransferRecord.setInTime(new Date());
            itemTransferRecord.setInNote(itemCell.getNote());
        } else {
            itemTransferRecord.setInStorageName(itemCell.getStorageName());
            itemTransferRecord.setInStorageCode(itemCell.getStorageCode());
            itemTransferRecord.setInStorageEmployee(CommonUtil.getLoginUser().getUsername());
            itemTransferRecord.setOutTime(new Date());
            itemTransferRecord.setOutNote(itemCell.getNote());
        }
    }

    @Override
    public List<ItemTransferRecord> queryStocksOfInStorage(String wareHouseCode) {
        return baseMapper.queryInStocksItemTramTransferRecord(wareHouseCode);
    }

    @Autowired
    private SopControlService sopControlService;

    @Override
    public void saveItemCellOutStorageRecord(ItemCell itemCell, String eventId, String eventName, String eventObject
            , String hopeInStorageName, String hopeInStorageCode, String billCode, boolean isOpenEvent, String controlId) {
        ItemTransferRecord itemTransferRecord = new ItemTransferRecord();

        //查询并关联标准作业流程的信息
        if (StringUtils.isNotEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);
            itemTransferRecord.setSopControlId(controlId);
            itemTransferRecord.setSopStepId(sopControl.getSopStepId());
            itemTransferRecord.setTaskId(sopControl.getRelatedTaskId());
        }
        itemTransferRecord.setItemCellId(itemCell.getId());
        itemTransferRecord.setSpec(itemCell.getSpec());
        itemTransferRecord.setTransferStatus(ItemTransferStatusEnum.OUT_STORAGED.getValue());
        itemTransferRecord.setManageWay(itemCell.getManageWay());
        itemTransferRecord.setFatherQrcode(itemCell.getFatherQrcode());
        itemTransferRecord.setQrcode(itemCell.getQrcode());
        itemTransferRecord.setBatch(itemCell.getBatch());
        itemTransferRecord.setItemId(itemCell.getItemId());
        itemTransferRecord.setItemCode(itemCell.getItemCode());
        itemTransferRecord.setItemName(itemCell.getItemName());
        itemTransferRecord.setQty(itemCell.getQty());
        itemTransferRecord.setUnitId(itemCell.getUnitId());
        itemTransferRecord.setUnitName(itemCell.getUnitName());
        itemTransferRecord.setOutStorageCode(itemCell.getStorageCode());
        itemTransferRecord.setOutStorageName(itemCell.getStorageName());
        itemTransferRecord.setOutStorageEmployee(CommonUtil.getLoginUser().getRealname());
        itemTransferRecord.setOutTime(new Date());
        itemTransferRecord.setOutNote(itemCell.getNote());
        itemTransferRecord.setHopeInHouseName(hopeInStorageName);
        itemTransferRecord.setHopeInHouseCode(hopeInStorageCode);
        itemTransferRecord.setProduceDate(itemCell.getProduceDate());
        itemTransferRecord.setValidDate(itemCell.getValidDate());
        itemTransferRecord.setQualityStatus(itemCell.getQcStatus());
        if (isOpenEvent) {
            itemTransferRecord.setEventId(eventId);
            itemTransferRecord.setEventName(eventName);
            itemTransferRecord.setEventObject(eventObject);
            itemTransferRecord.setBillCode(billCode);
        }
        baseMapper.insert(itemTransferRecord);
    }

    @Override
    public void saveItemCellInStorageRecord(ItemCell itemCell, String eventId, String eventName, String eventObject, String billCode, String controlId) {
        QueryWrapper<ItemTransferRecord> itemTransferRecordQueryWrapper = new QueryWrapper<>();

        itemTransferRecordQueryWrapper.eq("item_cell_id", itemCell.getId());
        itemTransferRecordQueryWrapper.eq("transfer_status", ItemTransferStatusEnum.OUT_STORAGED.getValue());
        ItemTransferRecord itemTransferRecord = baseMapper.selectOne(itemTransferRecordQueryWrapper);
        //查询并设置sop标准事务控件数据
        if (StringUtils.isNotEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);
            itemTransferRecord.setSopControlId(controlId);
            itemTransferRecord.setSopStepId(sopControl.getSopStepId());
            itemTransferRecord.setTaskId(sopControl.getRelatedTaskId());
        }
        itemTransferRecord.setTransferStatus(ItemTransferStatusEnum.IN_STORAGED.getValue());
        itemTransferRecord.setInStorageEmployee(CommonUtil.getLoginUser().getRealname());
        itemTransferRecord.setInStorageName(itemCell.getStorageName());
        itemTransferRecord.setInStorageCode(itemCell.getStorageCode());
        itemTransferRecord.setInNote(itemCell.getNote());
        itemTransferRecord.setInTime(new Date());
        //校验标签码入库事务是否开启
        BizConfig bizConfig = CommonUtil.getBizConfig(MesCommonConstant.TAG_STORAGE_EVENT_RELATE_SWITH);
        if (StorageEventRelateSwitchEnum.OPEN_IN_STORAGE.getValue().equals(bizConfig.getConfigValue())) {
            itemTransferRecord.setBillCode(billCode);
            itemTransferRecord.setEventId(eventId);
            itemTransferRecord.setEventName(eventName);
            itemTransferRecord.setEventObject(eventObject);
        }
        baseMapper.updateById(itemTransferRecord);
    }

    @Override
    public List<ItemTransferRecord> queryRecordByStorageCode(String wareStorageCode, String controlId, String storageId){
        if (StringUtils.isBlank(wareStorageCode) && StringUtils.isBlank(storageId)) {
            return null;
        }
        String sopControlLogic = null;
        String sopItemId = null;
        if (!StringUtils.isEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);
            sopControlLogic = sopControl.getControlLogic();
            sopItemId = sopControl.getEntityItem();
        }
        if (StringUtils.isNoneBlank(sopControlLogic) && SopControlLogic.CONTROL.getValue().equals(sopControlLogic)) {
            WareHouse wareHouse = wareHouseMapper.queryByStorageId(storageId);
            //通过sop模板查询对应的物料单元
            QueryWrapper<ItemTransferRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("transfer_status", ItemTransferStatusEnum.OUT_STORAGED.getValue());
            queryWrapper.in("manage_way", ItemManageWayEnum.NONE_MANAGE.getValue(), ItemManageWayEnum.BATCH_MANAGE.getValue());
            queryWrapper.eq("item_id", sopItemId);
            queryWrapper.and(item -> item.eq("hope_in_house_code", wareHouse.getHouseCode()).or().isNotNull("hope_in_house_code").or().eq("hope_in_house_code", ""));
            return baseMapper.selectList(queryWrapper);
        } else {
            WareHouse wareHouse = wareHouseMapper.queryByStorageCode(wareStorageCode);
            String wareHouseCode=null;
            if(null!=wareHouse){
                wareHouseCode=wareHouse.getHouseCode();
            }
            return this.queryStocksOfInStorage(wareHouseCode);
        }
    }
}
