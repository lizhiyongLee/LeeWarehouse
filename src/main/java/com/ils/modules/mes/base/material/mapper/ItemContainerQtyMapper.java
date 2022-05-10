package com.ils.modules.mes.base.material.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.material.entity.ItemContainerQty;
import com.ils.modules.mes.base.material.vo.ItemQualityVO;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 9:46
 */
public interface ItemContainerQtyMapper extends ILSMapper<ItemContainerQty> {

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
    public List<ItemContainerQty> selectByMainId(String mainId);
}
