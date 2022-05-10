package com.ils.modules.mes.material.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.material.entity.ItemDeliveryGoodsRecord;
import com.ils.modules.mes.material.service.ItemDeliveryGoodsRecordService;
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
 * @Description: 发货记录
 * @Author: wyssss
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "发货记录")
@RestController
@RequestMapping("/material/itemDeliveryGoodsRecord")
public class ItemDeliveryGoodsRecordController extends ILSController<ItemDeliveryGoodsRecord, ItemDeliveryGoodsRecordService> {
    @Autowired
    private ItemDeliveryGoodsRecordService itemDeliveryGoodsRecordService;

    /**
     * 分页列表查询
     *
     * @param itemDeliveryGoodsRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "发货记录-分页列表查询", notes = "发货记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemDeliveryGoodsRecord itemDeliveryGoodsRecord,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemDeliveryGoodsRecord> queryWrapper = QueryGenerator.initQueryWrapper(itemDeliveryGoodsRecord, req.getParameterMap());
        Page<ItemDeliveryGoodsRecord> page = new Page<ItemDeliveryGoodsRecord>(pageNo, pageSize);
        IPage<ItemDeliveryGoodsRecord> pageList = itemDeliveryGoodsRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param itemDeliveryGoodsRecord
     * @return
     */
    @AutoLog("发货记录-添加")
    @ApiOperation(value = "发货记录-添加", notes = "发货记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemDeliveryGoodsRecord itemDeliveryGoodsRecord) {
        itemDeliveryGoodsRecordService.saveItemDeliveryGoodsRecord(itemDeliveryGoodsRecord);
        return Result.ok(itemDeliveryGoodsRecord);
    }

    /**
     * 编辑
     *
     * @param itemDeliveryGoodsRecord
     * @return
     */
    @AutoLog("发货记录-编辑")
    @ApiOperation(value = "发货记录-编辑", notes = "发货记录-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemDeliveryGoodsRecord itemDeliveryGoodsRecord) {
        itemDeliveryGoodsRecordService.updateItemDeliveryGoodsRecord(itemDeliveryGoodsRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "发货记录-通过id删除")
    @ApiOperation(value = "发货记录-通过id删除", notes = "发货记录-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        itemDeliveryGoodsRecordService.delItemDeliveryGoodsRecord(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "发货记录-批量删除")
    @ApiOperation(value = "发货记录-批量删除", notes = "发货记录-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.itemDeliveryGoodsRecordService.delBatchItemDeliveryGoodsRecord(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "发货记录-通过id查询", notes = "发货记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ItemDeliveryGoodsRecord itemDeliveryGoodsRecord = itemDeliveryGoodsRecordService.getById(id);
        return Result.ok(itemDeliveryGoodsRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param itemDeliveryGoodsRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemDeliveryGoodsRecord itemDeliveryGoodsRecord) {
        return super.exportXls(request, itemDeliveryGoodsRecord, ItemDeliveryGoodsRecord.class, "发货记录");
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
        return super.importExcel(request, response, ItemDeliveryGoodsRecord.class);
    }
}
