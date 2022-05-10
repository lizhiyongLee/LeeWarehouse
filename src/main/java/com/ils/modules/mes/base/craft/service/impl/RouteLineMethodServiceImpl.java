package com.ils.modules.mes.base.craft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.RouteLineMethod;
import com.ils.modules.mes.base.craft.mapper.RouteLineMethodMapper;
import com.ils.modules.mes.base.craft.service.RouteLineMethodService;
import com.ils.modules.mes.base.craft.vo.RouteLineMethodVO;

/**
 * @Description: 工序关联质检方案
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Service
public class RouteLineMethodServiceImpl extends ServiceImpl<RouteLineMethodMapper, RouteLineMethod> implements RouteLineMethodService {
	
	@Autowired
	private RouteLineMethodMapper routeLineMethodMapper;
	
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteByRouteId(String routeId) {
        return routeLineMethodMapper.deleteByRouteId(routeId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteByRouteLineId(String routeLineId) {
        return routeLineMethodMapper.deleteByRouteLineId(routeLineId);
    }

    @Override
    public List<RouteLineMethodVO> selectByRouteId(String routeId) {
        return routeLineMethodMapper.selectByRouteId(routeId);
    }

}
