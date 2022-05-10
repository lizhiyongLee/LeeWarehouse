package com.ils.modules.mes.material.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.vo.ItemContainerVO;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemContainerManage;
import com.ils.modules.mes.material.vo.ItemContainerLoadVO;
import com.ils.modules.mes.material.vo.ItemContainerManageVO;
import com.ils.modules.mes.material.vo.ItemContainerStorageVO;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 11:58
 */
public interface ItemContainerManageService extends IService<ItemContainerManage> {


    /**
     * 分页查询
     *
     * @param queryWrapper
     * @param page
     * @return
     */
    IPage<ItemContainerManage> listPage(QueryWrapper<ItemContainerManage> queryWrapper, Page<ItemContainerManage> page);

    /**
     * 物料单元装载/卸载
     *
     * @param itemContainerManageLoadVO
     */
    void loadItemCell(ItemContainerLoadVO itemContainerManageLoadVO);

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    ItemContainerManageVO queryDetailById(String id);

    /**
     * 载具出库
     *
     * @param itemContainerStorageVO
     */
    void outStorage(ItemContainerStorageVO itemContainerStorageVO);

    /**
     * 载具入库
     *
     * @param itemContainerStorageVO
     */
    void inStorage(ItemContainerStorageVO itemContainerStorageVO);

    /**
     * 根据qrcode查询详情
     *
     * @param qrcode
     * @return
     */
    ItemContainerManageVO queryDetailByQrcode(String qrcode);

    /**
     * 检查物料单元是否在容器中
     * @param qrcode
     * @return
     */
     boolean isInContainer(String qrcode);
}
