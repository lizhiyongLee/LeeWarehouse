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
 * @Description: 转移记录
 * @Author: wyssss
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "转移记录")
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
     * 分页列表查询
     *
     * @param itemTransferRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "转移记录-分页列表查询", notes = "转移记录-分页列表查询")
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
     * 添加
     *
     * @param itemTransferRecord
     * @return
     */
    @AutoLog("转移记录-添加")
    @ApiOperation(value = "转移记录-添加", notes = "转移记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemTransferRecord itemTransferRecord) {
        itemTransferRecordService.saveItemTransferRecord(itemTransferRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param itemTransferRecord
     * @return
     */
    @AutoLog("转移记录-编辑")
    @ApiOperation(value = "转移记录-编辑", notes = "转移记录-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemTransferRecord itemTransferRecord) {
        itemTransferRecordService.updateItemTransferRecord(itemTransferRecord);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "转移记录-通过id删除")
    @ApiOperation(value = "转移记录-通过id删除", notes = "转移记录-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        itemTransferRecordService.delItemTransferRecord(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "转移记录-批量删除")
    @ApiOperation(value = "转移记录-批量删除", notes = "转移记录-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.itemTransferRecordService.delBatchItemTransferRecord(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "转移记录-通过id查询", notes = "转移记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ItemTransferRecord itemTransferRecord = itemTransferRecordService.getById(id);
        return Result.ok(itemTransferRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param itemTransferRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemTransferRecord itemTransferRecord) {
        return super.exportXls(request, itemTransferRecord, ItemTransferRecord.class, "转移记录");
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
        return super.importExcel(request, response, ItemTransferRecord.class);
    }

    @ApiOperation(value = "入库时通过选择的仓位code查询物料转移记录", notes = "入库时通过选择的仓位code查询物料转移记录")
    @GetMapping(value = "/queryRecordByStorageCode")
    public Result<?> queryRecordByStorageCode(
            @RequestParam(name = "wareStorageCode", required = false) String wareStorageCode,
            @RequestParam(name = "controlId", required = false) String controlId,
            @RequestParam(name = "storageId", required = false) String storageId) {
        List<ItemTransferRecord> itemTransferRecords = itemTransferRecordService.queryRecordByStorageCode(wareStorageCode, controlId, storageId);
        return Result.ok(itemTransferRecords);
    }

    /**
     * 库存入库时通过sop步骤id查询对应的数据
     *
     * @param controlId
     * @return
     */
    @ApiOperation(value = "转移记录-库存入库时通过sop步骤id查询对应的数据", notes = "转移记录-库存入库时通过sop步骤id查询对应的数据")
    @GetMapping(value = "/queryByControlId")
    public Result queryItemTransferRecordBySopControlId(@RequestParam(name = "controlId", required = false) String controlId) {
        SopControl sopControl = sopControlService.getById(controlId);

        WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());
        //查询工位对应的完工仓库
        List<WareFeedingStorageRelateArea> wareFeedingStorageRelateAreas = wareFeedingStorageRelateAreaMapper.selectFeedingStorageByAreaId(produceTask.getStationId());

        if (wareFeedingStorageRelateAreas == null) {
            throw new ILSBootException("P-SOP-0094");
        }

        List<String> storageIds = wareFeedingStorageRelateAreas.stream().map(wareFeedingStorageRelateArea -> wareFeedingStorageRelateArea.getFeedingStorage()).collect(Collectors.toList());
        List<WareStorage> wareStorageList = wareStorageService.listByIds(storageIds);

        return Result.ok(wareStorageList);
    }
}
