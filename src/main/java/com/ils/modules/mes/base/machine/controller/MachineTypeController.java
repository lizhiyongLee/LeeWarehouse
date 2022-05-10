package com.ils.modules.mes.base.machine.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.base.machine.vo.MachineTypeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 设备类型
 * @Author: Tian
 * @Date: 2020-10-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "设备类型")
@RestController
@RequestMapping("/base/machine/machineType")
public class MachineTypeController extends ILSController<MachineType, MachineTypeService> {
    @Autowired
    private MachineTypeService machineTypeService;

    /**
     * 分页列表查询
     *
     * @param machineType
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "设备类型-分页列表查询", notes = "设备类型-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MachineType machineType,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MachineType> queryWrapper = QueryGenerator.initQueryWrapper(machineType, req.getParameterMap());
        Page<MachineType> page = new Page<MachineType>(pageNo, pageSize);
        IPage<MachineType> pageList = machineTypeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param machineTypeVO
     * @return
     */
    @AutoLog("设备类型-添加")
    @ApiOperation(value = "设备类型-添加", notes = "设备类型-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MachineTypeVO machineTypeVO) {
        machineTypeVO.setTenantId(TenantContext.getTenant());
        machineTypeService.saveMain(machineTypeVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param machineTypeVO
     * @return
     */
    @AutoLog(value = "设备类型-编辑",logType =CommonConstant.OPERATE_TYPE_EDIT )
    @ApiOperation(value = "设备类型-编辑", notes = "设备类型-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody MachineTypeVO machineTypeVO) {
        machineTypeService.updateMachineTypeMain(machineTypeVO);
        return Result.ok(machineTypeVO);
    }

    /**
     * 删除设备参数
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "设备管理-删除设备参数", notes = "设备管理-删除设备参数")
    @GetMapping(value = "/deleteMachineTypeParaBatch")
    @AutoLog(value = "设备管理-编辑", operateType = CommonConstant.OPERATE_TYPE_DELETE)
    public Result<?> deleteMachineTypeParaBatch(@RequestParam(value = "ids", required = true) String ids) {
        machineTypeService.deleteMachinePara(ids);
        return Result.ok(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备类型-通过id查询", notes = "设备类型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MachineTypeVO machineTypeVO = machineTypeService.selectByMainId(id);
        return Result.ok(machineTypeVO);
    }

    /**
     * 停用启用设备类型
     *
     * @param machineType
     * @return
     */
    @ApiOperation(value = "设备类型-停用启用设备类型", notes = "设备类型-停用启用设备类型")
    @PostMapping(value = "/changeStatus")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<?> changeStatus(@RequestBody MachineType machineType) {
        machineTypeService.updateById(machineType);
        return Result.ok();
    }

    /**
     * 导出excel
     *
     * @param request
     * @param machineType
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MachineType machineType) {
        return super.exportXls(request, machineType, MachineType.class, "设备类型");
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
        return super.importExcel(request, response, MachineType.class);
    }
}
