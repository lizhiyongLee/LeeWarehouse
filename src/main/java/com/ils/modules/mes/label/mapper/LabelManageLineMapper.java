package com.ils.modules.mes.label.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.label.entity.LabelManageLine;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:53
 */
public interface LabelManageLineMapper extends ILSMapper<LabelManageLine> {

    /**
     * 通过主表 Id 查询
     *
     * @param mainId
     * @return
     */
    List<LabelManageLine> selectByMainId(String mainId);

    /**
     * 通过主表 Id 删除
     *
     * @param mainId
     * @return
     */
    void deleteByMainId(String mainId);
}
