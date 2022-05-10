package com.ils.modules.mes.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: 转移记录
 * @Author: wyssss
 * @Date: 2020-11-18
 * @Version: V1.0
 */
public interface ItemTransferRecordService extends IService<ItemTransferRecord> {

    /**
     * 添加
     *
     * @param itemTransferRecord
     */
    public void saveItemTransferRecord(ItemTransferRecord itemTransferRecord);

    /**
     * 修改
     *
     * @param itemTransferRecord
     */
    public void updateItemTransferRecord(ItemTransferRecord itemTransferRecord);

    /**
     * 删除
     *
     * @param id
     */
    public void delItemTransferRecord(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchItemTransferRecord(List<String> idList);

    /**
     * 保存出入库物料转运记录
     *
     * @param itemCell
     * @param type
     */
    public void saveItemTransferRecordInOrOutStorage(ItemCell itemCell, String type);

    /**
     * 查询入库时的物料记录
     *
     * @param wareHouseCode
     * @return
     */
    public List<ItemTransferRecord> queryStocksOfInStorage(String wareHouseCode);

    /**
     * 出库时生成出库记录
     *
     * @param itemCell
     * @param eventId
     * @param eventName
     * @param eventObject
     * @param hopeInStorageName
     * @param hopeInStorageCode
     * @param billCode
     * @param isOpenEvent
     * @param controlId
     */
    public void saveItemCellOutStorageRecord(ItemCell itemCell, String eventId, String eventName, String eventObject
            , String hopeInStorageName, String hopeInStorageCode, String billCode, boolean isOpenEvent, String controlId);

    /**
     * 入库时生产入库记录
     *
     * @param itemCell
     * @param eventId
     * @param eventName
     * @param eventObject
     * @param billCode
     * @param controlId
     */
    public void saveItemCellInStorageRecord(ItemCell itemCell, String eventId, String eventName, String eventObject, String billCode, String controlId);

    /**
     * 入库时通过选择的仓位code查询物料转移记录
     *
     * @param wareStorageCode
     * @param controlId
     * @param storageId
     * @return
     */
    List<ItemTransferRecord> queryRecordByStorageCode(String wareStorageCode, String controlId, String storageId);
}
