package com.ils.modules.mes.base.craft.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.base.craft.entity.*;
import com.ils.modules.mes.base.craft.service.*;
import com.ils.modules.mes.base.craft.vo.*;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.util.CommonUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 工艺路线主表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/craft/route")
@Api(tags = "工艺路线主表")
@Slf4j
public class RouteController extends ILSController<Route, RouteService> {
    @Autowired
    private RouteService routeService;
    @Autowired
    private RouteLineService routeLineService;
    @Autowired
    private RouteLineStationService routeLineStationService;
    @Autowired
    private RouteLineMethodService routeLineMethodService;
    @Autowired
    private RouteLineParaHeadService routeLineParaHeadService;

    /**
     * 分页列表查询
     *
     * @param route
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "工艺路线主表-分页列表查询", notes = "工艺路线主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Route route,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Route> queryWrapper = QueryGenerator.initQueryWrapper(route, req.getParameterMap());
        Page<Route> page = new Page<Route>(pageNo, pageSize);
        IPage<Route> pageList = routeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param routeVO
     * @return
     */
    @ApiOperation(value = "工艺路线主表-添加", notes = "工艺路线主表-添加")
    @PostMapping(value = "/add")
    @AutoLog(value = "工艺路线主表-添加", operateType = CommonConstant.OPERATE_TYPE_ADD)
    public Result<?> add(@RequestBody RouteVO routeVO) {
        routeService.saveMain(routeVO);
        return Result.ok(routeVO);
    }

    /**
     * 编辑
     *
     * @param routeVO
     * @return
     */
    @ApiOperation(value = "工艺路线主表-编辑", notes = "工艺路线主表-编辑")
    @PostMapping(value = "/edit")
    @AutoLog("工艺路线主表-编辑")
    public Result<?> edit(@RequestBody RouteVO routeVO) {
        routeService.updateMain(routeVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工艺路线主表-通过id查询", notes = "工艺路线主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Route route = routeService.getById(id);
        RouteVO routeVO = new RouteVO();
        BeanUtils.copyProperties(route, routeVO);
        List<RouteLineVO> routeLineList = routeLineService.selectByMainId(id);
        routeVO.setRouteLineList(routeLineList);

        List<RouteLineStationVO> routeLineStationList = routeLineStationService.selectByRouteId(id);
        Map<String, List<RouteLineStationVO>> routeLineStationMap =
                routeLineStationList.stream().collect(Collectors.groupingBy(RouteLineStationVO::getRouteLineId));

        List<RouteLineMethodVO> routeLineMethodList = routeLineMethodService.selectByRouteId(id);
        Map<String, List<RouteLineMethodVO>> routeLineMethodMap =
                routeLineMethodList.stream().collect(Collectors.groupingBy(RouteLineMethodVO::getRouteLineId));

        List<RouteLineParaVO> routeLineParaVOList = routeLineParaHeadService.queryByRouteId(id);
        Map<String, List<RouteLineParaVO>> routeLineParaMap =
                routeLineParaVOList.stream().collect(Collectors.groupingBy(RouteLineParaVO::getRouteLineId));

        for (RouteLineVO routeLineVO : routeLineList) {
            if (routeLineStationMap.containsKey(routeLineVO.getId())) {
                routeLineVO.setRouteLineStationList(routeLineStationMap.get(routeLineVO.getId()));
            }
            if (routeLineMethodMap.containsKey(routeLineVO.getId())) {
                routeLineVO.setRouteLineMethodList(routeLineMethodMap.get(routeLineVO.getId()));
            }
            if (routeLineParaMap.containsKey(routeLineVO.getId())) {
                routeLineVO.setRouteLineParaVOList(routeLineParaMap.get(routeLineVO.getId()));
            }
        }

        return Result.ok(routeVO);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工艺路线主表-通过id查询", notes = "工艺路线主表-通过id查询")
    @GetMapping(value = "/queryRouteLineById")
    public Result<?> queryRouteLineById(@RequestParam(name = "id", required = true) String id) {
        Route route = routeService.getById(id);
        RouteVO routeVO = new RouteVO();
        BeanUtils.copyProperties(route, routeVO);
        List<RouteLineVO> routeLineList = routeLineService.selectByMainId(id);
        routeVO.setRouteLineList(routeLineList);
        return Result.ok(routeVO);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param route
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Route route) {
        // Step.1 组装查询条件
        QueryWrapper<Route> queryWrapper = QueryGenerator.initQueryWrapper(route, request.getParameterMap());

        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<RouteVO> pageList = new ArrayList<RouteVO>();
        List<Route> routeList = routeService.list(queryWrapper);

        List<String> idList = new ArrayList<>();
        routeList.forEach(temp -> idList.add(temp.getId()));

        //2.1 取出符合条件的所有数据
        putList(pageList, routeList, idList);

        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "工艺路线主表");
        mv.addObject(NormalExcelConstants.CLASS, RouteVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("工艺路线主表数据", "导出人:" + sysUser.getRealname(), "工艺路线主表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    private void putList(List<RouteVO> pageList, List<Route> routeList, List<String> idList) {
        QueryWrapper<RouteLine> routeLineQueryWrapper = new QueryWrapper<>();
        routeLineQueryWrapper.in("route_id", idList);
        List<RouteLine> routeLineList = routeLineService.list(routeLineQueryWrapper);

        QueryWrapper<RouteLineStation> routeLineStationQueryWrapper = new QueryWrapper<>();
        routeLineQueryWrapper.in("route_id", idList);
        List<RouteLineStation> routeLineStationList = routeLineStationService.list(routeLineStationQueryWrapper);

        QueryWrapper<RouteLineMethod> routeLineMethodQueryWrapper = new QueryWrapper<>();
        routeLineQueryWrapper.in("route_id", idList);
        List<RouteLineMethod> routeLineMethodList = routeLineMethodService.list(routeLineMethodQueryWrapper);

        //2.2 数据分类
        for (Route temp : routeList) {
            RouteVO vo = new RouteVO();
            BeanUtils.copyProperties(temp, vo);

            List<RouteLineVO> tempRouteLineList = new ArrayList<>();
            routeLineList.forEach(routeLine -> {
                if (temp.getId().equals(routeLine.getRouteId())) {
                    RouteLineVO routeLineVO = new RouteLineVO();
                    BeanUtils.copyProperties(routeLine, routeLineVO);
                    tempRouteLineList.add(routeLineVO);
                }
            });
            vo.setRouteLineList(tempRouteLineList);

            List<RouteLineStationVO> tempRouteLineStationList = new ArrayList<>();
            routeLineStationList.forEach(routeLineStation -> {
                if (temp.getId().equals(routeLineStation.getRouteId())) {
                    RouteLineStationVO routeLineStationVO = new RouteLineStationVO();
                    BeanUtils.copyProperties(routeLineStation, routeLineStationVO);
                    tempRouteLineStationList.add(routeLineStationVO);
                }
            });
            Map<String, List<RouteLineStationVO>> routeLineStationMap =
                    tempRouteLineStationList.stream().collect(Collectors.groupingBy(RouteLineStationVO::getRouteLineId));

            List<RouteLineMethodVO> tempRouteLineMethodList = new ArrayList<>();
            routeLineMethodList.forEach(routeLineMethod -> {
                if (temp.getId().equals(routeLineMethod.getRouteId())) {
                    RouteLineMethodVO routeLineMethodVO = new RouteLineMethodVO();
                    BeanUtils.copyProperties(routeLineMethod, routeLineMethodVO);
                    tempRouteLineMethodList.add(routeLineMethodVO);
                }
            });
            Map<String, List<RouteLineMethodVO>> routeLineMethodMap =
                    tempRouteLineMethodList.stream().collect(Collectors.groupingBy(RouteLineMethodVO::getRouteLineId));

            for (RouteLineVO routeLineVO : tempRouteLineList) {
                if (routeLineStationMap.containsKey(routeLineVO.getId())) {
                    routeLineVO.setRouteLineStationList(routeLineStationMap.get(routeLineVO.getId()));
                }
                if (routeLineMethodMap.containsKey(routeLineVO.getId())) {
                    routeLineVO.setRouteLineMethodList(routeLineMethodMap.get(routeLineVO.getId()));
                }
            }
            pageList.add(vo);
        }
    }


}
