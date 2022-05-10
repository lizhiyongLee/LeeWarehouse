package com.ils.modules.mes.material.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemContainerLoadItemRecord;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/26 14:33
 */
public interface ItemContainerLoadItemRecordMapper extends ILSMapper<ItemContainerLoadItemRecord> {
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);

    /**
     * 通过主表 Id 查询
     * @param mainId
     * @return
     */
    public List<ItemContainerLoadItemRecord> selectByMainId(String mainId);
}
