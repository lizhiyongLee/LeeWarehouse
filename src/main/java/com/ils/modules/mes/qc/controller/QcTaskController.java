package com.ils.modules.mes.qc.controller;

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
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.qc.entity.QcStateAjustRecord;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.entity.QcTaskEmployee;
import com.ils.modules.mes.qc.mapper.QcTaskEmployeeMapper;
import com.ils.modules.mes.qc.service.QcStateAjustRecordService;
import com.ils.modules.mes.qc.service.QcTaskService;
import com.ils.modules.mes.qc.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.TreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 质检任务
 * @Author: Tian
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "质检任务")
@RestController
@RequestMapping("/qc/qcTask")
public class QcTaskController extends ILSController<QcTask, QcTaskService> {
    @Autowired
    private QcTaskService qcTaskService;
    @Autowired
    private QcTaskEmployeeMapper qcTaskEmployeeMapper;
    @Autowired
    private QcStateAjustRecordService qcStateAjustRecordService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    /**
     * 所有任务分页列表查询
     *
     * @param qcTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "质检任务-所有任务分页列表查询", notes = "质检任务-所有任务分页列表查询")
    @GetMapping(value = "/list")
    @AutoLog(value = "质检任务-所有任务分页列表查询")
    public Result<?> listQueryPageList(QcTaskVO qcTaskVO,
                                       @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                       @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<QcTaskVO> queryWrapper = QueryGenerator.initQueryWrapper(qcTaskVO, req.getParameterMap());
        Page<QcTaskVO> page = new Page<>(pageNo, pageSize);
        IPage<QcTaskVO> pageList = qcTaskService.listPage(page, queryWrapper);
        return Result.ok(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param qcTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "质检任务-已领取任务列表", notes = "质检任务-已领取任务列表")
    @GetMapping(value = "/queryTaskList")
    @AutoLog(value = "质检任务-已领取任务列表")
    public Result<?> listQueryTaskList(QcTask qcTask,
                                       @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                       @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                       HttpServletRequest req) {
        QueryWrapper<QcTask> queryWrapper = QueryGenerator.initQueryWrapper(qcTask, req.getParameterMap());
        Page<QcTask> page = new Page<QcTask>(pageNo, pageSize);
        String qcLocationName = req.getParameter("qcLocationName");
        IPage<QcTaskVO> pageList = qcTaskService.receivedQcTaskListPage(page, queryWrapper, CommonUtil.getLoginUser().getId(),qcLocationName);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param qcTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "质检任务-待领取任务列表", notes = "质检任务-待领取任务列表")
    @GetMapping(value = "/toReceiveTask")
    @AutoLog(value = "质检任务-待领取任务列表")
    public Result<?> listQueryToRecevieTaskList(QcTask qcTask,
                                                @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<QcTask> queryWrapper = QueryGenerator.initQueryWrapper(qcTask, req.getParameterMap());
        Page<QcTask> page = new Page<QcTask>(pageNo, pageSize);
        IPage<QcTaskVO> pageList = qcTaskService.toReceiveQcTaskListPage(page, queryWrapper, CommonUtil.getLoginUser().getId());
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param qcTaskSaveVO
     * @return
     */
    @ApiOperation(value = "质检任务-添加", notes = "质检任务-添加")
    @PostMapping(value = "/add")
    @AutoLog(value = "质检任务-添加")
    public Result<?> add(@RequestBody QcTaskSaveVO qcTaskSaveVO) {
        QcTask qcTask = qcTaskService.saveQcTask(qcTaskSaveVO);
        return Result.ok(qcTask);
    }

    /**
     * 编辑
     *
     * @param qcTaskSaveVO
     * @return
     */
    @ApiOperation(value = "质检任务-编辑", notes = "质检任务-编辑")
    @PostMapping(value = "/edit")
    @AutoLog(value = "质检任务-编辑")
    public Result<?> edit(@RequestBody QcTaskSaveVO qcTaskSaveVO) {
        qcTaskService.updateQcTask(qcTaskSaveVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    @ApiOperation(value = "质检任务-修改质检任务物料信息", notes = "质检任务-修改质检任务物料信息")
    @PostMapping(value = "/updateQcTaskSampleQty")
    @AutoLog(value = "质检任务-修改质检任务物料信息")
    public Result<?> editUpdateQcTaskSampleQty(@RequestBody UpdataQcTaskSample updataQcTaskSample) {
        qcTaskService.updataQcTaskSampleQty(updataQcTaskSample);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 提交
     *
     * @return
     */
    @ApiOperation(value = "质检任务-质检任务提交", notes = "质检任务-质检任务提交")
    @PostMapping(value = "/executeQcTask")
    @AutoLog(value = "质检任务-质检任务提交")
    public Result<?> editExecuteQcTask(@RequestBody QcTaskExecuteVO qcTaskExecuteVO) {
        qcTaskService.executeQcTask(qcTaskExecuteVO);
        return Result.ok();
    }

    /**
     * 领取任务
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "质检任务-通过id领取任务", notes = "质检任务-通过id领取任务")
    @GetMapping(value = "/takeTask")
    @AutoLog("质检任务-领取任务")
    public Result<?> editTakeTask(@RequestParam(name = "id", required = true) String id) {
        qcTaskService.takeTask(id);
        return Result.ok();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "质检任务-通过id查询", notes = "质检任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        QcTaskSaveVO qcTaskSaveVO = new QcTaskSaveVO();
        QcTask qcTask = qcTaskService.getById(id);
        BeanUtils.copyProperties(qcTask, qcTaskSaveVO);
        QueryWrapper<QcTaskEmployee> qcTaskEmployeeQueryWrapper = new QueryWrapper<>();
        qcTaskEmployeeQueryWrapper.select("GROUP_CONCAT(employee_id) as employee_id");
        qcTaskEmployeeQueryWrapper.eq("excute_task_id", id);
        qcTaskEmployeeQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        QcTaskEmployee qcTaskEmployee = qcTaskEmployeeMapper.selectOne(qcTaskEmployeeQueryWrapper);
        if (qcTaskEmployee != null) {
            qcTaskSaveVO.setTaskEmployeeIds(qcTaskEmployee.getEmployeeId());
        }
        List<DefineFieldValueVO> istDefineFields = defineFieldValueService.queryDefineFieldValue(TableCodeConstants.QC_TASK_TABLE_CODE, id);
        qcTaskSaveVO.setLstDefineFields(istDefineFields);
        return Result.ok(qcTaskSaveVO);
    }

    @ApiOperation(value = "质检任务-查询质检任务详情", notes = "质检任务-查询质检任务详情")
    @GetMapping(value = "/queryQcTaskDetail")
    public Result<?> queryQcTaskDetail(@RequestParam(name = "id", required = true) String id) {
        QcTaskDetailVO qcTaskDetailVO = qcTaskService.queryQcTaskDetail(id);
        List<DefineFieldValueVO> istDefineFields = defineFieldValueService.queryDefineFieldValue(TableCodeConstants.QC_TASK_TABLE_CODE, id);
        qcTaskDetailVO.setLstDefineFields(istDefineFields);
        return Result.ok(qcTaskDetailVO);
    }

    @ApiOperation(value = "质检任务-质检调整记录分页查询", notes = "质检任务-质检调整记录分页查询")
    @GetMapping(value = "/queryQcStateAjustRecord")
    public Result<?> queryQcStateAjustRecord(QcStateAjustRecord qcStateAjustRecord,
                                             @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                             @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                             HttpServletRequest req) {
        QueryWrapper<QcStateAjustRecord> queryWrapper = QueryGenerator.initQueryWrapper(qcStateAjustRecord, req.getParameterMap());
        Page<QcStateAjustRecord> page = new Page<QcStateAjustRecord>();
        IPage<QcStateAjustRecordVO> pageList = qcStateAjustRecordService.listPage(queryWrapper, page);
        return Result.ok(pageList);
    }

    /**
     * 质量追溯记录导出
     *
     * @param request
     * @param qcStateAdjustRecord
     * @return
     */
    @GetMapping(value = "/exportQcStateAdjustRecordXls")
    public ModelAndView exportXls(HttpServletRequest request, QcStateAjustRecord qcStateAdjustRecord) {
        QueryWrapper<QcStateAjustRecord> queryWrapper = QueryGenerator.initQueryWrapper(qcStateAdjustRecord, request.getParameterMap());
        Page<QcStateAjustRecord> page = new Page<QcStateAjustRecord>();
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        IPage<QcStateAjustRecordVO> pageList = qcStateAjustRecordService.listPage(queryWrapper, page);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "质量追溯记录");
        mv.addObject(NormalExcelConstants.CLASS, QcStateAjustRecord.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("质量追溯记录", "导出人:" + sysUser.getRealname(), "质量追溯记录"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList.getRecords());
        return mv;
    }

    /**
     * 质检任务结束
     *
     * @param qcTaskEndVO
     * @return
     */
    @ApiOperation(value = "质检任务-质检任务结束", notes = "质检任务-质检任务结束")
    @PostMapping(value = "/endTask")
    @AutoLog(value = "质检任务-质检任务结束")
    public Result<?> editQcTaskEndTask(@RequestBody QcTaskEndVO qcTaskEndVO) {
        qcTaskService.endTask(qcTaskEndVO);
        return Result.ok();
    }


    /**
     * 质检任务取消
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "质检任务-质检任务取消", notes = "质检任务-质检任务取消")
    @GetMapping(value = "/cancelQcTask")
    @AutoLog(value = "质检任务-质检任务取消", logType = CommonConstant.OPERATE_TYPE_EDIT)
    public Result<?> editCancelTask(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        qcTaskService.cancelTask(idList);
        return Result.ok();
    }

    /**
     * 调整物料质量状态
     *
     * @param qcAdjustItemCellQcStatusVO
     * @return
     */
    @ApiOperation(value = "质检任务-调整物料质量状态", notes = "质检任务-调整物料质量状态")
    @PostMapping(value = "/adjustItemCellQcStatus")
    @AutoLog(value = "质检任务-调整物料质量状态")
    public Result<?> editAdjustItemCellQcStatus(@RequestBody QcAdjustItemCellQcStatusVO qcAdjustItemCellQcStatusVO) {
        //调整质量状态方法
        qcTaskService.adjustItemCellQcStatus(qcAdjustItemCellQcStatusVO.getItemCellList(), qcAdjustItemCellQcStatusVO.getQcStatus(), qcAdjustItemCellQcStatusVO.getNote());
        return Result.ok();
    }

    @ApiOperation(value = "质检任务-通过生产任务id查询生产物料", notes = "质检任务-通过生产任务id查询生产物料")
    @GetMapping(value = "queryBytaskId")
    public Result<?> queryItemCellByExecutionTask(@RequestParam(name = "id", required = true) String id) {
        List<ItemCell> itemCells = qcTaskService.queryItemCellByExecutionTask(id);
        return Result.ok(itemCells);
    }


    @ApiOperation(value = "web端看板-XR_ControlChart图", notes = "web端看板-XR_ControlChart图")
    @GetMapping("/getXRControlChartData")
    public Result<?> queryXRControlChartData(XrControlChartVO xrControlChartVO,
                                             HttpServletRequest req) {
        XrDashBoardDataVO xrDashBoardDataVO = new XrDashBoardDataVO();
        XrControlChartTableVO xrControlChartData = qcTaskService.getXRControlChartData(xrControlChartVO);
        ControlChartTableVO xCharTableData = qcTaskService.getXCharTableData(xrControlChartVO);
        ControlChartTableVO rCharTableData = qcTaskService.getRCharTableData(xrControlChartVO);
        xrDashBoardDataVO.setXControlChartTableVO(xCharTableData);
        xrDashBoardDataVO.setRControlChartTableVO(rCharTableData);
        xrDashBoardDataVO.setXrControlChartTableVO(xrControlChartData);
        return Result.ok(xrDashBoardDataVO);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param qcTask
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QcTask qcTask) {
        return super.exportXls(request, qcTask, QcTask.class, "质检任务");
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
        return super.importExcel(request, response, QcTask.class);
    }

    /**
     * 查询质检位置（仓位和工位）树形结构
     *
     * @return
     */
    @ApiOperation(value = "查询质检位置（仓位和工位）树形结构", notes = "查询质检位置（仓位和工位）树形结构")
    @GetMapping(value = "/queryTaskLocation")
    public Result<?> queryTaskLocation(HttpServletRequest req) {
        String status = req.getParameter("status");
        String name = req.getParameter("name");
        List<TreeNode> resultList = service.queryTaskLocation(name, status);
        return Result.ok(resultList);
    }

}
