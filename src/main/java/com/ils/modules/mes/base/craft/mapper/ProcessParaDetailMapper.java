package com.ils.modules.mes.base.craft.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.ProcessParaDetail;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/15 17:09
 */
public interface ProcessParaDetailMapper extends ILSMapper<ProcessParaDetail> {
    /**
     * 通过主表 Id 删除
     *
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);

    /**
     * 通过主表 Id 查询
     *
     * @param mainId
     * @return
     */
    public List<ProcessParaDetail> selectByMainId(String mainId);
}
