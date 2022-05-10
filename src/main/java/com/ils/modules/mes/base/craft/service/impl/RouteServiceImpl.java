package com.ils.modules.mes.base.craft.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ils.modules.mes.base.craft.service.RouteLineParaHeadService;
import com.ils.modules.mes.base.craft.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.Route;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.entity.RouteLineMethod;
import com.ils.modules.mes.base.craft.entity.RouteLineStation;
import com.ils.modules.mes.base.craft.mapper.RouteLineMapper;
import com.ils.modules.mes.base.craft.mapper.RouteLineMethodMapper;
import com.ils.modules.mes.base.craft.mapper.RouteLineStationMapper;
import com.ils.modules.mes.base.craft.mapper.RouteMapper;
import com.ils.modules.mes.base.craft.service.RouteService;
import com.ils.modules.mes.constants.MesCommonConstant;

/**
 * @Description: 工艺路线主表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements RouteService {

    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RouteLineMapper routeLineMapper;
    @Autowired
    private RouteLineStationMapper routeLineStationMapper;
    @Autowired
    private RouteLineMethodMapper routeLineMethodMapper;
    @Autowired
    private RouteLineParaHeadService routeLineParaHeadService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Route saveMain(RouteVO routeVO) {

        this.checkCondition(routeVO);

        // 工艺主信息
        Route route = new Route();
        BeanUtils.copyProperties(routeVO, route);
        routeMapper.insert(route);
        routeVO.setId(route.getId());

        // 工艺路线
        List<RouteLineVO> orgRouteLineVOList = routeVO.getRouteLineList();
        if (null == orgRouteLineVOList || orgRouteLineVOList.size() == 0) {
            return route;
        }

        for (RouteLineVO routeLineVO : orgRouteLineVOList) {
            //外键设置
            routeLineVO.setRouteId(route.getId());
            routeLineMapper.insert(routeLineVO);
            //工位设置
            List<RouteLineStationVO> routeLineStationList = routeLineVO.getRouteLineStationList();
            for (RouteLineStation routeLineStation : routeLineStationList) {
                // 外键设置
                routeLineStation.setRouteId(route.getId());
                routeLineStation.setRouteLineId(routeLineVO.getId());
                routeLineStationMapper.insert(routeLineStation);
            }
            //质检方案
            List<RouteLineMethodVO> routeLineMethodList = routeLineVO.getRouteLineMethodList();
            for (RouteLineMethod routeLineMethod : routeLineMethodList) {
                // 外键设置
                routeLineMethod.setRouteId(route.getId());
                routeLineMethod.setRouteLineId(routeLineVO.getId());
                routeLineMethodMapper.insert(routeLineMethod);
            }
            //参数模板
            List<RouteLineParaVO> routeLineParaVOList = routeLineVO.getRouteLineParaVOList();
            if (!CommonUtil.isEmptyOrNull(routeLineParaVOList)) {
                routeLineParaVOList.forEach(routeLineParaVO -> {
                    routeLineParaVO.setRouteId(route.getId());
                    routeLineParaVO.setRouteLineId(routeLineVO.getId());
                    routeLineParaVO.setId(null);
                    routeLineParaVO.setCreateBy(null);
                    routeLineParaVO.setCreateTime(null);
                    routeLineParaHeadService.saveRouteLinePara(routeLineParaVO);
                });
            }
        }
        this.setProcessLink(orgRouteLineVOList);

        return route;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(RouteVO routeVO) {

        this.checkCondition(routeVO);

        Route route = new Route();
        BeanUtils.copyProperties(routeVO, route);
        routeMapper.updateById(route);

        //1.先删除子表数据
        routeLineMapper.deleteByMainId(route.getId());
        routeLineStationMapper.deleteByRouteId(route.getId());
        routeLineMethodMapper.deleteByRouteId(route.getId());
        routeLineParaHeadService.deleteByRouteId(route.getId());

        // 工艺路线
        List<RouteLineVO> routeLineVOList = routeVO.getRouteLineList();
        for (RouteLineVO routeLineVO : routeLineVOList) {
            // 外键设置
            routeLineVO.setRouteId(route.getId());
            routeLineMapper.insert(routeLineVO);
            // 工位设置
            List<RouteLineStationVO> routeLineStationList = routeLineVO.getRouteLineStationList();
            if (routeLineStationList != null) {
                for (RouteLineStation routeLineStation : routeLineStationList) {
                    // 外键设置
                    routeLineStation.setRouteId(route.getId());
                    routeLineStation.setRouteLineId(routeLineVO.getId());

                    routeLineStationMapper.insert(routeLineStation);
                }
            }
            //质检方案
            List<RouteLineMethodVO> routeLineMethodList = routeLineVO.getRouteLineMethodList();
            if (routeLineMethodList != null) {
                for (RouteLineMethod routeLineMethod : routeLineMethodList) {
                    // 外键设置
                    routeLineMethod.setId(null);
                    routeLineMethod.setRouteId(route.getId());
                    routeLineMethod.setRouteLineId(routeLineVO.getId());
                    routeLineMethodMapper.insert(routeLineMethod);
                }
            }
            //参数模板
            List<RouteLineParaVO> routeLineParaVOList = routeLineVO.getRouteLineParaVOList();
            if (!CommonUtil.isEmptyOrNull(routeLineParaVOList)) {
                routeLineParaVOList.forEach(routeLineParaVO -> {
                    routeLineParaVO.setRouteId(route.getId());
                    routeLineParaVO.setRouteLineId(routeLineVO.getId());
                    routeLineParaHeadService.saveRouteLinePara(routeLineParaVO);
                });
            }
        }
        this.setProcessLink(routeLineVOList);
    }

    /**
     * 校验条件
     *
     * @param routeVO
     * @date 2020年12月4日
     */
    private void checkCondition(RouteVO routeVO) {
        // 编码不重复
        QueryWrapper<Route> queryWrapper = new QueryWrapper();
        queryWrapper.eq("route_code", routeVO.getRouteCode());
        if (StringUtils.isNoneBlank(routeVO.getId())) {
            queryWrapper.ne("id", routeVO.getId());
        }
        Route obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }

    }

    /**
     * 设置工序的前后顺序
     *
     * @param routeLineVOList
     * @return List<RouteLineVO>
     * @date 2020年11月3日
     */
    private void setProcessLink(List<RouteLineVO> orgRouteLineVOList) {
        List<RouteLineVO> routeLineVOList =
                orgRouteLineVOList.stream().sorted(Comparator.comparing(RouteLineVO::getSeq)).collect(Collectors.toList());

        RouteLineVO preNode = routeLineVOList.get(0);
        for (int i = 1; i < routeLineVOList.size(); i++) {
            RouteLineVO temp = routeLineVOList.get(i);
            temp.setPriorCode(preNode.getId());
            preNode.setNextCode(temp.getId());
            preNode = temp;
        }
        routeLineVOList.get(0).setPriorCode(MesCommonConstant.ROUTE_PROCESS_FIRST);
        routeLineVOList.get(routeLineVOList.size() - 1).setNextCode(MesCommonConstant.ROUTE_PROCESS_END);
        // 更新到数据库中
        QueryWrapper<RouteLine> queryWrapper = new QueryWrapper<>();
        RouteLine updateRouteLine = null;
        for (RouteLine routeLine : routeLineVOList) {
            queryWrapper.clear();
            queryWrapper.eq("id", routeLine.getId());
            updateRouteLine = new RouteLine();
            updateRouteLine.setPriorCode(routeLine.getPriorCode());
            updateRouteLine.setNextCode(routeLine.getNextCode());
            routeLineMapper.update(updateRouteLine, queryWrapper);
        }
    }

}
