package com.ils.modules.mes.sop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.service.SopControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 标准作业任务步骤控件
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "标准作业任务步骤控件")
@RestController
@RequestMapping("/sop/sopControl")
public class SopControlController extends ILSController<SopControl, SopControlService> {
    @Autowired
    private SopControlService sopControlService;

    /**
     * 分页列表查询
     *
     * @param sopControl
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤控件-分页列表查询", notes = "标准作业任务步骤控件-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SopControl sopControl,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SopControl> queryWrapper = QueryGenerator.initQueryWrapper(sopControl, req.getParameterMap());
        Page<SopControl> page = new Page<SopControl>(pageNo, pageSize);
        IPage<SopControl> pageList = sopControlService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sopControl
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤控件-添加", notes = "标准作业任务步骤控件-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SopControl sopControl) {
        sopControlService.saveSopControl(sopControl);
        return Result.ok(sopControl);
    }

    /**
     * 编辑
     *
     * @param sopControl
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤控件-编辑", notes = "标准作业任务步骤控件-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SopControl sopControl) {
        sopControlService.updateSopControl(sopControl);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }



    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤控件-通过id查询", notes = "标准作业任务步骤控件-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SopControl sopControl = sopControlService.getById(id);
        return Result.ok(sopControl);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sopControl
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SopControl sopControl) {
        return super.exportXls(request, sopControl, SopControl.class, "标准作业任务步骤控件");
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
        return super.importExcel(request, response, SopControl.class);
    }
}
