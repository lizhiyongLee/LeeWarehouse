package com.ils.modules.mes.execution.controller;

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
import com.ils.modules.mes.enums.ProduceRecordQcStatusEnum;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.service.WorkProduceRecordService;
import com.ils.modules.mes.execution.vo.DayProductDetailExportVO;
import com.ils.modules.mes.execution.vo.QrItemCellInfo;
import com.ils.modules.mes.execution.vo.WorkProduceRecordInfoVO;
import com.ils.modules.mes.execution.vo.WorkProduceRecordReportVO;
import com.ils.modules.mes.material.entity.ItemCell;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @Description: 产出记录
 * @Author: fengyi
 */
@Slf4j
@Api(tags = "产出记录")
@RestController
@RequestMapping("/execution/workProduceRecord")
public class WorkProduceRecordController extends ILSController<WorkProduceRecord, WorkProduceRecordService> {
    @Autowired
    private WorkProduceRecordService workProduceRecordService;

    /**
     * 分页列表查询
     *
     * @param workProduceRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "产出记录-分页列表查询", notes = "产出记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WorkProduceRecord workProduceRecord,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        String qcStatus = workProduceRecord.getQcStatus();
        workProduceRecord.setQcStatus(null);
        QueryWrapper<WorkProduceRecord> queryWrapper = QueryGenerator.initQueryWrapper(workProduceRecord, req.getParameterMap());
        if (!ProduceRecordQcStatusEnum.UNQUALIFIED.getValue().equals(qcStatus)) {
            queryWrapper.in("qc_status", ProduceRecordQcStatusEnum.QUALIFIED.getValue(), ProduceRecordQcStatusEnum.WAIT_TEST.getValue());
        } else {
            queryWrapper.eq("qc_status", ProduceRecordQcStatusEnum.UNQUALIFIED.getValue());
        }
        Page<WorkProduceRecord> page = new Page<WorkProduceRecord>(pageNo, pageSize);
        IPage<WorkProduceRecord> pageList = workProduceRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }
    /**
     * 日生产明细报表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "日生产明细报表查询", notes = "日生产明细报表查询")
    @GetMapping(value = "/dayProductDetailReport")
    public Result<?> dayProductDetailReport(@RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                            HttpServletRequest req) {
        String itemName = req.getParameter("itemName");
        String produceDate = req.getParameter("produceDate");
        String employeeId = req.getParameter("employeeId");
        QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(itemName)) {
            queryWrapper.like("item_name", itemName);
        }
        if (StringUtils.isNotEmpty(employeeId)) {
            queryWrapper.like("employee_id", employeeId);

        }
        if (StringUtils.isNotEmpty(produceDate)) {
            queryWrapper.eq("produce_date", produceDate);

        }
        Page<WorkProduceRecord> page = new Page<>(pageNo, pageSize);
        IPage<WorkProduceRecord> pageList = workProduceRecordService.page(page, queryWrapper);

        return Result.ok(pageList);
    }


    /**
     * 无码、批次合格产出记录
     *
     * @param workProduceRecord
     * @return
     */
    @ApiOperation(value = "无码、批次合格产出记录-添加", notes = "无码、批次合格产出记录-添加")
    @PostMapping(value = "/qualifiedAdd")
    public Result<?> qualifiedAdd(@RequestBody WorkProduceRecord workProduceRecord) {
        workProduceRecordService.qualifiedAdd(workProduceRecord);
        return Result.ok(workProduceRecord);
    }

    /**
     * 无码、批次不合格产出记录
     *
     * @param workProduceRecord
     * @return
     */
    @ApiOperation(value = "无码、批次不合格产出记录-添加", notes = "无码、批次不合格产出记录-添加")
    @PostMapping(value = "/unQualifiedAdd")
    public Result<?> unQualifiedAdd(@RequestBody WorkProduceRecord workProduceRecord) {
        workProduceRecordService.unQualifiedAdd(workProduceRecord);
        return Result.ok(workProduceRecord);
    }

    /**
     * 无码联产出记录
     *
     * @param workProduceRecord
     * @return
     */
    @ApiOperation(value = "联产品无码产出记录-添加", notes = "联产品无码产出记录-添加")
    @PostMapping(value = "/jointProductNoCodeAdd")
    public Result<?> jointProductNoCodeAdd(@RequestBody WorkProduceRecord workProduceRecord) {
        workProduceRecordService.jointProductNoCodeAdd(workProduceRecord);
        return Result.ok(workProduceRecord);
    }

    /**
     * 标签码联产出记录
     *
     * @param workProduceRecord
     * @return
     */
    @ApiOperation(value = "联产品标签码产出记录-添加", notes = "联产品标签码产出记录-添加")
    @PostMapping(value = "/jointProductQrcodeAdd")
    public Result<?> jointProductQrcodeAdd(@RequestBody WorkProduceRecord workProduceRecord) {
        workProduceRecordService.jointProductQrcodeAdd(workProduceRecord);
        return Result.ok(workProduceRecord);
    }

    /**
     * 联产品标签码物料校验
     *
     * @param qrcode，produceTaskId
     * @return
     */
    @ApiOperation(value = "联产品标签码物料校验", notes = "联产品标签码物料校验")
    @GetMapping(value = "/checkJointProductQrcode")
    public Result<?> checkJointProductQrcode(@RequestParam(name = "qrcode", required = true) String qrcode,
                                             @RequestParam(name = "produceTaskId", required = true) String produceTaskId) {
        ItemCell itemCell = workProduceRecordService.checkJointProductQrcode(qrcode, produceTaskId);
        return Result.ok(itemCell);
    }

    /**
     * 无码、批次码检验投产是否符合条件
     *
     * @param produceTaskId
     * @return
     */
    @ApiOperation(value = "无码、批次码检验投产是否符合条件", notes = "无码、批次码检验投产是否符合条件")
    @GetMapping(value = "/checkPreProduceRecord")
    public Result<?>
    checkPreProduceRecord(@RequestParam(name = "produceTaskId", required = true) String produceTaskId) {
        workProduceRecordService.checkPreProduceRecord(produceTaskId);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param workProduceRecord
     * @return
     */
    @ApiOperation(value = "产出记录-编辑", notes = "产出记录-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WorkProduceRecord workProduceRecord) {
        workProduceRecordService.updateWorkProduceRecord(workProduceRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "产出记录-通过id删除")
    @ApiOperation(value = "产出记录-通过id删除", notes = "产出记录-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        workProduceRecordService.delWorkProduceRecord(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "产出记录-批量删除")
    @ApiOperation(value = "产出记录-批量删除", notes = "产出记录-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.workProduceRecordService.delBatchWorkProduceRecord(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "产出记录-通过id查询", notes = "产出记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkProduceRecord workProduceRecord = workProduceRecordService.getById(id);
        return Result.ok(workProduceRecord);
    }

    @ApiOperation(value = "web端执行任务-分页列表查询", notes = "web端执行任务-分页列表查询")
    @GetMapping(value = "/queryProduceRecordReport")
    public Result<?> queryProduceRecordReport(WorkProduceRecord workProduceRecord,
                                              @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                              @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        Page<WorkProduceRecord> page  = new Page<>(pageNo, pageSize);
        Page<WorkProduceRecordReportVO> reportData = workProduceRecordService.getReportData(workProduceRecord, page, req);
        return Result.ok(reportData);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param workProduceRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkProduceRecord workProduceRecord) {
        return super.exportXls(request, workProduceRecord, WorkProduceRecord.class, "产出记录");
    }

    /**
     * 导出报表excel
     *
     * @param request
     * @param workProduceRecord
     */
    @GetMapping(value = "/exportReportXls")
    public ModelAndView exportReportXls(HttpServletRequest request, WorkProduceRecord workProduceRecord) {
        return this.service.exportReportXls(workProduceRecord, request);
    }

    @GetMapping(value = "/dayProductDetailExportXls")
    public ModelAndView dayProductDetailExportReportXls(HttpServletRequest req) {
        String itemName = req.getParameter("itemName");
        String produceDate = req.getParameter("produceDate");
        String employeeId = req.getParameter("employeeId");
        QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotEmpty(itemName)) {
            queryWrapper.like("item_name", itemName);
        }
        if (StringUtils.isNotEmpty(employeeId)) {
            queryWrapper.like("employee_id", employeeId);

        }
        if (StringUtils.isNotEmpty(produceDate)) {
            queryWrapper.eq("produce_date", produceDate);

        }
        //Step.2 获取导出数据
        List<DayProductDetailExportVO> pageList = new ArrayList<DayProductDetailExportVO>();
        List<WorkProduceRecord> workProduceRecords = this.service.list(queryWrapper);
        for (WorkProduceRecord workProduceRecord : workProduceRecords) {
            DayProductDetailExportVO dayProductDetailExportVO = new DayProductDetailExportVO();
            BeanUtils.copyProperties(workProduceRecord, dayProductDetailExportVO);
            pageList.add(dayProductDetailExportVO);
        }
        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "日生产明细报表");
        mv.addObject(NormalExcelConstants.CLASS, DayProductDetailExportVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("日生产明细数据", "导出人:" + sysUser.getRealname(), "日生产明细报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     * s
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WorkProduceRecord.class);
    }

    /**
     * 标签码合格产出记录
     *
     * @param workProduceRecordInfoVO
     * @return
     */
    @ApiOperation(value = "标签码合格产出记录-添加", notes = "标签码合格产出记录-添加")
    @PostMapping(value = "/qualifiedQrcodeAdd")
    public Result<?> qualifiedQrcodeAdd(@RequestBody WorkProduceRecordInfoVO workProduceRecordInfoVO) {
        workProduceRecordService.qualifiedQrcodeAdd(workProduceRecordInfoVO);
        return Result.ok(workProduceRecordInfoVO);
    }

    /**
     * 标签码不合格产出记录
     *
     * @param workProduceRecordInfoVO
     * @return
     */
    @ApiOperation(value = "标签码不合格产出记录-添加", notes = "标签码不合格产出记录-添加")
    @PostMapping(value = "/unQualifiedQrcodeAdd")
    public Result<?> unQualifiedQrcodeAdd(@RequestBody WorkProduceRecordInfoVO workProduceRecordInfoVO) {
        workProduceRecordService.unQualifiedQrcodeAdd(workProduceRecordInfoVO);
        return Result.ok(workProduceRecordInfoVO);
    }

    /**
     * 标签码投产时查询符合条件物料单元
     *
     * @param produceTaskId
     * @return
     */
    @ApiOperation(value = "标签码投产时查询符合条件物料单元", notes = "标签码投产时查询符合条件物料单元")
    @GetMapping(value = "/queryQrcodeItemCell")
    public Result<?>
    queryQrcodeItemCell(@RequestParam(name = "produceTaskId", required = true) String produceTaskId,
                        @RequestParam(name = "qrcode", required = true) String qrcode,
                        @RequestParam(name = "qcStatus", required = true) String qcStatus) {
        QrItemCellInfo qrItemCellInfo = workProduceRecordService.queryQrcodeItemCell(produceTaskId, qrcode, qcStatus);
        return Result.ok(qrItemCellInfo);
    }

}
