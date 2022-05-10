package com.ils.modules.mes.base.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.vo.*;
import com.ils.modules.mes.util.TreeNode;

import java.util.List;

/**
 * @Description: 仓位定义
 * @Author: Tian
 * @Date: 2020-11-06
 * @Version: V1.0
 */
public interface WareStorageService extends IService<WareStorage> {

    /**
     * 添加
     *
     * @param wareStorageVO
     */
    public void saveWareStorage(WareStorageVO wareStorageVO);

    /**
     * 修改
     *
     * @param wareStorageVO
     */
    public void updateWareStorage(WareStorageVO wareStorageVO);

    /**
     * 查询子节点集合
     *
     * @param upStorageId
     * @return
     */
    public List<WareStorageVO> selcetChildList(String upStorageId);

    /**
     * 查询仓位树形结构集合
     *
     * @return
     */
    public List<TreeNode> queryWareStorageTreeList();

    /**
     * 查询仓位树形结构
     *
     * @param workShopId
     * @return
     */
    public List<TreeNode> queryTreeStorage(String workShopId);

    /**
     * 通过区域id分页插叙子仓位
     *
     * @param areaId
     * @param page
     * @param page
     * @param itemId
     * @return
     */

    public IPage<ProduceWareStorageVO> selectStorageByStationId(String areaId, String itemId,
                                                                Page<ProduceWareStorageVO> page);

    /**
     * 根据工位查询线边仓数量
     *
     * @param areaId
     * @param storageId
     * @return
     * @date 2020年12月29日
     */
    public int queryWareHouseCount(String areaId, String storageId);

    /**
     * 查询收获仓位
     *
     * @param status
     * @param name
     * @return
     */
    public List<TreeNode> queryReceivingGoodsTreeStorage(String name, String status);

    /**
     * 获取最底层仓位
     *
     * @param positionNameList
     * @param wareStorageTreeVOList
     * @return
     */
    List<String> getStorageNameList(List<String> positionNameList, List<WareStorageTreeVO> wareStorageTreeVOList);

    /**
     * 通过仓库id查询子仓位集合
     *
     * @param wareHouseId
     * @return
     */
    public List<WareStorageListVO> queryWareStorageByWareHouseId(String wareHouseId);


}
