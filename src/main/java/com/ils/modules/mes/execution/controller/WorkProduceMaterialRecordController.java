package com.ils.modules.mes.execution.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.enums.SopControlLogic;
import com.ils.modules.mes.enums.SopControlTypeEnum;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.service.WorkProduceMaterialRecordService;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.execution.vo.GroupMaterialRecordVO;
import com.ils.modules.mes.execution.vo.ItemCellExtendInfo;
import com.ils.modules.mes.execution.vo.MaterialRecordReportVO;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.service.SopControlService;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 投料记录
 * @Author: fengyi
 * @Date: 2020-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "投料记录")
@RestController
@RequestMapping("/execution/workProduceMaterialRecord")
public class WorkProduceMaterialRecordController extends ILSController<WorkProduceMaterialRecord, WorkProduceMaterialRecordService> {
    @Autowired
    private WorkProduceMaterialRecordService workProduceMaterialRecordService;
    @Autowired
    private WorkProduceTaskService workProduceTaskService;


    /**
     * 分页列表查询
     *
     * @param workProduceMaterialRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "投料记录-分页列表查询", notes = "投料记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WorkProduceMaterialRecord workProduceMaterialRecord,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WorkProduceMaterialRecord> queryWrapper = QueryGenerator.initQueryWrapper(workProduceMaterialRecord, req.getParameterMap());
        Page<WorkProduceMaterialRecord> page = new Page<WorkProduceMaterialRecord>(pageNo, pageSize);
        IPage<WorkProduceMaterialRecord> pageList = workProduceMaterialRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 标签码查询物料-标签码查询物料
     *
     * @param workProduceMaterialRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "投料选择物料-标签码查询物料", notes = "投料选择物料-标签码查询物料")
    @GetMapping(value = "/queryItemCell")
    public Result<?> queryItemCell(@RequestParam(name = "qrcode", required = true) String qrcode,
                                   @RequestParam(name = "produceTaskId", required = true) String produceTaskId,
                                   @RequestParam(name = "controlId", required = false) String controlId,
                                   @RequestParam(name = "stationId", required = true) String stationId, HttpServletRequest req) {
        String itemId = null;
        if (StringUtils.isNotEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);
            itemId = sopControl.getEntityItem();
        }
        ItemCellExtendInfo itemCellExtendInfo =
                workProduceMaterialRecordService.queryItemCell(produceTaskId, qrcode, stationId, itemId);
        return Result.ok(itemCellExtendInfo);
    }

    @Autowired
    private SopControlService sopControlService;

    /**
     * 投料选择物料-分页列表查询
     *
     * @param workProduceMaterialRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "投料选择物料-分页列表查询", notes = "投料选择物料-分页列表查询")
    @GetMapping(value = "/queryMaterialPageList")
    public Result<?> queryMaterialPageList(@RequestParam(name = "produceTaskId", required = true) String produceTaskId,
                                           @RequestParam(name = "keyWord", required = false) String keyWord,
                                           @RequestParam(name = "controlId", required = false) String controlId,
                                           @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                           @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        Page<GroupMaterialRecordVO> page = new Page<GroupMaterialRecordVO>(pageNo, pageSize);
        String item = null;
        String mainItem = null;
        //sop控件
        if (StringUtils.isNotEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);
            //物料管控时，这投料选择物料只能选择管控的物料类型
            if (sopControl.getControlLogic() != null && SopControlLogic.CONTROL.getValue().equals(sopControl.getControlLogic())) {
                item = sopControl.getEntityItem();
            }
            //联产品时，排除主产出的物料
            if (SopControlTypeEnum.JOINT_PRODUCT.getValue().equals(sopControl.getControlType())) {
                WorkProduceTask workProduceTask = workProduceTaskService.getById(produceTaskId);
                mainItem = workProduceTask.getItemId();
            }
        }
        IPage<GroupMaterialRecordVO> pageList =
                workProduceMaterialRecordService.queryMaterialPageList(produceTaskId, keyWord, page, item, mainItem);
        return Result.ok(pageList);
    }

    /**
     * 有码投料-添加
     *
     * @param workProduceMaterialRecord
     * @return
     */
    @ApiOperation(value = "有码投料-添加", notes = "有码投料-添加")
    @PostMapping(value = "/qrCodeAdd")
    public Result<?> qrCodeAdd(@RequestBody WorkProduceMaterialRecord workProduceMaterialRecord) {
        workProduceMaterialRecordService.qrCodeAdd(workProduceMaterialRecord);
        return Result.ok(workProduceMaterialRecord);
    }

    /**
     * 有码撤料-撤料
     *
     * @param workProduceMaterialRecord
     * @return
     */
    @ApiOperation(value = "有码撤料-撤料", notes = "有码撤料-撤料")
    @PostMapping(value = "/qrCodeUndo")
    public Result<?> qrCodeUndo(@RequestBody WorkProduceMaterialRecord workProduceMaterialRecord) {
        workProduceMaterialRecordService.qrCodeUndo(workProduceMaterialRecord);
        return Result.ok(workProduceMaterialRecord);
    }

    /**
     * 无码投料-添加
     *
     * @param lstWorkProduceMaterialRecord
     * @return
     */
    @ApiOperation(value = "无码投料-添加", notes = "无码投料-添加")
    @PostMapping(value = "/noCodeAdd")
    public Result<?> noCodeAdd(@RequestBody List<WorkProduceMaterialRecord> lstWorkProduceMaterialRecord) {
        workProduceMaterialRecordService.noCodeAdd(lstWorkProduceMaterialRecord);
        return Result.ok();
    }

    /**
     * 投料物料汇总-分页列表查询
     *
     * @param workProduceMaterialRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "投料物料汇总-分页列表查询", notes = "投料物料汇总-分页列表查询")
    @GetMapping(value = "/queryGroupRecordPageList")
    public Result<?> queryGroupRecordPageList(
            @RequestParam(name = "produceTaskId", required = true) String produceTaskId,
            @RequestParam(name = "controlId", required = false) String controlId,
            @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        Page<GroupMaterialRecordVO> page = new Page<GroupMaterialRecordVO>(pageNo, pageSize);
        IPage<GroupMaterialRecordVO> pageList =
                workProduceMaterialRecordService.queryGroupRecordPageList(produceTaskId, controlId, page);
        return Result.ok(pageList);
    }

    /**
     * 编辑
     *
     * @param workProduceMaterialRecord
     * @return
     */
    @ApiOperation(value = "投料记录-编辑", notes = "投料记录-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WorkProduceMaterialRecord workProduceMaterialRecord) {
        workProduceMaterialRecordService.updateWorkProduceMaterialRecord(workProduceMaterialRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "投料记录-通过id删除")
    @ApiOperation(value = "投料记录-通过id删除", notes = "投料记录-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        workProduceMaterialRecordService.delWorkProduceMaterialRecord(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "投料记录-批量删除")
    @ApiOperation(value = "投料记录-批量删除", notes = "投料记录-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.workProduceMaterialRecordService.delBatchWorkProduceMaterialRecord(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "投料记录-通过id查询", notes = "投料记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkProduceMaterialRecord workProduceMaterialRecord = workProduceMaterialRecordService.getById(id);
        return Result.ok(workProduceMaterialRecord);
    }

    @ApiOperation(value = "投料报表", notes = "投料记录-通过id查询")
    @GetMapping(value = "/getMaterialRecord")
    public Result<?> getMaterialRecord(MaterialRecordReportVO materialRecordReportVO,
                                       @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                       @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        Page<MaterialRecordReportVO> page = new Page<>(pageNo, pageSize);
        Page<MaterialRecordReportVO> voPage = workProduceMaterialRecordService.getMaterialRecord(page, materialRecordReportVO);
        return Result.ok(voPage);
    }

    /**
     * 导出物料消耗报表excel
     *
     * @param materialRecordReportVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/exportMaterialRecordXls")
    public ModelAndView exportMaterialRecordXls(MaterialRecordReportVO materialRecordReportVO,
                                                @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        Page<MaterialRecordReportVO> page = new Page<>(pageNo, pageSize);
        String title = "物料消耗报表";
        List<MaterialRecordReportVO> records = workProduceMaterialRecordService.getMaterialRecord(page, materialRecordReportVO).getRecords();
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject("fileName", title);
        mv.addObject("entity", MaterialRecordReportVO.class);
        mv.addObject("params", new ExportParams(title, "导出人:" + CommonUtil.getLoginUser().getRealname(), title));
        mv.addObject("data", records);
        return mv;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param workProduceMaterialRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkProduceMaterialRecord workProduceMaterialRecord) {
        return super.exportXls(request, workProduceMaterialRecord, WorkProduceMaterialRecord.class, "投料记录");
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
        return super.importExcel(request, response, WorkProduceMaterialRecord.class);
    }
}
