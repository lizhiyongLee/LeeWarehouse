package com.ils.modules.mes.base.craft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.craft.entity.Parameter;
import com.ils.modules.mes.base.craft.service.ParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 参数管理
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "参数管理")
@RestController
@RequestMapping("/base/craft/parameter")
public class ParameterController extends ILSController<Parameter, ParameterService> {
    @Autowired
    private ParameterService parameterService;

    /**
     * 分页列表查询
     *
     * @param parameter
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "参数管理-分页列表查询", notes = "参数管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Parameter parameter,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Parameter> queryWrapper = QueryGenerator.initQueryWrapper(parameter, req.getParameterMap());
        Page<Parameter> page = new Page<Parameter>(pageNo, pageSize);
        IPage<Parameter> pageList = parameterService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param parameter
     * @return
     */
    @ApiOperation(value = "参数管理-添加", notes = "参数管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Parameter parameter) {
        parameterService.saveParameter(parameter);
        return Result.ok(parameter);
    }

    /**
     * 编辑
     *
     * @param parameter
     * @return
     */
    @ApiOperation(value = "参数管理-编辑", notes = "参数管理-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody Parameter parameter) {
        parameterService.updateParameter(parameter);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "参数管理-通过id查询", notes = "参数管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Parameter parameter = parameterService.getById(id);
        return Result.ok(parameter);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param parameter
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Parameter parameter) {
        return super.exportXls(request, parameter, Parameter.class, "参数管理");
    }

}
