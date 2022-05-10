package com.ils.modules.mes.base.craft.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.ProcessParaHead;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/15 17:11
 */
public interface ProcessParaHeadMapper extends ILSMapper<ProcessParaHead> {
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);
}
