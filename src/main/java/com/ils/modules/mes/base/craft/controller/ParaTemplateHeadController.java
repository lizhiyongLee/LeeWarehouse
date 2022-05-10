package com.ils.modules.mes.base.craft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.craft.entity.ParaTemplateDetail;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import com.ils.modules.mes.base.craft.service.ParaTemplateDetailService;
import com.ils.modules.mes.base.craft.service.ParaTemplateHeadService;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 参数模板主表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "参数模板主表")
@RestController
@RequestMapping("/base/craft/paraTemplateHead")
public class ParaTemplateHeadController extends ILSController<ParaTemplateHead, ParaTemplateHeadService> {
    @Autowired
    private ParaTemplateHeadService paraTemplateHeadService;
    @Autowired
    private ParaTemplateDetailService paraTemplateDetailService;

    /**
     * 分页列表查询
     *
     * @param paraTemplateHead
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "参数模板主表-分页列表查询", notes = "参数模板主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ParaTemplateHead paraTemplateHead,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ParaTemplateHead> queryWrapper = QueryGenerator.initQueryWrapper(paraTemplateHead, req.getParameterMap());
        Page<ParaTemplateHead> page = new Page<ParaTemplateHead>(pageNo, pageSize);
        IPage<ParaTemplateHead> pageList = paraTemplateHeadService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param parameterTemplateVO
     * @return
     */
    @ApiOperation(value = "参数模板主表-添加", notes = "参数模板主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ParameterTemplateVO parameterTemplateVO) {
        paraTemplateHeadService.saveParaTemplateHead(parameterTemplateVO);
        return Result.ok(parameterTemplateVO);
    }

    /**
     * 编辑
     *
     * @param parameterTemplateVO
     * @return
     */
    @ApiOperation(value = "参数模板主表-编辑", notes = "参数模板主表-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ParameterTemplateVO parameterTemplateVO) {
        paraTemplateHeadService.updateParaTemplateHead(parameterTemplateVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 停用启用参数模板
     *
     * @param parameterTemplateVO
     * @return
     */
    @ApiOperation(value = "参数模板-停用启用", notes = "参数模板-停用启用")
    @PostMapping(value = "/changeStatus")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<?> changeStatus(@RequestBody ParameterTemplateVO parameterTemplateVO) {
        paraTemplateHeadService.updateById(parameterTemplateVO);
        return Result.ok();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "参数模板主表-通过id查询", notes = "参数模板主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ParameterTemplateVO parameterTemplateVO = paraTemplateHeadService.queryById(id);
        return Result.ok(parameterTemplateVO);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param paraTemplateHead
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ParaTemplateHead paraTemplateHead) {
        // Step.1 组装查询条件
        QueryWrapper<ParaTemplateHead> queryWrapper = QueryGenerator.initQueryWrapper(paraTemplateHead, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        //Step.2 获取导出数据
        List<ParameterTemplateVO> pageList = new ArrayList<ParameterTemplateVO>();
        List<ParaTemplateHead> paraTemplateHeadList = paraTemplateHeadService.list(queryWrapper);

        for (ParaTemplateHead temp : paraTemplateHeadList) {
            QueryWrapper<ParaTemplateDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("para_temp_id", temp.getId());
            List<ParaTemplateDetail> detailList = paraTemplateDetailService.list(detailQueryWrapper);
            for (ParaTemplateDetail detail : detailList) {
                ParameterTemplateVO vo = new ParameterTemplateVO();
                BeanUtils.copyProperties(temp, vo);
                vo.setParaTemplateDetailList(Collections.singletonList(detail));
                pageList.add(vo);
            }
        }
        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "参数模板");
        mv.addObject(NormalExcelConstants.CLASS, ParameterTemplateVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("参数模板数据", "导出人:" + sysUser.getRealname(), "销售订单"));
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
        return super.importExcel(request, response, ParaTemplateHead.class);
    }
}
