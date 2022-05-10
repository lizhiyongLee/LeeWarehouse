package com.ils.modules.mes.base.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.machine.entity.MachineFaultAppearance;
import com.ils.modules.mes.base.machine.service.MachineFaultAppearanceService;
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
 * @Description: 故障现象
 * @Author: Tian
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "故障现象")
@RestController
@RequestMapping("/base/machine/machineFaultAppearance")
public class MachineFaultAppearanceController extends ILSController<MachineFaultAppearance, MachineFaultAppearanceService> {
    @Autowired
    private MachineFaultAppearanceService machineFaultAppearanceService;

    /**
     * 分页列表查询
     *
     * @param machineFaultAppearance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "故障现象-分页列表查询", notes = "故障现象-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MachineFaultAppearance machineFaultAppearance,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MachineFaultAppearance> queryWrapper = QueryGenerator.initQueryWrapper(machineFaultAppearance, req.getParameterMap());
        Page<MachineFaultAppearance> page = new Page<MachineFaultAppearance>(pageNo, pageSize);
        IPage<MachineFaultAppearance> pageList = machineFaultAppearanceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param machineFaultAppearance
     * @return
     */
    @ApiOperation(value = "故障现象-添加", notes = "故障现象-添加")
    @PostMapping(value = "/add")
    @AutoLog("故障现象-添加")
    public Result<?> add(@RequestBody MachineFaultAppearance machineFaultAppearance) {
        machineFaultAppearanceService.saveMachineFaultAppearance(machineFaultAppearance);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param machineFaultAppearance
     * @return
     */
    @ApiOperation(value = "故障现象-编辑", notes = "故障现象-编辑")
    @PostMapping(value = "/edit")
    @AutoLog("故障现象-编辑")
    public Result<?> edit(@RequestBody MachineFaultAppearance machineFaultAppearance) {
        machineFaultAppearanceService.updateMachineFaultAppearance(machineFaultAppearance);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "故障现象-通过id查询", notes = "故障现象-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MachineFaultAppearance machineFaultAppearance = machineFaultAppearanceService.getById(id);
        return Result.ok(machineFaultAppearance);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param machineFaultAppearance
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MachineFaultAppearance machineFaultAppearance) {
        return super.exportXls(request, machineFaultAppearance, MachineFaultAppearance.class, "故障现象");
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
        return super.importExcel(request, response, MachineFaultAppearance.class);
    }

    @ApiOperation(value = "故障现象-查询所有的故障现象id和名字", notes = "故障现象-查询所有的故障现象id和名字")
    @GetMapping(value = "/mesFaultAppearanceDict")
    public Result<?> queryDictFaultAppearance() {
        List<DictModel> dictModels = machineFaultAppearanceService.queryDictFaultAppearance();
        return Result.ok(dictModels);
    }
}
