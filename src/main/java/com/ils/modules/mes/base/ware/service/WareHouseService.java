package com.ils.modules.mes.base.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;
import com.ils.modules.mes.base.ware.vo.WareHouseVO;
import com.ils.modules.mes.base.ware.vo.WareStorageVO;

import java.util.List;

/**
 * @Description: 仓库定义
 * @Author: Tian
 * @Date:   2020-11-05
 * @Version: V1.0
 */
public interface WareHouseService extends IService<WareHouse> {

    /**
     * 添加
     * @param wareHouseVO
     */
    public void saveWareHouse(WareHouseVO wareHouseVO) ;
    
    /**
     * 修改
     * @param wareHouseVO
     */
    public void updateWareHouse(WareHouseVO wareHouseVO);
    
    /**
     * 删除
     * @param id
     */
    public void delWareHouse (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWareHouse (List<String> idList);

    /**
     * 
     * 根据工位查询完工线边仓
     * 
     * @param stationId
     * @return ItemCellFinishedStorageVO
     */
    public ItemCellFinishedStorageVO findFinishedStorageByStationId(String stationId);

    /**
     * 分页查询仓库
     * @param page
     * @return
     */
    public IPage<WareStorageVO> queryWareHouseList(Page<WareStorageVO> page);

    /**
     * 根据仓位编码查询完工线边仓
     * @param storageCode
     * @return ItemCellFinishedStorageVO
     */
    public ItemCellFinishedStorageVO findFinishedStorageByStorageCode(String storageCode);

    /**
     * 查询仓库
     * @return
     */
    public List<DictModel> queryWareHouseList();

    /**
     * 查询仓库code
     * @return
     */
    public List<WareHouse> queryWareHouse();

    /**
     * 通过二级仓位编码查询仓库
     * @param storageCode
     * @return
     */
    public WareHouse queryByStorageCode(String storageCode);
}
