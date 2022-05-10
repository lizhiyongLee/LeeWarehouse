package com.ils.modules.mes.material.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemContainerTransferRecord;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/27 14:30
 */
public interface ItemContainerTransferRecordMapper extends ILSMapper<ItemContainerTransferRecord> {
    /**
     * 通过主表 Id 查询
     *
     * @param mainId
     * @return
     */
    public List<ItemContainerTransferRecord> selectByMainId(String mainId);
}
