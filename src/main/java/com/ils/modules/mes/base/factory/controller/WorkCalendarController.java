package com.ils.modules.mes.base.factory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.base.factory.entity.WorkCalendar;
import com.ils.modules.mes.base.factory.service.WorkCalendarService;
import com.ils.modules.mes.base.factory.vo.WorkCalendarParamsVO;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;
import com.ils.modules.mes.enums.WorkCalendarTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 工作日历
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工作日历")
@RestController
@RequestMapping("/base/factory/workCalendar")
public class WorkCalendarController extends ILSController<WorkCalendar, WorkCalendarService> {
	@Autowired
	private WorkCalendarService workCalendarService;
	
	/**
	 * 分页列表查询
	 *
	 * @param workCalendar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value = "工作日历-列表查询", notes = "工作日历-列表查询")
	@GetMapping(value = "/list")
    public Result<?> queryList(
								   HttpServletRequest req) {
        QueryWrapper<WorkCalendarVO> queryWrapper = new QueryWrapper<WorkCalendarVO>();
        String type = req.getParameter("type");
        queryWrapper.eq("a.is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.eq("a.type", type);
        String year = req.getParameter("year");
        String month = req.getParameter("month");
        String stationId = req.getParameter("stationId");
        queryWrapper.eq("DATE_FORMAT(workdate,'%Y-%m')", year + "-" + month);
        if (WorkCalendarTypeEnum.PRODUCTION.getValue().equals(type)) {
            queryWrapper.eq("a.station_id", stationId);
        }
        List<WorkCalendarVO> pageList = workCalendarService.queryWorkCalendarList(queryWrapper);
		return Result.ok(pageList);
	}
	


    /**
     * 批量添加
     *
     * @param workCalendarParamsVO
     * @return
     */
    @ApiOperation(value = "工作日历-批量添加", notes = "工作日历-批量添加")
    @PostMapping(value = "/add")
    @AutoLog("工作日历-批量添加")
    public Result<?> add(@RequestBody WorkCalendarParamsVO workCalendarParamsVO) {
        workCalendarService.addBatch(workCalendarParamsVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 查询单条设置数据
     *
     * @param workCalendarParamsVO
     * @return
     */
    @ApiOperation(value = "工作日历-查询单条设置数据", notes = "工作日历-查询单条设置数据")
    @PostMapping(value = "/querySingleInitData")
    public Result<?> querySingleInitData(@RequestBody WorkCalendarParamsVO workCalendarParamsVO) {
        QueryWrapper<WorkCalendarParamsVO> queryWrapper = new QueryWrapper();

        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.eq("type", workCalendarParamsVO.getType());
        queryWrapper.eq("workdate", workCalendarParamsVO.getWorkDate());
        if (StringUtils.isNotBlank(workCalendarParamsVO.getStationIds())) {
            List<String> ids = Arrays.asList(workCalendarParamsVO.getStationIds().split(","));
            queryWrapper.in("station_id", ids);
        }

        List<WorkCalendarParamsVO> lstWorkCalendarParamsVO = workCalendarService.querySingleInitData(queryWrapper,CommonUtil.getTenantId());
        if (CommonUtil.isEmptyOrNull(lstWorkCalendarParamsVO)) {
            return Result.ok(new ArrayList<>());
        } else {
            return Result.ok(lstWorkCalendarParamsVO.get(0));
        }

    }
	
	/**
     * 单个添加
     *
     * @param workCalendar
     * @return
     */
    @ApiOperation(value = "工作日历-单个添加", notes = "工作日历-单个添加")
    @PostMapping(value = "/singleAdd")
    @AutoLog("工作日历-单个添加")
    public Result<?> singleAdd(@RequestBody WorkCalendarParamsVO workCalendarParamsVO) {
        workCalendarService.saveWorkCalendar(workCalendarParamsVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 添加
     *
     * @param workCalendar
     * @return
     */
    @ApiOperation(value = "工作日历-单条重置", notes = "工作日历-单条重置")
    @PostMapping(value = "/singleReset")
    public Result<?> singleReset(@RequestBody WorkCalendarParamsVO workCalendarParamsVO) {
        workCalendarService.singleReset(workCalendarParamsVO);
        return Result.ok(workCalendarParamsVO);
    }



}
