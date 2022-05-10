package com.ils.modules.mes.base.ware.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;

import java.util.List;

/**
 * @Description: 仓库定义
 * @Author: Tian
 * @Date:   2020-11-05
 * @Version: V1.0
 */
public interface WareHouseMapper extends ILSMapper<WareHouse> {

    /**
     * 查询全部仓库
     * @return
     */
    public List<WareHouse> selectAll();

    /**
     * 查询跟车间 关联的仓位线边仓
     * @param id
     * @return
     */
    public String selectHasFeelingStorage(String id);

    /**
     * 
     * 根据工位查询线边仓
     * 
     * @param stationId
     * @return ItemCellFinishedStorageVO
     */
    public ItemCellFinishedStorageVO selectStorageByStationId(String stationId);


    /**
     *
     * 根据工位查询线边仓
     *
     * @param storageCode
     * @return ItemCellFinishedStorageVO
     */
    public ItemCellFinishedStorageVO selectStorageByStorageCode(String storageCode);

    /**
     * 查询仓库
     * @return
     */
    public List<DictModel> queryWareHouseList();

    /**
     * 通过二级仓位编码查询仓库
     * @param storageCode
     * @return
     */
    public WareHouse queryByStorageCode(String storageCode);

    /**
     * 通过仓位id查询仓库
     * @param storageId
     * @return
     */
    public WareHouse queryByStorageId(String storageId);

    /**
     * 通过区域类型和区域id查询对应线边仓数量
     * @param id
     * @param type
     * @return
     */
    public Integer countWorkShopRelatedWareHouseByAreaId(String id,String type);
}
