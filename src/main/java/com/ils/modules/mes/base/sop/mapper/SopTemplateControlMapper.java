package com.ils.modules.mes.base.sop.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.sop.entity.SopTemplateControl;

import java.util.List;

/**
 * @Description: SOP模板步骤对应控件
 * @Author: Tian
 * @Date: 2021-07-15
 * @Version: V1.0
 */
public interface SopTemplateControlMapper extends ILSMapper<SopTemplateControl> {
    /**
     * 通过sop模板主表id删除
     *
     * @param mainId
     */
    void deletedByMainId(String mainId);

    /**
     * 通过sop模板主表id查询
     *
     * @param mainId
     * @return List<SopTemplateControl>
     */
    List<SopTemplateControl> selectByMainId(String mainId);
}
