package com.ils.modules.mes.produce.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
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
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.produce.vo.WorkProcessTaskVO;
import com.ils.modules.mes.util.TreeNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 工单工序任务
 * @Author: fengyi
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "工单工序任务")
@RestController
@RequestMapping("/produce/workProcessTask")
public class WorkProcessTaskController extends ILSController<WorkProcessTask, WorkProcessTaskService> {
    @Autowired
    private WorkProcessTaskService workProcessTaskService;

    /**
     * 分页列表查询
     *
     * @param workProcessTask
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "工单工序任务-分页列表查询", notes = "工单工序任务-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WorkProcessTaskVO workProcessTaskVO,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WorkProcessTaskVO> queryWrapper =
                QueryGenerator.initQueryWrapper(workProcessTaskVO, req.getParameterMap());
        Page<WorkProcessTaskVO> page = new Page<WorkProcessTaskVO>(pageNo, pageSize);
        IPage<WorkProcessTaskVO> pageList = workProcessTaskService.listPage(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param workProcessTask
     * @return
     */
    @AutoLog("工单工序任务-添加")
    @ApiOperation(value = "工单工序任务-添加", notes = "工单工序任务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WorkProcessTask workProcessTask) {
        workProcessTaskService.saveWorkProcessTask(workProcessTask);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param workProcessTask
     * @return
     */
    @AutoLog("工单工序任务-编辑")
    @ApiOperation(value = "工单工序任务-编辑", notes = "工单工序任务-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WorkProcessTask workProcessTask) {
        workProcessTaskService.updateWorkProcessTask(workProcessTask);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "工单工序任务-通过id删除")
    @ApiOperation(value = "工单工序任务-通过id删除", notes = "工单工序任务-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        workProcessTaskService.delWorkProcessTask(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "工单工序任务-批量删除")
    @ApiOperation(value = "工单工序任务-批量删除", notes = "工单工序任务-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.workProcessTaskService.delBatchWorkProcessTask(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工单工序任务-通过id查询", notes = "工单工序任务-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WorkProcessTask workProcessTask = workProcessTaskService.getById(id);
        return Result.ok(workProcessTask);
    }

    /**
     * 根据任务查询工位
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据任务查询工位-通过id查询", notes = "根据任务查询工位-通过id查询")
    @GetMapping(value = "/queryStationById")
    public Result<?> queryStationById(@RequestParam(name = "id", required = true) String id) {
        List<TreeNode> lstTreeNode = workProcessTaskService.queryStationById(id);
        return Result.ok(lstTreeNode);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param workProcessTask
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WorkProcessTask workProcessTask) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        WorkProcessTaskVO workProcessTaskVO = new WorkProcessTaskVO();
        BeanUtils.copyProperties(workProcessTask, workProcessTaskVO);
        QueryWrapper<WorkProcessTaskVO> queryWrapper =
                QueryGenerator.initQueryWrapper(workProcessTaskVO, request.getParameterMap());
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        List<WorkProcessTaskVO> pageList = workProcessTaskService.listPageAll(queryWrapper);

        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "工序任务");
        mv.addObject(NormalExcelConstants.CLASS, WorkProcessTaskVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("工序任务", "导出人:" + sysUser.getRealname(), "工序任务"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
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
        return super.importExcel(request, response, WorkProcessTask.class);
    }
}
