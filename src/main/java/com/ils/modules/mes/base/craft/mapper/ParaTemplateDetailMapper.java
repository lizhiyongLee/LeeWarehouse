package com.ils.modules.mes.base.craft.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.ParaTemplateDetail;

/**
 * @Description: 参数模板明细表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
public interface ParaTemplateDetailMapper extends ILSMapper<ParaTemplateDetail> {

    Integer delByMainId(String mainId);
}
