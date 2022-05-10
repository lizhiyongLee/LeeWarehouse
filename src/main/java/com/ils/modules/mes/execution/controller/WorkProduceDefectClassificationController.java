package com.ils.modules.mes.execution.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;
import com.ils.modules.mes.execution.entity.WorkProduceDefectClassification;
import com.ils.modules.mes.execution.service.WorkProduceDefectClassificationService;
import com.ils.modules.mes.execution.vo.WorkProduceDefectClassificationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/3 11:58
 */
@Slf4j
@Api(tags="生产任务缺陷分类")
@RestController
@RequestMapping("/execution/workProduceDefectClassification")
public class WorkProduceDefectClassificationController extends ILSController<WorkProduceDefectClassification, WorkProduceDefectClassificationService> {
    @Autowired
    private WorkProduceDefectClassificationService workProduceDefectClassificationService;

    /**
     * 分页列表查询
     *
     * @param workProduceDefectClassification
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="缺陷分类-分页列表查询", notes="缺陷分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WorkProduceDefectClassification workProduceDefectClassification,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WorkProduceDefectClassification> queryWrapper = QueryGenerator.initQueryWrapper(workProduceDefectClassification, req.getParameterMap());
        Page<WorkProduceDefectClassification> page = new Page<>(pageNo, pageSize);
        IPage<WorkProduceDefectClassification> pageList = workProduceDefectClassificationService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 记录批量保存
     *
     * @param list
     * @return
     */
    @ApiOperation(value = "缺陷分类记录-批量添加", notes = "缺陷分类记录-批量添加")
    @PostMapping(value = "/saveBatch")
    public Result<?> saveBatch(@RequestBody List<WorkProduceDefectClassificationVO> list) {
        workProduceDefectClassificationService.saveBatchWorkProduceDefectClassification(list);
        return  commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param workProduceDefectClassification
     * @return
     */
    @ApiOperation(value="缺陷分类记录-编辑", notes="缺陷分类记录-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WorkProduceDefectClassification workProduceDefectClassification) {
        workProduceDefectClassificationService.updateWorkProduceDefectClassification(workProduceDefectClassification);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "缺陷分类记录-通过id删除")
    @ApiOperation(value="缺陷分类记录-通过id删除", notes="缺陷分类记录-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        workProduceDefectClassificationService.delWorkProduceDefectClassification(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id列表批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "缺陷分类记录-通过id列表批量删除")
    @ApiOperation(value="缺陷分类记录-通过id列表批量删除", notes="缺陷分类记录-通过id列表批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        workProduceDefectClassificationService.delBatchWorkProduceDefectClassification(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过任务id查询当前工序对应的缺陷选项
     *
     * @param taskId
     * @return
     */
    @AutoLog(value = "通过任务id查询当前工序对应的缺陷选项")
    @ApiOperation(value="通过任务id查询当前工序对应的缺陷选项", notes="通过任务id查询当前工序对应的缺陷选项")
    @GetMapping(value = "/selectNgItemByTaskId")
    public Result<?> selectNgItemByProcessFromTaskId(@RequestParam(name="taskId",required=true) String taskId) {
        List<ProcessNgItem> processNgItems = workProduceDefectClassificationService.selectNgItemByProcessFromTaskId(taskId);
        return Result.ok(processNgItems);
    }

    /**
     * 通过任务id查询缺陷分类记录列表
     *
     * @param taskId
     * @return
     */
    @AutoLog(value = "通过任务id查询缺陷分类记录列表")
    @ApiOperation(value="通过任务id查询缺陷分类记录列表", notes="通过任务id查询缺陷分类记录列表")
    @GetMapping(value = "/selectDetailByTaskId")
    public Result<?> selectDetailByTaskId(@RequestParam(name="taskId",required=true) String taskId) {
        List<WorkProduceDefectClassification> workProduceDefectClassifications = workProduceDefectClassificationService.selectDefectClassificationByTaskId(taskId);
        return Result.ok(workProduceDefectClassifications);
    }


    /**
     * 查询工序缺陷排行
     *
     * @return
     */
    @AutoLog(value = "查询工序缺陷排行")
    @ApiOperation(value="查询工序缺陷排行", notes="查询工序缺陷排行")
    @GetMapping(value = "/statisticsDefectByProcess")
    public Result<?>  statisticsDefectClassificationByProcess(){
        return Result.ok(workProduceDefectClassificationService.statisticsDefectClassificationByProcess());
    }

    /**
     * 查询工位缺陷排行
     *
     * @return
     */
    @AutoLog(value = "查询工位缺陷排行")
    @ApiOperation(value="查询工位缺陷排行", notes="查询工位缺陷排行")
    @GetMapping(value = "/statisticsDefectByStation")
    public Result<?>  statisticsDefectClassificationByStation(){
        return Result.ok(workProduceDefectClassificationService.statisticsDefectClassificationByStation());
    }

    /**
     * 查询质量柏拉图
     *
     * @return
     */
    @AutoLog(value = "查询质量柏拉图")
    @ApiOperation(value="查询质量柏拉图", notes="查询质量柏拉图")
    @GetMapping(value = "/selectQualityPlato")
    public Result<?>  selectQualityPlato(){
        return Result.ok(workProduceDefectClassificationService.selectQualityPlato());
    }
}
