package com.ils.modules.mes.base.machine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.machine.vo.SparePartsStoargeNodeTreeVO;
import com.ils.modules.mes.base.machine.vo.SparePartsStorageVO;

import java.util.List;

/**
 * @Description: 备件库位
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface SparePartsStorageService extends IService<SparePartsStorage> {

    /**
     * 添加
     * @param sparePartsStorage
     */
    public void saveSparePartsStorage(SparePartsStorage sparePartsStorage) ;
    
    /**
     * 修改
     * @param sparePartsStorage
     */
    public void updateSparePartsStorage(SparePartsStorage sparePartsStorage);

    /**
     * 分页查询仓库
     * @param page
     * @return
     */
    public IPage<SparePartsStorageVO> querySparePartsHouse(Page<SparePartsStorageVO> page);

    /**
     * 查询子节点集合
     * @param upStorageId
     * @return
     */
    public List<SparePartsStorageVO> selcetChildList(String upStorageId);

    /**
     * 查询备件仓位树形结构集合
     * @return
     */
    public List<SparePartsStoargeNodeTreeVO> queryWareStorageTreeList();
}
