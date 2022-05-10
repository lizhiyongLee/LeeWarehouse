package com.ils.modules.mes.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.ConvertUtils;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import com.ils.modules.mes.machine.service.MachineMaintenanceTaskService;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description: 维保任务
 * @Author: Tian
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "维保任务")
@RestController
@RequestMapping("/machine/machineMaintenanceTask")
public class MachineMaintenanceTaskController extends ILSController<MachineMaintenanceTask, MachineMaintenanceTaskService> {
    @Autowired
    private MachineMaintenanceTaskService machineMaintenanceTaskService;
    @Autowired
    private MachineService machineService;

    /**
     * 分页列表查询
     *
     * @param maintenanceTaskVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "点检任务-分页列表查询", notes = "点检任务-分页列表查询")
    @GetMapping(value = "/querySpotCheckPageList")
    @AutoLog(value = "点检任务-分页列表查询")
    public Result<?> listQuerySpotCheckPage(MaintenanceTaskVO maintenanceTaskVO,
                                            @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                            HttpServletRequest req) {
        String employeeName = maintenanceTaskVO.getEmployeeName();
        maintenanceTaskVO.setEmployeeName(null);
        QueryWrapper<MaintenanceTaskVO> queryWrapper =
                QueryGenerator.initQueryWrapper(maintenanceTaskVO, req.getParameterMap());
        queryWrapper.eq("maintenance_type", MesCommonConstant.MAINTENANECE_TASK_TYPE_SPOT_CHECK);
        if (StringUtils.isNoneBlank(employeeName)) {
            queryWrapper.like("employee_name", employeeName);
        }
        //设置设备类型
        String machineType = req.getParameter("machineTypeId");
        if (StringUtils.isNoneBlank(machineType)) {
            QueryWrapper<Machine> machineQueryWrapper = new QueryWrapper<>();
            machineQueryWrapper.eq("machine_type_id", machineType);
            List<Machine> machineList = machineService.list(machineQueryWrapper);
            if (CommonUtil.isEmptyOrNull(machineList)) {
                queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ZERO.getStrCode());
            } else {
                queryWrapper.in("repair_machine_id", machineList.stream().map(Machine::getId).collect(Collectors.toList()));
            }
        }
        Page<MaintenanceTaskVO> page = new Page<MaintenanceTaskVO>(pageNo, pageSize);
        String userId = CommonUtil.getLoginUser().getId();
        IPage<MaintenanceTaskVO> pageList = machineMaintenanceTaskService.listPage(userId, page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param maintenanceTaskVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "保养任务-分页列表查询", notes = "保养任务-分页列表查询")
    @GetMapping(value = "/queryMaintenancePageList")
    @AutoLog(value = "保养任务-分页列表查询")
    public Result<?> listQueryMaintenancePageList(MaintenanceTaskVO maintenanceTaskVO,
                                                  @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                  @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                                  HttpServletRequest req) {
        String employeeName = maintenanceTaskVO.getEmployeeName();
        maintenanceTaskVO.setEmployeeName(null);
        QueryWrapper<MaintenanceTaskVO> queryWrapper =
                QueryGenerator.initQueryWrapper(maintenanceTaskVO, req.getParameterMap());
        queryWrapper.eq("maintenance_type", MesCommonConstant.MAINTENANECE_TASK_TYPE_MAINTENANECE);
        if (employeeName != null) {
            queryWrapper.like("employee_name", employeeName);
        }
        //设置设备类型
        String machineType = req.getParameter("machineTypeId");
        if (StringUtils.isNoneBlank(machineType)) {
            QueryWrapper<Machine> machineQueryWrapper = new QueryWrapper<>();
            machineQueryWrapper.eq("machine_type_id", machineType);
            List<Machine> machineList = machineService.list(machineQueryWrapper);
            if (CommonUtil.isEmptyOrNull(machineList)) {
                queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ZERO.getStrCode());
            } else {
                queryWrapper.in("repair_machine_id", machineList.stream().map(Machine::getId).collect(Collectors.toList()));
            }
        }
        Page<MaintenanceTaskVO> page = new Page<MaintenanceTaskVO>(pageNo, pageSize);
        String userId = CommonUtil.getLoginUser().getId();
        IPage<MaintenanceTaskVO> pageList = machineMaintenanceTaskService.listPage(userId, page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param machineMaintenanceTask
     * @return
     */
    @ApiOperation(value = "维保任务-添加", notes = "维保任务-添加")
    @PostMapping(value = "/add")
    @AutoLog(value = "维保任务-添加")
    public Result<?> add(@RequestBody MachineMaintenanceTask machineMaintenanceTask) {
        machineMaintenanceTaskService.saveMachineMaintenanceTask(machineMaintenanceTask);
        return Result.ok(machineMaintenanceTask);
    }

    /**
     * 编辑
     *
     * @param machineMaintenanceTaskVO
     * @return
     */
    @ApiOperation(value = "维保任务-编辑", notes = "维保任务-编辑")
    @PostMapping(value = "/edit")
    @AutoLog(value = "维保任务-编辑")
    public Result<?> edit(@RequestBody MachineMaintenanceTaskVO machineMaintenanceTaskVO) {
        machineMaintenanceTaskService.updateMachineMaintenanceTask(machineMaintenanceTaskVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "维保任务-通过id查询", notes = "维保任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MaintenancePageListWithPhoneVO maintenancePageListWithPhoneVO = machineMaintenanceTaskService.queryMaintenanceTaskById(id);
        return Result.ok(maintenancePageListWithPhoneVO);
    }

    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "点检任务-通过id查询详情", notes = "点检任务-通过id查询详情")
    @GetMapping(value = "/querySpotCheckDetailById")
    public Result<?> querySpotCheckDetailById(@RequestParam(name = "id", required = true) String id) {
        MaintenanceTaskDetailVO maintenanceTaskDetailVO = machineMaintenanceTaskService.queryMaintenanceTaskDetailById(id, MesCommonConstant.MAINTENANECE_TASK_TYPE_SPOT_CHECK);
        return Result.ok(maintenanceTaskDetailVO);
    }

    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "保养任务-通过id查询详情", notes = "保养任务-通过id查询详情")
    @GetMapping(value = "/queryMaintenanceTaskDetailById")
    public Result<?> queryMaintenanceTaskDetailById(@RequestParam(name = "id", required = true) String id) {
        MaintenanceTaskDetailVO maintenanceTaskDetailVO = machineMaintenanceTaskService.queryMaintenanceTaskDetailById(id, MesCommonConstant.MAINTENANECE_TASK_TYPE_MAINTENANECE);
        return Result.ok(maintenanceTaskDetailVO);
    }

    /**
     * 保养任务导出excel
     *
     * @param request
     * @param maintenanceTaskVO
     */
    @GetMapping(value = "/exportXlsMaintenanceTask")
    public ModelAndView exportXlsMaintenanceTask(HttpServletRequest request, MaintenanceTaskVO maintenanceTaskVO) {
        QueryWrapper<MaintenanceTaskVO> queryWrapper = QueryGenerator.initQueryWrapper(maintenanceTaskVO, request.getParameterMap());
        String selections = request.getParameter("selections");
        if (ConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        return exportXls(queryWrapper, MesCommonConstant.MAINTENANECE_TASK_TYPE_MAINTENANECE);
    }

    /**
     * 点检任务导出excel
     *
     * @param request
     * @param maintenanceTaskVO
     */
    @GetMapping(value = "/exportXlsSpotCheck")
    public ModelAndView exportXlsSpotCheck(HttpServletRequest request, MaintenanceTaskVO maintenanceTaskVO) {
        QueryWrapper<MaintenanceTaskVO> queryWrapper = QueryGenerator.initQueryWrapper(maintenanceTaskVO, request.getParameterMap());
        String selections = request.getParameter("selections");
        if (ConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        return exportXls(queryWrapper, MesCommonConstant.MAINTENANECE_TASK_TYPE_SPOT_CHECK);
    }

    public ModelAndView exportXls(QueryWrapper<MaintenanceTaskVO> queryWrapper, String type) {
        Page<MaintenanceTaskVO> page = new Page<MaintenanceTaskVO>(1, Integer.MAX_VALUE);
        LoginUser loginUser = CommonUtil.getLoginUser();

        queryWrapper.eq("maintenance_type", type);
        IPage<MaintenanceTaskVO> pageList = machineMaintenanceTaskService.listPage(loginUser.getId(), page, queryWrapper);
        List<MaintenanceTaskVO> exportList = pageList.getRecords();

        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        // 此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, "");
        mv.addObject(NormalExcelConstants.CLASS, MaintenanceTaskVO.class);

        String xlsName = MesCommonConstant.MAINTENANECE_TASK_TYPE_MAINTENANECE.equals(type) ? "保养记录" : "点检记录";
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams(xlsName + "报表", "导出人:" + loginUser.getRealname(), "保养记录"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }


    /**
     * 维保任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)
     *
     * @param maintainStatus
     * @return
     */
    @ApiOperation(value = "维保任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)", notes = "维保任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)")
    @PostMapping(value = "/changeStatus")
    @AutoLog(value = "维保任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)")
    public Result<?> editChangeStatus(@RequestBody MaintainStatus maintainStatus) {
        String userId = CommonUtil.getLoginUser().getId();
        service.changeStatus(maintainStatus.getId(), maintainStatus.getStatus(), userId);
        return Result.ok();
    }

    @ApiOperation(value = "维保任务-app分页查询维保任务", notes = "维保任务-app分页查询维保任务")
    @GetMapping(value = "/queryMaintenanceListPage")
    @AutoLog(value = "维保任务-app分页查询维保任务")
    public Result<?> listQueryListPageWithPhone(MaintenancePageListWithPhoneVO maintenancePageListWithPhoneVO,
                                                @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<MaintenancePageListWithPhoneVO> queryWrapper =
                QueryGenerator.initQueryWrapper(maintenancePageListWithPhoneVO, req.getParameterMap());
        //设置状态
        String exeStatus = req.getParameter("exeStatus");
        if (StringUtils.isNoneBlank(exeStatus)) {
            queryWrapper.in("status", exeStatus.split(","));
        }
        //设置设备类型
        String machineType = req.getParameter("machineTypeId");
        if (StringUtils.isNoneBlank(machineType)) {
            QueryWrapper<Machine> machineQueryWrapper = new QueryWrapper<>();
            machineQueryWrapper.eq("machine_type_id", machineType);
            List<Machine> machineList = machineService.list(machineQueryWrapper);
            if (CommonUtil.isEmptyOrNull(machineList)) {
                queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ZERO.getStrCode());
            } else {
                queryWrapper.in("repair_machine_id", machineList.stream().map(Machine::getId).collect(Collectors.toList()));
            }
        }
        Page<MaintenancePageListWithPhoneVO> page = new Page<MaintenancePageListWithPhoneVO>(pageNo, pageSize);
        IPage<MaintenancePageListWithPhoneVO> maintenancePage = service.listPageWithPhone(page, queryWrapper);
        return Result.ok(maintenancePage);
    }
}
