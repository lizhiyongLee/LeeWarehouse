package com.ils.modules.mes.material.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemContainerManageDetail;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/26 14:33
 */
public interface ItemContainerManageDetailMapper extends ILSMapper<ItemContainerManageDetail> {
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
    public List<ItemContainerManageDetail> selectByMainId(String mainId);
}
