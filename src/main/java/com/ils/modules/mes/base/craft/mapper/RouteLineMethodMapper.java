package com.ils.modules.mes.base.craft.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.RouteLineMethod;
import com.ils.modules.mes.base.craft.vo.RouteLineMethodVO;

/**
 * @Description: 工序关联质检方案
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
public interface RouteLineMethodMapper extends ILSMapper<RouteLineMethod> {
    
    /**
     * 通过工艺主表 routeId 删除
     * 
     * @param routeId
     * @return
     */
    public boolean deleteByRouteId(String routeId);
    
    /**
     * 通过工艺路线 routeLineId 删除
     * 
     * @param routeLineId
     * @return
     */
    public boolean deleteByRouteLineId(String routeLineId);

    /**
     * 通过工艺主表 routeId 查询
     * 
     * @param routeId
     * @return
     */
    public List<RouteLineMethodVO> selectByRouteId(String routeId);

    /**
     * 通过工艺路线 routeLineId 查询
     * @param routeLineId
     * @param methodType
     * @return
     */
    public List<RouteLineMethodVO> selectByRouteLineId(String routeLineId,String methodType);

}
