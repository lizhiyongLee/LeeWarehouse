package com.ils.modules.mes.base.ware.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.vo.ProduceWareStorageVO;
import com.ils.modules.mes.base.ware.vo.WareStorageTreeVO;
import com.ils.modules.mes.base.ware.vo.WareStorageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 仓位定义
 * @Author: Tian
 * @Date: 2020-11-06
 * @Version: V1.0
 */
public interface WareStorageMapper extends ILSMapper<WareStorage> {

    /**
     * 通过上级仓位id查询对应的数据
     *
     * @param upstorageId
     * @return
     */
    List<WareStorageVO> selectByUpStorageId(String upstorageId);

    /**
     * 查询仓位集合
     *
     * @return
     */
    List<WareStorage> selectStorageTreeList();

    /**
     * 查询仓位树形结构By车间Id
     *
     * @param workShopId
     * @return
     */
    List<WareStorageTreeVO> selectStorageByWorkShopIdTreeVO(String workShopId);

    /**
     * 查询投料仓以及物料可用数量
     *
     * @param areaId
     * @param itemId
     * @param page
     * @return
     * @date 2020年12月16日
     */
    IPage<ProduceWareStorageVO> selectStorageByStationId(@Param("areaId") String areaId,
                                                         @Param("itemId") String itemId,
                                                         Page<ProduceWareStorageVO> page);

    /**
     * 分页查询仓库
     *
     * @param page
     * @return
     */
    IPage<WareStorageVO> queryWareHouseList(Page<WareStorageVO> page);


    /**
     * 根据工位查询线边仓数量
     *
     * @param areaId
     * @param storageId
     * @return
     * @date 2020年12月29日
     */
    int queryWareHouseCount(@Param("areaId") String areaId, @Param("storageId") String storageId);

    /**
     * 查询三级结构的仓库和仓位
     *
     * @param queryWrapper
     * @return
     */
    List<WareStorageTreeVO> queryReceivingGoodsTreeStorage(@Param("ew") QueryWrapper queryWrapper);


}
