package com.ils.modules.mes.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.DictModel;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.ConvertUtils;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import com.ils.modules.mes.machine.service.MachineRepairTaskService;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description: 维修任务
 * @Author: Tian
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "维修任务")
@RestController
@RequestMapping("/machine/machineRepairTask")
public class MachineRepairTaskController extends ILSController<MachineRepairTask, MachineRepairTaskService> {
    @Autowired
    private MachineRepairTaskService machineRepairTaskService;
    @Autowired
    private MachineService machineService;

    /**
     * 分页列表查询
     *
     * @param machineRepairTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "维修任务-分页列表查询", notes = "维修任务-分页列表查询")
    @GetMapping(value = "/list")
    @AutoLog(value = "维修任务-分页列表查询")
    public Result<?> queryPageList(MachineRepairTask machineRepairTask,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MachineRepairTask> queryWrapper = QueryGenerator.initQueryWrapper(machineRepairTask, req.getParameterMap());
        //设置执行人
        String employeeName = req.getParameter("employeeName");
        if (StringUtils.isNoneBlank(employeeName)) {
            queryWrapper.like("employee_name", employeeName);
        }
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
        Page<MachineRepairTask> page = new Page<MachineRepairTask>(pageNo, pageSize);
        String userId = CommonUtil.getLoginUser().getId();
        IPage<MachineRepairVO> pageList = machineRepairTaskService.listPage(userId, page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param repairTaskWithEmployeeVO
     * @return
     */
    @ApiOperation(value = "维修任务-添加", notes = "维修任务-添加")
    @PostMapping(value = "/add")
    @AutoLog(value = "维修任务-分页列表查询")
    public Result<?> add(@RequestBody RepairTaskWithEmployeeVO repairTaskWithEmployeeVO) {
        return Result.ok(machineRepairTaskService.saveMachineRepairTask(repairTaskWithEmployeeVO));
    }

    /**
     * 编辑
     *
     * @param repairTaskWithEmployeeVO
     * @return
     */
    @ApiOperation(value = "维修任务-编辑", notes = "维修任务-编辑")
    @PostMapping(value = "/edit")
    @AutoLog(value = "维修任务-编辑")
    public Result<?> edit(@RequestBody RepairTaskWithEmployeeVO repairTaskWithEmployeeVO) {
        machineRepairTaskService.updateMachineRepairTask(repairTaskWithEmployeeVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "维修任务-通过id查询", notes = "维修任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        RepairTaskWithEmployeeVO repairTaskWithEmployeeVO = machineRepairTaskService.queryTaskWithEmployeesById(id);
        return Result.ok(repairTaskWithEmployeeVO);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "维修任务-通过id查询详情", notes = "维修任务-通过id查询详情")
    @GetMapping(value = "/queryDetailById")
    public Result<?> queryDetailById(@RequestParam(name = "id", required = true) String id) {
        RepairTaskDetailVO repairTaskDetailVO = machineRepairTaskService.queryDetailById(id);
        return Result.ok(repairTaskDetailVO);
    }

    /**
     * 维修任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)
     *
     * @param maintainStatus
     * @return
     */
    @ApiOperation(value = "维修任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)", notes = "维修任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)")
    @PostMapping(value = "/changeStatus")
    @AutoLog(value = "维修任务-改变执行状态(开始，执行，暂停，结束等等状态的改变)")
    public Result<?> changeStatus(@RequestBody MaintainStatus maintainStatus) {
        String userId = CommonUtil.getLoginUser().getId();
        service.changeStatus(maintainStatus.getId(), maintainStatus.getStatus(), userId, maintainStatus.getQrcode());
        return Result.ok();
    }

    /**
     * 维修任务-改变执行人
     *
     * @param repairTaskRealExcuter
     * @return
     */
    @ApiOperation(value = "维修任务-改变执行人", notes = "维修任务-改变执行人")
    @PostMapping(value = "/changeRealExcuter")
    @AutoLog(value = "维修任务-改变执行人")
    public Result<?> editChangeRealExcuter(@RequestBody RepairTaskRealExcuter repairTaskRealExcuter) {
        service.changeRealExcuter(repairTaskRealExcuter.getId(), repairTaskRealExcuter.getRealExcuters());
        return Result.ok();
    }

    /**
     * 添加故障原因
     *
     * @param addRepairReason
     * @return
     */
    @ApiOperation(value = "维修任务-添加故障原因", notes = "维修任务-添加故障原因")
    @PostMapping(value = "/addFaultReason")
    @AutoLog(value = "维修任务-添加故障原因")
    public Result<?> addFaultReason(@RequestBody Map<String, String> addRepairReason) {
        String faultReasonId = addRepairReason.get("FaultReasonId");
        String taskId = addRepairReason.get("taskId");
        service.addFaultReason(faultReasonId, taskId);
        return Result.ok();
    }

    @ApiOperation(value = "维修目标-查询所有的维修目标id和名字", notes = "维修目标-查询所有的维修目标id和名字")
    @GetMapping(value = "/mesMachineDict")
    public Result<?> queryDictMachineName() {
        List<DictModel> dictModels = machineRepairTaskService.queryDictMachineName();
        return Result.ok(dictModels);
    }

    @ApiOperation(value = "维修-通过维修设备id查询模板类型", notes = "维修-通过维修设备id查询模板类型")
    @GetMapping(value = "/queryTemplateIdByMachineId")
    public Result<?> queryTemplateIdByMachineId(@RequestParam(name = "id", required = true) String id) {
        String templateId = machineRepairTaskService.queryTemplateIdByMachineId(id);
        return Result.ok(templateId);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param spareParts
     */
    @GetMapping(value = "/exportXls")
    @AutoLog(value = "维修任务-导出维修任务")
    public ModelAndView exportXls(HttpServletRequest request, MachineRepairTask machineRepairTask) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<MachineRepairTask> queryWrapper = QueryGenerator.initQueryWrapper(machineRepairTask, request.getParameterMap());
        Page<MachineRepairTask> page = new Page<MachineRepairTask>(1, Integer.MAX_VALUE);
        String userId = CommonUtil.getLoginUser().getId();

        String selections = request.getParameter("selections");
        if (ConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.eq("id", selectionList);
        }

        IPage<MachineRepairVO> pageList = machineRepairTaskService.listPage(userId, page, queryWrapper);
        List<MachineRepairVO> exportList = pageList.getRecords();

        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        // 此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, "维修记录");
        mv.addObject(NormalExcelConstants.CLASS, MachineRepairVO.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams("维修记录报表", "导出人:" + sysUser.getRealname(), "维修记录"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
}
