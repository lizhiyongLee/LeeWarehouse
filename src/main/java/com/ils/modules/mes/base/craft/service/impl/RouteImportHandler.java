package com.ils.modules.mes.base.craft.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.*;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.service.*;
import com.ils.modules.mes.base.craft.vo.*;
import com.ils.modules.mes.base.qc.entity.QcItemType;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.vo.QcMethodVO;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.system.service.AbstractExcelHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 工艺路线的导入类
 *
 * @author Anna.
 * @date 2021/6/21 19:06
 */
@Component
public class RouteImportHandler extends AbstractExcelHandler {

    @Autowired
    private RouteService routeService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private ProcessQcMethodService processQcMethodService;
    @Autowired
    private ProcessStationService processStationService;


    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        Map<String, RouteVO> mapRouteVO = new HashMap<>(16);
        Map<String,String> processCodeIdMap = new HashMap<>();

        //查询已存在的工序集合
        QueryWrapper<Process> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<Process> existProcessTypeList = processService.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(existProcessTypeList)) {
            existProcessTypeList.forEach(process -> processCodeIdMap.put(process.getProcessCode(),process.getId()));
        }

        //根据唯一属性建立获取主表
        Set<String> routeCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            routeCodeList.add((String) data.get("routeCode"));
        }

        routeCodeList.forEach(code -> {
            RouteVO routeVO = new RouteVO();
            routeVO.setRouteLineList(new ArrayList<>());
            mapRouteVO.put(code, routeVO);
        });

        //赋值
        for (Map<String, Object> data : dataList) {
            RouteVO routeVO = mapRouteVO.get((String) data.get("routeCode"));
            Route route = BeanUtil.toBeanIgnoreError(data, Route.class);
            BeanUtil.copyProperties(route, routeVO);
            RouteLine routeLine = BeanUtil.toBeanIgnoreError(data, RouteLine.class);
            if (!processCodeIdMap.containsKey(routeLine.getProcessCode())) {
                throw new ILSBootException("未查询到该工序");
            }else {
                routeLine.setProcessId(processCodeIdMap.get(routeLine.getProcessCode()));
            }
            RouteLineVO routeLineVO = new RouteLineVO();
            BeanUtil.copyProperties(routeLine, routeLineVO);
            this.setDetail(routeLine.getProcessId(), routeLineVO);
            routeVO.getRouteLineList().add(routeLineVO);
            RouteLineVO routeLineVO2 = new RouteLineVO();
        }
        mapRouteVO.values().forEach(vo -> routeService.saveMain(vo));
    }

    private void setDetail(String processId, RouteLineVO routeLineVO) {
        routeLineVO.setRouteLineMethodList(new ArrayList<>());
        routeLineVO.setRouteLineStationList(new ArrayList<>());
        List<ProcessQcMethodVO> processQcMethodList = processQcMethodService.selectByMainId(processId);
        processQcMethodList.forEach(processQcMethodVO -> {
            RouteLineMethodVO routeLineMethodVO = new RouteLineMethodVO();
            BeanUtil.copyProperties(processQcMethodVO, routeLineMethodVO);
            routeLineVO.getRouteLineMethodList().add(routeLineMethodVO);
        });

        QueryWrapper<ProcessStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        List<ProcessStation> lstStation = processStationService.list(queryWrapper);
        lstStation.forEach(station -> {
            RouteLineStationVO routeLineStationVO = new RouteLineStationVO();
            BeanUtil.copyProperties(station, routeLineStationVO);
            routeLineVO.getRouteLineStationList().add(routeLineStationVO);
        });
    }
}
