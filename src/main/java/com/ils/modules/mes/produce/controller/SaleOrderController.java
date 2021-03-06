package com.ils.modules.mes.produce.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.ExcelImportUtil;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.ImportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.produce.entity.SaleOrder;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.service.SaleOrderLineService;
import com.ils.modules.mes.produce.service.SaleOrderService;
import com.ils.modules.mes.produce.vo.SaleOrderVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: ????????????
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@RestController
@RequestMapping("/produce/saleOrder")
@Api(tags = "????????????")
@Slf4j
public class SaleOrderController extends ILSController<SaleOrder, SaleOrderService> {
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private SaleOrderLineService saleOrderLineService;

    /**
     * ??????????????????
     *
     * @param saleOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "????????????-??????????????????", notes = "????????????-??????????????????")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SaleOrder saleOrder,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SaleOrder> queryWrapper = QueryGenerator.initQueryWrapper(saleOrder, req.getParameterMap());
        Page<SaleOrder> page = new Page<SaleOrder>(pageNo, pageSize);
        IPage<SaleOrder> pageList = saleOrderService.page(page, queryWrapper);
        return Result.ok(pageList);
    }


    /**
     * ??????
     *
     * @param saleOrderPage
     * @return
     */
    @AutoLog("????????????-??????")
    @ApiOperation(value = "????????????-??????", notes = "????????????-??????")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SaleOrderVO saleOrderPage) {
        SaleOrder saleOrder = new SaleOrder();
        BeanUtils.copyProperties(saleOrderPage, saleOrder);
        saleOrderService.saveMain(saleOrder, saleOrderPage.getSaleOrderLineList());
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * ??????
     *
     * @param saleOrderPage
     * @return
     */
    @AutoLog("????????????-??????")
    @ApiOperation(value = "????????????-??????", notes = "????????????-??????")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SaleOrderVO saleOrderPage) {
        SaleOrder saleOrder = new SaleOrder();
        BeanUtils.copyProperties(saleOrderPage, saleOrder);
        saleOrderService.updateMain(saleOrder, saleOrderPage.getSaleOrderLineList());
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * ????????????-????????????
     *
     * @param saleOrderPage
     * @return
     */
    @ApiOperation(value = "????????????-??????????????????", notes = "????????????-??????????????????")
    @GetMapping(value = "/updateStatusBatch")
    public Result<?> updateStatus(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="status",required=true) String status) {
        List<String> idList = Arrays.asList(ids.split(","));
        saleOrderService.updateStatus(status, idList);
        return commonSuccessResult(CommonConstant.STATUS_YES);
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "????????????-??????id??????")
    @ApiOperation(value = "????????????-??????id??????", notes = "????????????-??????id??????")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        saleOrderService.delMain(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "????????????-????????????")
    @ApiOperation(value = "????????????-????????????", notes = "????????????-????????????")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.saleOrderService.delBatchMain(Arrays.asList(ids.split(",")));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "????????????-??????id??????", notes = "????????????-??????id??????")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SaleOrder saleOrder = saleOrderService.getById(id);
        SaleOrderVO saleOrderVO = new SaleOrderVO();
        BeanUtils.copyProperties(saleOrder, saleOrderVO);
        List<SaleOrderLine> saleOrderLineList = saleOrderLineService.selectByMainId(id);
        saleOrderVO.setSaleOrderLineList(saleOrderLineList);
        return Result.ok(saleOrderVO);
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "????????????-??????????????????id???????????????????????????", notes = "????????????-??????????????????id???????????????????????????")
    @GetMapping(value = "/querySaleOrderLineByMainId")
    public Result<?> querySaleOrderLineListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SaleOrderLine> saleOrderLineList = saleOrderLineService.selectByMainId(id);
        return Result.ok(saleOrderLineList);
    }

    /**
     * ??????excel
     *
     * @param request
     * @param saleOrder
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SaleOrder saleOrder) {
        // Step.1 ??????????????????
        QueryWrapper<SaleOrder> queryWrapper = QueryGenerator.initQueryWrapper(saleOrder, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        //Step.2 ??????????????????
        List<SaleOrderVO> pageList = new ArrayList<SaleOrderVO>();
        List<SaleOrder> saleOrderList = saleOrderService.list(queryWrapper);

        for (SaleOrder temp : saleOrderList) {
            List<SaleOrderLine> saleOrderLineList = saleOrderLineService.selectByMainId(temp.getId());
            for (SaleOrderLine saleOrderLine : saleOrderLineList) {
                SaleOrderVO vo = new SaleOrderVO();
                BeanUtils.copyProperties(temp, vo);
                vo.setSaleOrderLineList(Collections.singletonList(saleOrderLine));
                pageList.add(vo);
            }
        }
        //Step.3 ??????AutoPoi??????Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, SaleOrderVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:" + sysUser.getRealname(), "????????????"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SaleOrderVO> list = ExcelImportUtil.importExcel(file.getInputStream(), SaleOrderVO.class, params);
                for (SaleOrderVO page : list) {
                    SaleOrder po = new SaleOrder();
                    BeanUtils.copyProperties(page, po);
                    saleOrderService.saveMain(po, page.getSaleOrderLineList());
                }
                return Result.ok("?????????????????????????????????:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("??????????????????:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("?????????????????????");
    }

}
