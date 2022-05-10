package com.ils.modules.mes.base.craft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.mapper.RouteLineMapper;
import com.ils.modules.mes.base.craft.service.RouteLineService;
import com.ils.modules.mes.base.craft.vo.RouteLineVO;

/**
 * @Description: 工艺路线明细表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Service
public class RouteLineServiceImpl extends ServiceImpl<RouteLineMapper, RouteLine> implements RouteLineService {
	
	@Autowired
	private RouteLineMapper routeLineMapper;
	
	@Override
    public List<RouteLineVO> selectByMainId(String mainId) {
		return routeLineMapper.selectByMainId(mainId);
	}
}
