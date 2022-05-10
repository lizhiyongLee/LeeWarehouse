package com.ils.modules.mes.material.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.vo.*;


import java.util.List;

/**
 * @Description: 物料单元
 * @Author: wyssss
 * @Date: 2020-11-17
 * @Version: V1.0
 */
public interface ItemCellService extends IService<ItemCell> {

    /**
     * 添加
     *
     * @param itemCell
     */
    public void saveItemCell(ItemCell itemCell);

    /**
     * 修改
     *
     * @param itemCell
     */
    public void updateItemCell(ItemCell itemCell);

    /**
     * 删除
     *
     * @param id
     */
    public void delItemCell(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchItemCell(List<String> idList);

    /**
     * 查询批次
     *
     * @param itemId
     * @param storageId
     * @param unit
     * @param page
     * @return
     * @date 2020年12月29日
     */
    public IPage<ItemCellBatchVO> queryBatchList(String itemId, String storageId, String unit,
                                                 Page<ItemCellBatchVO> page);

    /**
     * 无码收货
     *
     * @param itemCell
     */
    public void saveNoCodeItemCell(ItemCell itemCell);

    /**
     * 批次码收货
     *
     * @param itemCell
     */
    public void saveBatchCodeItemCell(ItemCell itemCell);

    /**
     * 标签码收货
     *
     * @param itemCell
     */
    public void saveQrcodeItemCell(ItemCell itemCell);

    /**
     * 库存入库
     *
     * @param itemTransferRecordVO
     */
    public void stocksOfInStorage(ItemTransferRecordVO itemTransferRecordVO);

    /**
     * 标签码入库
     *
     * @param itemCellStorageVO
     */
    public void labelCodeStorage(ItemCellStorageVO itemCellStorageVO);

    /**
     * 标签码出库
     *
     * @param itemCellStorageVO
     */
    public void labelCodeOutStorage(ItemCellStorageVO itemCellStorageVO);

    /**
     * 物料单元-入库时通过标签码查询物料单元
     *
     * @param qrcode
     * @param storageCode
     * @return
     */
    public ItemCell queryItemCellByQrCodeInStorage(String qrcode, String storageCode);

    /**
     * 物料单元-chuku时通过标签码查询物料单元
     *
     * @param qrcode
     * @return
     */
    public ItemCell queryItemCellByQrCodeOutStorage(String qrcode);

    /**
     * 库存出库
     *
     * @param itemCellStorageVO
     */
    public void stocksOfOutStorage(ItemCellStorageVO itemCellStorageVO);

    /**
     * 查询本仓库出库的物料
     *
     * @param deliveryLocation
     * @return
     */
    public List<ItemCell> queryStocksOfOutStorage(String deliveryLocation);

    /**
     * 标签码物料单元查询
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<QrCodeItemCellFollowVO> queryMaterialLabelFollow(Page<QrCodeItemCellFollowVO> page, QueryWrapper<QrCodeItemCellFollowVO> queryWrapper);

    /**
     * 标签码物料追溯
     *
     * @param id
     * @return
     */
    public TraceItemCellQrcodeVO traceItemCellWithQrcode(String id);

    /**
     * 物料单元批次码追溯
     *
     * @param itemId
     * @return
     */
    public TraceItemCellQrcodeVO batchItemCellTraceById(String itemId);

    /**
     * 物料单元分页查询
     *
     * @param queryWrapper
     * @param page
     * @return
     */
    public IPage<ItemCell> listPage(QueryWrapper<ItemCell> queryWrapper, Page<ItemCell> page);

    /**
     * 物料单元库存分页查询
     *
     * @param queryWrapper
     * @param page
     * @return
     */
    public IPage<ItemCell> listPageStorage(QueryWrapper<ItemCell> queryWrapper, Page<ItemCell> page);

    /**
     * 普通发货-标签码发货
     *
     * @param itemCellList
     */
    public void labelCodeDeliveryGoods(List<ItemCell> itemCellList);

    /**
     * 普通发货-库存发货
     *
     * @param itemCellList
     */
    public void stocksOfDeliveryGoods(List<ItemCellOutStorageVO> itemCellList);

    /**
     * RF标签码物料追溯
     * @param qrcode
     * @return
     */
    public RfTraceRecordVO rfQrcodeItemCellTrace(String qrcode);


    /**
     * RF端批次码物料追溯
     * @param batch
     * @param itemCellId
     * @return
     */
    public RfTraceRecordVO rfBatchItemCellTrace(String batch,String itemCellId);

    /**
     * 通过当前工序的id查询不同物料的数量数据
     * @param processId
     * @return
     */
    public List<DashBoardDataVO> queryNowProcessItemQty(String processId);


    /**
     * 查询在制品物料的占比
     * @return
     */
    public JSONArray queryAccountedForItemProcessed();


    /**
     * 以工序为维度，查询每个工序下面的物料总数量
     * @return
     */
    public List<ProcessMaterialQuantityVO> queryProcessedItemQty();

    /**
     * 以物料为工序的统计物料数量看板
     * @return
     */
    public List<DashBoardProcessItemQtyVO> selectDashBoardProcessItemQtyVO();
}
