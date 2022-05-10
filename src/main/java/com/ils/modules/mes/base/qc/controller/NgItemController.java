package com.ils.modules.mes.base.qc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.qc.entity.NgItem;
import com.ils.modules.mes.base.qc.service.NgItemService;
import com.ils.modules.mes.util.CommonUtil;
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
 * @Description: 不良类型原因
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "不良类型原因")
@RestController
@RequestMapping("/base/qc/ngItem")
public class NgItemController extends ILSController<NgItem, NgItemService> {
    @Autowired
    private NgItemService ngItemService;

    /**
     * 分页列表查询
     *
     * @param ngItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "不良类型原因-分页列表查询", notes = "不良类型原因-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(NgItem ngItem,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<NgItem> queryWrapper = QueryGenerator.initQueryWrapper(ngItem, req.getParameterMap());
        Page<NgItem> page = new Page<NgItem>(pageNo, pageSize);
        IPage<NgItem> pageList = ngItemService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param ngItem
     * @return
     */
    @AutoLog("不良类型原因-添加")
    @ApiOperation(value = "不良类型原因-添加", notes = "不良类型原因-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody NgItem ngItem) {
        ngItem.setTenantId(CommonUtil.getTenantId());
        ngItemService.saveNgItem(ngItem);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param ngItem
     * @return
     */
    @AutoLog("不良类型原因-添加")
    @ApiOperation(value = "不良类型原因-编辑", notes = "不良类型原因-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody NgItem ngItem) {
        ngItemService.updateNgItem(ngItem);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "不良类型原因-通过id删除")
    @ApiOperation(value = "不良类型原因-通过id删除", notes = "不良类型原因-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        ngItemService.delNgItem(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "不良类型原因-批量删除")
    @ApiOperation(value = "不良类型原因-批量删除", notes = "不良类型原因-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.ngItemService.delBatchNgItem(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "不良类型原因-通过id查询", notes = "不良类型原因-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        NgItem ngItem = ngItemService.getById(id);
        return Result.ok(ngItem);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param ngItem
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NgItem ngItem) {
        return super.exportXls(request, ngItem, NgItem.class, "不良类型原因");
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
        return super.importExcel(request, response, NgItem.class);
    }
}
