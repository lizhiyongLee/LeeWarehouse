package com.ils.modules.mes.material.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 物料单元
 * @Author: wyssss
 * @Date: 2020-11-17
 * @Version: V1.0
 */
public interface ItemCellMapper extends ILSMapper<ItemCell> {

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
    public IPage<ItemCellBatchVO> queryBatchList(@Param("itemId") String itemId, @Param("storageId") String storageId,
                                                 @Param("unit") String unit, Page<ItemCellBatchVO> page);

    /**
     * 查询系统中是否有该标签码
     *
     * @param qrCode
     * @return
     * @date 2021年1月7日
     */
    public Integer queryCountQrcodeInAllPlace(String qrCode);

    /**
     * 通过标签码查询物料单元
     *
     * @param qrcode
     * @return
     */
    public ItemCell queryByQrcode(String qrcode);

    /**
     * 根据位置Code查询出库时的该位置下的物料单元
     *
     * @param code
     * @return
     */
    public List<ItemCell> queryOutStocksItemCell(String code);

    /**
     * 标签码分页查询
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<QrCodeItemCellFollowVO> queryMaterialLabelFollow(Page<QrCodeItemCellFollowVO> page, @Param("ew") QueryWrapper<QrCodeItemCellFollowVO> queryWrapper);


    /**
     * 物料单元查询
     *
     * @param itemCellQueryWrapper
     * @param itemCellPage
     * @return
     */
    public IPage<ItemCell> listPage(@Param("ew") QueryWrapper<ItemCell> itemCellQueryWrapper, Page<ItemCell> itemCellPage);

    /**
     * 物料单元库存查询
     *
     * @param itemCellQueryWrapper
     * @param itemCellPage
     * @return
     */
    public IPage<ItemCell> itemStockQtyPage(@Param("ew") QueryWrapper<ItemCell> itemCellQueryWrapper, Page<ItemCell> itemCellPage);

    /**
     * 通过生产任务id查询物料
     *
     * @param id
     * @return
     */
    public List<ItemCell> queryItemCellByExecutionTask(@Param("id") String id);

    /**
     * 通过物料单元查询该物料的转移记录
     * @param id
     * @return
     */
    public List<TransferRecordVO> queryTansferRecordById(String id);

    /**
     * 查询质检信息详情
     * @param qrcode
     * @return
     */
    public List<QcInfoDetailVO> queryQcInfoDetailByQrcode(String qrcode);
    /**
     * 查询质检信息详情
     * @param batch
     * @return
     */
    public List<QcInfoDetailVO> queryQcInfoDetailByBatch(String batch);

    /**
     * 查询工序物料数量
     * @return
     */
    public List<ProcessMaterialQuantityVO> queryProcessItemQty();

    /**
     * 以物料为工序的统计物料数量看板
     * @return
     */
    public List<DashBoardProcessItemQtyVO> queryDashBoardProcessItemQtyVO();

    /**
     * 以工序id查询该工序下所有物料数量分布
     * @param processId
     * @return
     */
    public List<DashBoardDataVO> querDashBoardItemQty(String processId);
}
