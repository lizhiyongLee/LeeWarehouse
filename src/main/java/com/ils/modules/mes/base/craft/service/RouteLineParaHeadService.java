package com.ils.modules.mes.base.craft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.RouteLineParaHead;
import com.ils.modules.mes.base.craft.vo.RouteLineParaVO;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 14:42
 */
public interface RouteLineParaHeadService extends IService<RouteLineParaHead> {
    /**
     * 保存数据
     * @param routeLineParaVO
     */
    void saveRouteLinePara(RouteLineParaVO routeLineParaVO);

    /**
     * 更新数据
     * @param routeLineParaVO
     */
    void updateRouteLinePara(RouteLineParaVO routeLineParaVO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    RouteLineParaVO queryById(String id);

    /**
     * 根据工艺路线行id查询
     * @param routeId
     * @return
     */
    List<RouteLineParaVO> queryByRouteId(String routeId);

    /**
     * 根据工艺路线行id删除
     * @param routeId
     */
    void deleteByRouteId(String routeId);
}
