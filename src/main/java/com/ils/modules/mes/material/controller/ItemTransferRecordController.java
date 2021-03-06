package com.ils.modules.mes.material.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.service.SopTemplateService;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.entity.WareFinishedStorageRelateArea;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareFeedingStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.mapper.WareFinishedStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.mapper.WareHouseMapper;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.enums.ItemManageWayEnum;
import com.ils.modules.mes.enums.ItemTransferStatusEnum;
import com.ils.modules.mes.enums.SopControlLogic;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import com.ils.modules.mes.material.service.ItemTransferRecordService;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.service.SopControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: ????????????
 * @Author: wyssss
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "????????????")
@RestController
@RequestMapping("/material/itemTransferRecord")
public class ItemTransferRecordController extends ILSController<ItemTransferRecord, ItemTransferRecordService> {
    @Autowired
    private ItemTransferRecordService itemTransferRecordService;
    @Autowired
    private SopTemplateService sopTemplateService;
    @Autowired
    private WorkProduceTaskService workProduceTaskService;
    @Autowired
    private WareFeedingStorageRelateAreaMapper wareFeedingStorageRelateAreaMapper;
    @Autowired
    private WareHouseMapper wareHouseMapper;
    @Autowired
    private SopControlService sopControlService;
    @Autowired
    private WareStorageService wareStorageService;

    /**
     * ??????????????????
     *
     * @param itemTransferRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "????????????-??????????????????", notes = "????????????-??????????????????")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemTransferRecord itemTransferRecord,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemTransferRecord> queryWrapper = QueryGenerator.initQueryWrapper(itemTransferRecord, req.getParameterMap());
        Page<ItemTransferRecord> page = new Page<ItemTransferRecord>(pageNo, pageSize);
        IPage<ItemTransferRecord> pageList = itemTransferRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * ??????
     *
     * @param itemTransferRecord
     * @return
     */
    @AutoLog("????????????-??????")
    @ApiOperation(value = "????????????-??????", notes = "????????????-??????")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemTransferRecord itemTransferRecord) {
        itemTransferRecordService.saveItemTransferRecord(itemTransferRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * ??????
     *
     * @param itemTransferRecord
     * @return
     */
    @AutoLog("????????????-??????")
    @ApiOperation(value = "????????????-??????", notes = "????????????-??????")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemTransferRecord itemTransferRecord) {
        itemTransferRecordService.updateItemTransferRecord(itemTransferRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
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
        itemTransferRecordService.delItemTransferRecord(id);
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
        List<String> idList = Arrays.asList(ids.split(","));
        this.itemTransferRecordService.delBatchItemTransferRecord(idList);
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
        ItemTransferRecord itemTransferRecord = itemTransferRecordService.getById(id);
        return Result.ok(itemTransferRecord);
    }

    /**
     * ??????excel
     *
     * @param request
     * @param itemTransferRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemTransferRecord itemTransferRecord) {
        return super.exportXls(request, itemTransferRecord, ItemTransferRecord.class, "????????????");
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
        return super.importExcel(request, response, ItemTransferRecord.class);
    }

    @ApiOperation(value = "??????????????????????????????code????????????????????????", notes = "??????????????????????????????code????????????????????????")
    @GetMapping(value = "/queryRecordByStorageCode")
    public Result<?> queryRecordByStorageCode(
            @RequestParam(name = "wareStorageCode", required = false) String wareStorageCode,
            @RequestParam(name = "controlId", required = false) String controlId,
            @RequestParam(name = "storageId", required = false) String storageId) {
        List<ItemTransferRecord> itemTransferRecords = itemTransferRecordService.queryRecordByStorageCode(wareStorageCode, controlId, storageId);
        return Result.ok(itemTransferRecords);
    }

    /**
     * ?????????????????????sop??????id?????????????????????
     *
     * @param controlId
     * @return
     */
    @ApiOperation(value = "????????????-?????????????????????sop??????id?????????????????????", notes = "????????????-?????????????????????sop??????id?????????????????????")
    @GetMapping(value = "/queryByControlId")
    public Result queryItemTransferRecordBySopControlId(@RequestParam(name = "controlId", required = false) String controlId) {
        SopControl sopControl = sopControlService.getById(controlId);

        WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());
        //?????????????????????????????????
        List<WareFeedingStorageRelateArea> wareFeedingStorageRelateAreas = wareFeedingStorageRelateAreaMapper.selectFeedingStorageByAreaId(produceTask.getStationId());

        if (wareFeedingStorageRelateAreas == null) {
            throw new ILSBootException("P-SOP-0094");
        }

        List<String> storageIds = wareFeedingStorageRelateAreas.stream().map(wareFeedingStorageRelateArea -> wareFeedingStorageRelateArea.getFeedingStorage()).collect(Collectors.toList());
        List<WareStorage> wareStorageList = wareStorageService.listByIds(storageIds);

        return Result.ok(wareStorageList);
    }
}
