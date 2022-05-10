package com.ils.modules.mes.base.craft.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.RouteLineParaHead;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/15 17:21
 */
public interface RouteLineParaHeadMapper extends ILSMapper<RouteLineParaHead> {
    /**
     * 通过主表 Id 删除
     * @param routeId
     * @return
     */
    public boolean deleteByMainId(String routeId);
}
