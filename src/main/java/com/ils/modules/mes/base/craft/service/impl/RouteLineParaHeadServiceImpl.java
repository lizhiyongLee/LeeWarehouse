package com.ils.modules.mes.base.craft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.RouteLineParaDetail;
import com.ils.modules.mes.base.craft.entity.RouteLineParaHead;
import com.ils.modules.mes.base.craft.mapper.RouteLineParaDetailMapper;
import com.ils.modules.mes.base.craft.mapper.RouteLineParaHeadMapper;
import com.ils.modules.mes.base.craft.service.RouteLineParaHeadService;
import com.ils.modules.mes.base.craft.vo.RouteLineParaVO;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 参数模板主表
 * @Author: Conner
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Service
public class RouteLineParaHeadServiceImpl extends ServiceImpl<RouteLineParaHeadMapper, RouteLineParaHead> implements RouteLineParaHeadService {

    @Autowired
    private RouteLineParaDetailMapper routeLineParaDetailMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveRouteLinePara(RouteLineParaVO routeLineParaVO) {
        //保存主表
        baseMapper.insert(routeLineParaVO);
        List<RouteLineParaDetail> routeLineParaDetailList = routeLineParaVO.getRouteLineParaDetailList();
        //保存从表
        if (!CommonUtil.isEmptyOrNull(routeLineParaDetailList)) {
            routeLineParaDetailList.forEach(routeLineParaDetail -> {
                routeLineParaDetail.setParaHeadId(routeLineParaVO.getId());
                routeLineParaDetail.setId(null);
                routeLineParaDetail.setCreateBy(null);
                routeLineParaDetail.setCreateTime(null);
                routeLineParaDetailMapper.insert(routeLineParaDetail);
            });
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateRouteLinePara(RouteLineParaVO routeLineParaVO) {
        baseMapper.updateById(routeLineParaVO);
        //删除从表
        routeLineParaDetailMapper.deleteByMainId(routeLineParaVO.getId());

        //重新插入从表
        List<RouteLineParaDetail> routeLineParaDetailList = routeLineParaVO.getRouteLineParaDetailList();
        routeLineParaDetailList.forEach(routeLineParaDetail -> routeLineParaDetailMapper.insert(routeLineParaDetail));
    }

    @Override
    public RouteLineParaVO queryById(String id) {
        RouteLineParaVO routeLineParaVO = new RouteLineParaVO();
        //查询主表
        RouteLineParaHead routeLineParaHead = baseMapper.selectById(id);
        BeanUtils.copyProperties(routeLineParaHead, routeLineParaVO);
        //查询从表
        QueryWrapper<RouteLineParaDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("para_head_id", id);
        List<RouteLineParaDetail> routeLineParaDetailList = routeLineParaDetailMapper.selectList(queryWrapper);

        routeLineParaVO.setRouteLineParaDetailList(routeLineParaDetailList);
        return routeLineParaVO;
    }

    @Override
    public List<RouteLineParaVO> queryByRouteId(String routeId) {
        List<RouteLineParaVO> routeLineParaVOList = new ArrayList<>();
        //查询主表
        QueryWrapper<RouteLineParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("route_id", routeId);
        List<RouteLineParaHead> processParaHeadList = baseMapper.selectList(headQueryWrapper);
        processParaHeadList.forEach(processParaHead -> {
            RouteLineParaVO routeLineParaVO = new RouteLineParaVO();
            BeanUtils.copyProperties(processParaHead, routeLineParaVO);
            //查询从表
            QueryWrapper<RouteLineParaDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("para_head_id", processParaHead.getId());
            List<RouteLineParaDetail> routeLineParaDetailList = routeLineParaDetailMapper.selectList(detailQueryWrapper);

            routeLineParaVO.setRouteLineParaDetailList(routeLineParaDetailList);
            routeLineParaVOList.add(routeLineParaVO);
        });
        return routeLineParaVOList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteByRouteId(String routeId) {
        List<String> ids = new ArrayList<>();
        //查询主表
        QueryWrapper<RouteLineParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("route_id", routeId);
        List<RouteLineParaHead> routeLineParaHeadList = baseMapper.selectList(headQueryWrapper);
        //删除主从表
        routeLineParaHeadList.forEach(processParaHead -> ids.add(processParaHead.getId()));
        baseMapper.deleteByMainId(routeId);
        if (!CommonUtil.isEmptyOrNull(ids)) {
            ids.forEach(id -> routeLineParaDetailMapper.selectByMainId(id));
        }
    }
}
