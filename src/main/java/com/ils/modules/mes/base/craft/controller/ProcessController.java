package com.ils.modules.mes.base.craft.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.craft.entity.Route;
import com.ils.modules.mes.base.craft.service.*;
import com.ils.modules.mes.base.craft.vo.ProcessParaVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;
import com.ils.modules.mes.base.craft.entity.ProcessStation;
import com.ils.modules.mes.base.craft.vo.ProcessDetailVO;
import com.ils.modules.mes.base.craft.vo.ProcessQcMethodVO;
import com.ils.modules.mes.base.craft.vo.ProcessVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 工序
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/craft/process")
@Api(tags = "工序")
@Slf4j
public class ProcessController extends ILSController<Process, ProcessService> {
    @Autowired
    private ProcessService processService;
    @Autowired
    private ProcessQcMethodService processQcMethodService;
    @Autowired
    private ProcessNgItemService processNgItemService;
    @Autowired
    private ProcessStationService processStationService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;
    @Autowired
    private ProcessParaHeadService processParaHeadService;

    /**
     * 分页列表查询
     *
     * @param process
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "工序-分页列表查询", notes = "工序-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Process process,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Process> queryWrapper = QueryGenerator.initQueryWrapper(process, req.getParameterMap());
        Page<Process> page = new Page<Process>(pageNo, pageSize);
        IPage<Process> pageList = processService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param processVO
     * @return
     */
    @ApiOperation(value = "工序-添加", notes = "工序-添加")
    @PostMapping(value = "/add")
    @AutoLog("工序-添加")
    public Result<?> add(@RequestBody ProcessVO processVO) {
        processService.saveMain(processVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param processPage
     * @return
     */
    @ApiOperation(value = "工序-编辑", notes = "工序-编辑")
    @PostMapping(value = "/edit")
    @AutoLog("工序-编辑")
    public Result<?> edit(@RequestBody ProcessVO processPage) {
        processService.updateMain(processPage);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 通过id查询，工位是用字符串拼接的
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工序-通过id查询，工位是用字符串拼接的", notes = "工序-通过id查询，工位是用字符串拼接的")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Process process = processService.getById(id);
        ProcessVO processVO = new ProcessVO();
        BeanUtils.copyProperties(process, processVO);
        //质检方案
        List<ProcessQcMethodVO> processQcMethodList = processQcMethodService.selectByMainId(id);
        processVO.setProcessQcMethodList(processQcMethodList);
        //不良项
        List<ProcessNgItem> processNgItemList = processNgItemService.selectByMainId(id);
        processVO.setProcessNgItemList(processNgItemList);
        //工位
        QueryWrapper<ProcessStation> queryWrapper = new QueryWrapper();
        queryWrapper.eq("process_id", id);
        List<ProcessStation> lstStation = processStationService.list(queryWrapper);
        if (null != lstStation && lstStation.size() > 0) {
            processVO.setProcessStation(lstStation.stream().map(ProcessStation::getStationId).collect(Collectors.joining(",")));
            processVO.setProcessStationName(lstStation.stream().map(ProcessStation::getStationName).collect(Collectors.joining(",")));
        }
        //自定义字段
        List<DefineFieldValueVO> istDefineFields = defineFieldValueService.queryDefineFieldValue(TableCodeConstants.PROCESS_TABLE_CODE, process.getId());
        processVO.setLstDefineFields(istDefineFields);
        //参数模板
        List<ProcessParaVO> processParaVOList = processParaHeadService.queryByProcessId(id);
        processVO.setProcessParaVOList(processParaVOList);
        return Result.ok(processVO);
    }

    /**
     * 通过id查询，工位是用list拼接的
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工序-通过id查询，工位是用list拼接的", notes = "通过id查询，工位是用list拼接的")
    @GetMapping(value = "/queryProcessById")
    public Result<?> queryProcessById(@RequestParam(name = "id", required = true) String id) {
        Process process = processService.getById(id);
        ProcessDetailVO processDetailVO = new ProcessDetailVO();
        BeanUtils.copyProperties(process, processDetailVO);
        //质检方案
        List<ProcessQcMethodVO> processQcMethodList = processQcMethodService.selectByMainId(id);
        processDetailVO.setProcessQcMethodList(processQcMethodList);
        //不良项
        List<ProcessNgItem> processNgItemList = processNgItemService.selectByMainId(id);
        processDetailVO.setProcessNgItemList(processNgItemList);
        //工位
        QueryWrapper<ProcessStation> queryWrapper = new QueryWrapper();
        queryWrapper.eq("process_id", id);
        List<ProcessStation> lstStation = processStationService.list(queryWrapper);
        processDetailVO.setProcessStationList(lstStation);
        //自定义字段
        List<DefineFieldValueVO> istDefineFields = defineFieldValueService.queryDefineFieldValue(TableCodeConstants.PROCESS_TABLE_CODE, process.getId());
        processDetailVO.setLstDefineFields(istDefineFields);
        //参数模板
        List<ProcessParaVO> processParaVOList = processParaHeadService.queryByProcessId(id);
        processDetailVO.setProcessParaVOList(processParaVOList);
        return Result.ok(processDetailVO);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param process
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Process process) {
        return super.exportXls(request, process, Process.class, "工序数据");
    }
}
