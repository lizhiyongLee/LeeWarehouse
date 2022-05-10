package com.ils.modules.mes.machine.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.enums.MachineTaskTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.*;
import com.ils.modules.mes.machine.mapper.MachineDataMapper;
import com.ils.modules.mes.machine.mapper.MachineMapper;
import com.ils.modules.mes.machine.mapper.MachinePolicyMapper;
import com.ils.modules.mes.machine.service.*;
import com.ils.modules.mes.machine.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 设备管理
 * @Author: Tian
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "设备管理")
@RestController
@RequestMapping("/machine/machine")
public class MachineController extends ILSController<Machine, MachineService> {
    @Autowired
    private MachineService machineService;
    @Autowired
    private MachineStopTimeService machineStopTimeService;
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private MachineMaintenanceTaskService machineMaintenanceTaskService;
    @Autowired
    private MachineRepairTaskService machineRepairTaskService;
    @Autowired
    private MachineMapper machineMapper;
    @Autowired
    private MachineLogService machineLogService;
    @Autowired
    private MachineDataMapper machineDataMapper;
    @Autowired
    private MachinePolicyMapper machinePolicyMapper;

    /**
     * 分页列表查询
     *
     * @param machine
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "设备管理-分页列表查询", notes = "设备管理-分页列表查询")
    @GetMapping(value = "/list")
    @AutoLog(value = "设备管理-分页列表查询")
    public Result<?> listQueryPage(Machine machine,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Machine> queryWrapper = QueryGenerator.initQueryWrapper(machine, req.getParameterMap());

        //查询有相应任务的设备
        String machineTask = req.getParameter("taskType");
        if (StringUtils.isNotBlank(machineTask)) {
            List<String> ids = new ArrayList<>(16);
            if (MachineTaskTypeEnum.REPAIR_TASKS.getValue().equals(machineTask)) {
                ids = machineRepairTaskService.queryRepairMachineTask();
            } else if (MachineTaskTypeEnum.MAINTENANCE_TASKS.getValue().equals(machineTask)) {
                ids = machineMaintenanceTaskService.queryWaitMaintenanMachine("2");
            } else if (MachineTaskTypeEnum.CHECK_TASKS.getValue().equals(machineTask)) {
                ids = machineMaintenanceTaskService.queryWaitMaintenanMachine("1");
            }
            queryWrapper.in("id", CollectionUtil.isNotEmpty(ids) ? ids : null);
        }
        Page<Machine> page = new Page<Machine>(pageNo, pageSize);
        IPage<Machine> pageList = machineService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 查询当前工位可选设备
     *
     * @return 设备id和名称
     */
    @ApiOperation(value = "查询当前工位可选设备", notes = "查询当前工位可选设备")
    @GetMapping(value = "/getMachineByStationId")
    public Result<?> getMachineByStationId(@RequestParam(required = false) String stationId) {
        JSONArray machineByStationId = machineService.getMachineByStationId(stationId);
        return Result.ok(machineByStationId);
    }

    /**
     * 添加
     *
     * @param machine
     * @return
     */
    @ApiOperation(value = "设备管理-添加", notes = "设备管理-添加")
    @PostMapping(value = "/add")
    @Around(value = "设备管理-添加")
    public Result<?> add(@RequestBody Machine machine) {
        machineService.saveMachine(machine);
        return Result.ok(machine);
    }

    /**
     * 编辑
     *
     * @param machineVO
     * @return
     */
    @ApiOperation(value = "设备管理-编辑", notes = "设备管理-编辑")
    @PostMapping(value = "/edit")
    @AutoLog(value = "设备管理-编辑", operateType = CommonConstant.OPERATE_TYPE_EDIT)
    public Result<?> edit(@RequestBody MachineVO machineVO) {
        machineService.updateMachine(machineVO);
        return Result.ok(machineVO);
    }

    /**
     * 修改设备参数
     *
     * @param machineVO
     * @return
     */
    @ApiOperation(value = "设备管理-修改设备参数", notes = "设备管理-修改设备参数")
    @PostMapping(value = "/updateMachinePara")
    @AutoLog(value = "设备管理-修改设备参数", operateType = CommonConstant.OPERATE_TYPE_EDIT)
    public Result<?> updateMachinePara(@RequestBody MachineVO machineVO) {
        machineService.updateMachinePara(machineVO);
        return Result.ok(machineVO);
    }

    /**
     * 修改设备读数
     *
     * @param machineVO
     * @return
     */
    @ApiOperation(value = "设备管理-修改设备读数", notes = "设备管理-修改设备读数")
    @PostMapping(value = "/updateMachineData")
    @AutoLog(value = "设备管理-修改设备读数", operateType = CommonConstant.OPERATE_TYPE_EDIT)
    public Result<?> updateMachineData(@RequestBody MachineVO machineVO) {
        machineService.updateMachineData(machineVO);
        return Result.ok(machineVO);
    }

    /**
     * 删除设备参数
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "设备管理-删除设备参数", notes = "设备管理-删除设备参数")
    @GetMapping(value = "/deleteMachineParaBatch")
    @AutoLog(value = "设备管理-删除设备参数", operateType = CommonConstant.OPERATE_TYPE_DELETE)
    public Result<?> deleteMachineParaBatch(@RequestParam(value = "ids", required = true) String ids) {
        machineService.deleteMachinePara(ids);
        return Result.ok(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 根据设备类型查询策略组
     *
     * @param machineTypeId
     * @return
     */
    @ApiOperation(value = "设备管理-根据设备类型查询策略组", notes = "设备管理-根据设备类型查询策略组")
    @GetMapping(value = "/getPolicyByTypeId")
    public Result<?> getPolicyByMachineTypeId(@RequestParam(name = "machineTypeId", required = true) String machineTypeId,
                                              @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                              @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                              HttpServletRequest req) {
        Page<MachineTypePolicy> page = new Page<>(pageNo, pageSize);
        return Result.ok(machineService.getPolicyByMachineTypeId(machineTypeId, page));
    }

    /**
     * 根据设备类型查询读数组
     *
     * @param machineTypeId
     * @return
     */
    @ApiOperation(value = "设备管理-根据设备类型查询策略组", notes = "设备管理-根据设备类型查询策略组")
    @GetMapping(value = "/getDataByTypeId")
    public Result<?> getDataByTypeId(@RequestParam(name = "machineTypeId", required = true) String machineTypeId,
                                     @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                     @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                     HttpServletRequest req) {
        Page<MachineTypeData> page = new Page<>(pageNo, pageSize);
        return Result.ok(machineService.getDataByMachineTypeId(machineTypeId, page));
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备管理-通过id查询", notes = "设备管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Machine machine = machineService.getById(id);
        return Result.ok(machine);
    }

    /**
     * 设备看板数据
     *
     * @return
     */
    @ApiOperation(value = "设备管理-设备看板数据", notes = "设备管理-设备看板数据")
    @GetMapping(value = "/dashboardMachineInfo")
    public Result<?> dashboardMachineInfo() {
        MachineDashBoardCountVO machineDashBoardCountVO = new MachineDashBoardCountVO();

        //查询设备总数
        QueryWrapper<Machine> machineQueryWrapper = new QueryWrapper<>();
        machineQueryWrapper.select("COUNT(*) as total");
        Map<String, Object> map = machineService.getMap(machineQueryWrapper);
        Integer machineTotal = Integer.valueOf(String.valueOf(map.get("total")));
        machineDashBoardCountVO.setMachineCount(machineTotal);

        //查询待维修设备数
        Integer repairCount = machineMapper.queryMachineRepairCount();
        machineDashBoardCountVO.setMachineRepairCount(repairCount);

        //查询待点检维修数
        Integer checkCount = machineMapper.queryMachinMaintenanceCount("1");
        machineDashBoardCountVO.setMachineCheckCount(checkCount);

        //查询待保养设备数
        Integer maintenanceCount = machineMapper.queryMachinMaintenanceCount("2");
        machineDashBoardCountVO.setMachineMaintenanceCount(maintenanceCount);

        return Result.ok(machineDashBoardCountVO);
    }

    /**
     * 通过设备id查询设备看板数据
     *
     * @param machineId
     * @return
     */
    @ApiOperation(value = "设备管理-通过设备id查询设备看板数据", notes = "设备管理-通过设备id查询设备看板数据")
    @GetMapping(value = "/queryDashBoardByMachineId")
    public Result<?> queryDashBoardByMachineId(@RequestParam(value = "machineId", required = true) String machineId) {
        MachineDashBoardVO machineDashBoardVO = new MachineDashBoardVO();

        Machine machine = machineService.getById(machineId);
        BeanUtils.copyProperties(machine, machineDashBoardVO);

        //查询该设备的任务
        List<MachineRepairAndMaintenanceVO> machineRepairAndMaintenanceVOList = machineMapper.queryMachineDashBoardInfoById(machineId);
        machineDashBoardVO.setMachineRepairAndMaintenanceVOList(machineRepairAndMaintenanceVOList);

        //查询该设备的日志
        List<MachineLog> machineLogList = machineLogService.queryByMachineId(machineId);
        machineDashBoardVO.setMachineLogList(machineLogList);

        return Result.ok(machineDashBoardVO);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备管理-通过设备id查询对于的报告模板", notes = "设备管理-通过设备id查询对于的报告模板")
    @GetMapping(value = "/queryTemplateById")
    public Result<?> queryMachineTemplate(@RequestParam(name = "id", required = true) String id) {
        Machine machine = machineService.getById(id);
        MachineType machineType = machineTypeService.getById(machine.getMachineTypeId());
        return Result.ok(machineType.getTemplateId());
    }

    /**
     * 通过设备id查询设备日志
     *
     * @param id
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "设备管理-通过设备id查询设备日志", notes = "设备管理-通过设备id查询设备日志")
    @GetMapping(value = "/queryLogById")
    public Result<?> queryLogById(@RequestParam(name = "id", required = true) String id,
                                  @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                  @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                  HttpServletRequest req) {
        QueryWrapper<MachineLog> machineLogQueryWrapper = new QueryWrapper<>();
        machineLogQueryWrapper.eq("machine_id", id);
        machineLogQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machineLogQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        machineLogQueryWrapper.orderByDesc("create_time");
        Page<MachineLog> page = new Page<>(pageNo, pageSize);
        Page<MachineLog> logPage = machineLogService.page(page, machineLogQueryWrapper);
        return Result.ok(logPage);
    }

    /**
     * 通过id查询全部详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备管理-通过id查询", notes = "设备管理-通过id查询")
    @GetMapping(value = "/queryDetailById")
    public Result<?> queryDetailById(@RequestParam(name = "id", required = true) String id) {
        MachineVO machineVO = machineService.selectDetailById(id);
        return Result.ok(machineVO);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param machine
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Machine machine) {
        // Step.1 组装查询条件
        QueryWrapper<Machine> queryWrapper = QueryGenerator.initQueryWrapper(machine, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }

        //Step.2 获取导出数据

        List<Machine> machineList = machineService.list(queryWrapper);
        List<MachineVO> allList = new ArrayList<>();
        for (Machine temp : machineList) {
            String machineId = temp.getId();
            //查询设备管理子表,查询设备读数，并设置到machineVO
            QueryWrapper<MachineData> dataQueryWrapper = new QueryWrapper<>();
            dataQueryWrapper.eq("machine_id", machineId);
            dataQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
            dataQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            List<MachineData> machineDataList = machineDataMapper.selectList(dataQueryWrapper);

            List<MachineVO> list = new ArrayList<>();
            machineDataList.forEach(machineData -> {
                //设置主表数据
                MachineVO machineVO = new MachineVO();
                BeanUtil.copyProperties(temp, machineVO);
                machineVO.setMachineDataList(Collections.singletonList(machineData));
                machineVO.setMachinePolicyList(new ArrayList<>());
                list.add(machineVO);
            });

            //查询设备维护策略,并设置到machineVO
            QueryWrapper<MachinePolicy> machinePolicyQueryWrapper = new QueryWrapper<>();
            machinePolicyQueryWrapper.eq("machine_id", machineId);
            machinePolicyQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
            machinePolicyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            List<MachinePolicy> machinePolicyList = machinePolicyMapper.selectList(machinePolicyQueryWrapper);

            List<MachineVO> tempList = new ArrayList<>();
            machinePolicyList.forEach(machinePolicy -> {
                if (list.iterator().hasNext()) {
                    MachineVO next = list.iterator().next();
                    next.setMachinePolicyList(Collections.singletonList(machinePolicy));
                } else {
                    //设置主表数据
                    MachineVO machineVO = new MachineVO();
                    BeanUtil.copyProperties(temp, machineVO);
                    machineVO.setMachinePolicyList(Collections.singletonList(machinePolicy));
                    machineVO.setMachineDataList(new ArrayList<>());
                    tempList.add(machineVO);
                }
            });
            if (tempList.size() > 0) {
                allList.addAll(tempList);
            }
            allList.addAll(list);
        }

        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "设备管理");
        mv.addObject(NormalExcelConstants.CLASS, MachineVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("设备管理数据", "导出人:" + sysUser.getRealname(), "设备管理"));
        mv.addObject(NormalExcelConstants.DATA_LIST, allList);
        return mv;

    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Machine.class);
    }

    /**
     * 执行端通过设备id查询设备详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备管理-执行端通过设备id查询设备详情", notes = "设备管理-执行端通过设备id查询设备详情")
    @GetMapping(value = "/getMachineDetails")
    public Result<?> getMachineDetails(@RequestParam(name = "id", required = true) String id) {
        MachineDetailsVO machineDetails = service.getMachineDetails(id);
        return Result.ok(machineDetails);
    }

    /**
     * 添加停机事件
     *
     * @param machineStopTime
     * @return
     */
    @ApiOperation(value = "设备管理-添加停机事件", notes = "设备管理-添加停机事件")
    @PostMapping(value = "/addStopMachine")
    @AutoLog(value = "设备管理-添加停机事件")
    public Result<?> addStopMachine(@RequestBody MachineStopTime machineStopTime) {
        machineStopTimeService.saveMachineStopTime(machineStopTime);
        return Result.ok(machineStopTime);
    }

    /**
     * 删除停机事件
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备管理-删除停机事件", notes = "设备管理-删除停机事件")
    @GetMapping(value = "/deletedStopMachine")
    @AutoLog(value = "设备管理-删除停机事件")
    public Result<?> deletedStopMachine(@RequestParam(value = "id", required = true) String id) {
        machineStopTimeService.delMachineStopTime(id);
        return Result.ok(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除停机时间
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "设备管理-批量删除停机时间", notes = "设备管理-批量删除停机时间")
    @GetMapping(value = "deleteStopTimeBatch")
    @AutoLog(value = "设备管理-批量删除停机时间")
    public Result<?> deleteStopTimeBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        machineStopTimeService.delBatchMachineStopTime(idList);
        return Result.ok(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 改变设备维护策略状态
     *
     * @param machinePolicyStatus
     * @return
     */
    @ApiOperation(value = "设备管理-改变设备维护策略状态", notes = "设备管理-改变设备维护策略状态")
    @PostMapping(value = "changeMachinePolicyStatus")
    @AutoLog(value = "设备管理-改变设备维护策略状态")
    public Result<?> editMachinePolicyStatus(@RequestBody Map<String, Object> machinePolicyStatus) {
        String policyId = machinePolicyStatus.get("policyId").toString();
        String status = machinePolicyStatus.get("status").toString();
        machineService.changeMachinePolicyStatus(policyId, status);
        return Result.ok(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 改变策略基准时间
     *
     * @param policyExcuteBaseTime
     * @return
     */
    @ApiOperation(value = "设备管理-改变策略基准时间", notes = "设备管理-改变策略基准时间")
    @PostMapping(value = "changeExcuteBaseTime")
    @AutoLog(value = "设备管理-改变策略基准时间")
    public Result<?> changeExcuteBaseTime(@RequestBody Map<String, Object> policyExcuteBaseTime) {
        String policyId = policyExcuteBaseTime.get("policyId").toString();
        String baseTime = policyExcuteBaseTime.get("baseTime").toString();
        service.addExcuteBaseTime(policyId, baseTime);
        return Result.ok(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 改变设备状态
     *
     * @param machineStatus
     * @return
     */
    @ApiOperation(value = "设备管理-改变设备状态", notes = "设备管理-改变设备状态")
    @PostMapping(value = "changeMachineStatus")
    @AutoLog(value = "设备管理-改变设备状态")
    public Result<?> changeMachineStatus(@RequestBody Map<String, Object> machineStatus) {
        String machineId = machineStatus.get("machineId").toString();
        String status = machineStatus.get("status").toString();
        service.changeMachineStatus(machineId, status);
        return Result.ok(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 改变设备状态
     *
     * @param machineOeeSettingVO
     * @return
     */
    @ApiOperation(value = "设备管理-oee", notes = "设备管理-oee")
    @GetMapping(value = "getOee")
    @AutoLog(value = "设备管理-oee")
    public Result<?> getOee(MachineOeeSettingVO machineOeeSettingVO) {
        ResultDataVO oeeData = service.getOeeData(machineOeeSettingVO);
        return Result.ok(oeeData);
    }


}
