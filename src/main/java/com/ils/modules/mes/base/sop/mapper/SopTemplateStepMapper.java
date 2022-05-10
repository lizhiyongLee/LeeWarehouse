package com.ils.modules.mes.base.sop.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.sop.entity.SopTemplateStep;

import java.util.List;

/**
 * @Description: SOP模板步骤
 * @Author: Tian
 * @Date: 2021-07-15
 * @Version: V1.0
 */
public interface SopTemplateStepMapper extends ILSMapper<SopTemplateStep> {
    /**
     * 通过主表id删除
     *
     * @param mainId
     */
    void deleteByMainId(String mainId);

    /**
     * 通过主表id查询
     *
     * @param mainId
     * @return List<SopTemplateStep>
     */
    List<SopTemplateStep> selectByMainId(String mainId);
}
