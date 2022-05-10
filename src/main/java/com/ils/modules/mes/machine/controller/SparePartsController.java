package com.ils.modules.mes.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.machine.entity.*;
import com.ils.modules.mes.machine.service.SparePartsReceiptHeadService;
import com.ils.modules.mes.machine.service.SparePartsSendHeadService;
import com.ils.modules.mes.machine.service.SparePartsService;
import com.ils.modules.mes.machine.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 备件定义
 * @Author: Tian
 * @Date: 2021-02-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "备件定义")
@RestController
@RequestMapping("/machine/spareParts")
public class SparePartsController extends ILSController<SpareParts, SparePartsService> {
    @Autowired
    private SparePartsService sparePartsService;
    @Autowired
    private SparePartsReceiptHeadService sparePartsReceiptHeadService;
    @Autowired
    private SparePartsSendHeadService sparePartsSendHeadService;

    /**
     * 分页列表查询
     *
     * @param spareParts
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "备件定义-分页列表查询", notes = "备件定义-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SpareParts spareParts,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SpareParts> queryWrapper = QueryGenerator.initQueryWrapper(spareParts, req.getParameterMap());
        String neQtyKey = "qty!";
        if (req.getParameterMap().containsKey(neQtyKey)) {
            queryWrapper.ne("qty", req.getParameterMap().get(neQtyKey)[0]);
        }
        Page<SpareParts> page = new Page<SpareParts>(pageNo, pageSize);
        IPage<SpareParts> pageList = sparePartsService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param spareParts
     * @return
     */
    @ApiOperation(value = "备件定义-添加", notes = "备件定义-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SpareParts spareParts) {
        sparePartsService.saveSpareParts(spareParts);
        return Result.ok(spareParts);
    }

    @ApiOperation(value = "备件入库", notes = "备件入库")
    @PostMapping(value = "/sparePartsReceipt")
    public Result<?> sparePartsReceipt(@RequestBody SparePartsReceiptHeadVO sparePartsReceiptHeadVO) {
        List<SparePartsReceiptLine> sparePartsReceiptLineList = sparePartsReceiptHeadVO.getSparePartsReceiptLineList();
        SparePartsReceiptHead sparePartsReceiptHead = new SparePartsReceiptHead();
        BeanUtils.copyProperties(sparePartsReceiptHeadVO, sparePartsReceiptHead);
        //保存入库单表头
        sparePartsReceiptHeadService.saveSparePartsReceiptHead(sparePartsReceiptHeadVO);
        return Result.ok();
    }

    /**
     * 备件出库
     *
     * @param sparePartsSendHeadVO
     * @return
     */
    @ApiOperation(value = "备件出库", notes = "备件出库")
    @PostMapping(value = "/sparePartsSend")
    public Result<?> sparePartsSend(@RequestBody SparePartsSendHeadVO sparePartsSendHeadVO) {
        List<SparePartsSendLine> sparePartsSendLineList = sparePartsSendHeadVO.getSparePartsSendLineList();
        SparePartsSendHead sparePartsSendHead = new SparePartsSendHead();
        BeanUtils.copyProperties(sparePartsSendHeadVO, sparePartsSendHead);
        //保存出库表头
        sparePartsSendHeadService.saveSparePartsSendHead(sparePartsSendHeadVO);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param spareParts
     * @return
     */
    @ApiOperation(value = "备件定义-编辑", notes = "备件定义-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SpareParts spareParts) {
        sparePartsService.updateSpareParts(spareParts);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "备件定义-通过id删除")
    @ApiOperation(value = "备件定义-通过id删除", notes = "备件定义-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sparePartsService.delSpareParts(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "备件定义-批量删除")
    @ApiOperation(value = "备件定义-批量删除", notes = "备件定义-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.sparePartsService.delBatchSpareParts(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "备件定义-通过id查询", notes = "备件定义-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SpareParts spareParts = sparePartsService.getById(id);
        return Result.ok(spareParts);
    }

    /**
     * 通过id查询
     *
     * @param headId
     * @return
     */
    @ApiOperation(value = "备件入库-通过id查询", notes = "备件入库-通过id查询")
    @GetMapping(value = "/queryReceiptDetail")
    public Result<?> queryReceiptDetail(@RequestParam(name = "id", required = true) String headId) {
        SparePartsReceiptHeadVO sparePartsReceiptHeadVO = sparePartsReceiptHeadService.queryReceiptDetail(headId);
        return Result.ok(sparePartsReceiptHeadVO);
    }

    @ApiOperation(value = "备件管理详情", notes = "备件管理详情")
    @GetMapping("/querySparePartsPage")
    public Result<?> querySparePartsPage(@RequestParam(name = "id", required = true) String id) {
        //创建一个VO类用来给前端返回入库明细list
        SparePartsReceiptOrSendOrderListVO sparePartsReceiptOrSendOrderListVO = new SparePartsReceiptOrSendOrderListVO();
        List<SparePartsReceiptOrSendOrderVO> sparePartsReceiptOrSendOrderVOList = sparePartsService.querySparePartsRelate(id);
        sparePartsReceiptOrSendOrderListVO.setSparePartsReceiptOrSendOrderVOList(sparePartsReceiptOrSendOrderVOList);
        return Result.ok(sparePartsReceiptOrSendOrderListVO);
    }

    @ApiOperation(value = "仓库下的备件数量", notes = "仓库下的备件数量")
    @GetMapping("/querySparePartsQtyWithStorage")
    public Result<?> querySparePartsQtyWithStorage(@RequestParam(name = "id", required = true) String id) {
        List<SparePartsQtyVO> sparePartsQtyVOList = sparePartsService.querySparePartsQtyWithStorage(id);
        return Result.ok(sparePartsQtyVOList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param spareParts
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SpareParts spareParts) {
        return super.exportXls(request, spareParts, SpareParts.class, "备件定义");
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
        return super.importExcel(request, response, SpareParts.class);
    }
}
