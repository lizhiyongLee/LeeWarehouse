package com.ils.modules.mes.base.factory.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;

import java.util.List;

/**
 * @Description: 报告模板控件
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface ReportTemplateControlMapper extends ILSMapper<ReportTemplateControl> {

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
    public List<ReportTemplateControl> selectByMainId(String mainId);

}
