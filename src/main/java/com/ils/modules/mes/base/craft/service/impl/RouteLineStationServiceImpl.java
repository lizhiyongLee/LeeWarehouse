package com.ils.modules.mes.base.craft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.RouteLineStation;
import com.ils.modules.mes.base.craft.mapper.RouteLineStationMapper;
import com.ils.modules.mes.base.craft.service.RouteLineStationService;
import com.ils.modules.mes.base.craft.vo.RouteLineStationVO;

/**
 * @Description: 工序关联工位
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Service
public class RouteLineStationServiceImpl extends ServiceImpl<RouteLineStationMapper, RouteLineStation> implements RouteLineStationService {
	
	@Autowired
	private RouteLineStationMapper routeLineStationMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteByRouteId(String routeId) {
        return routeLineStationMapper.deleteByRouteId(routeId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteByRouteLineId(String routeLineId) {
        return routeLineStationMapper.deleteByRouteLineId(routeLineId);
    }

    @Override
    public List<RouteLineStationVO> selectByRouteId(String routeId) {
        return routeLineStationMapper.selectByRouteId(routeId);
    }

    @Override
    public List<RouteLineStationVO> selectByRouteLineId(String routeLineId) {
        return routeLineStationMapper.selectByRouteLineId(routeLineId);
    }
	

}
