package com.ils.modules.mes.material.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemContainerManage;
import org.apache.ibatis.annotations.Param;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 11:32
 */
public interface ItemContainerManageMapper extends ILSMapper<ItemContainerManage> {

    /**
     * 分页查询
     *
     * @param itemContainerManageQueryWrapper
     * @param itemCellPage
     * @return
     */
    public IPage<ItemContainerManage> listPage(@Param("ew") QueryWrapper<ItemContainerManage> itemContainerManageQueryWrapper, Page<ItemContainerManage> itemCellPage);
}
