package com.ils.modules.mes.base.craft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.Route;
import com.ils.modules.mes.base.craft.vo.RouteVO;

/**
 * @Description: 工艺路线主表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
public interface RouteService extends IService<Route> {

    /**
     * 
     * 保存工艺路线
     * 
     * @param routeVO
     * @return Route
     * @date 2020年11月2日
     */
    public Route saveMain(RouteVO routeVO);
	
	/**
     * 
     * 修改工艺路线
     * 
     * @param routeVO
     * @date 2020年11月5日
     */
    public void updateMain(RouteVO routeVO);
	
	
}
