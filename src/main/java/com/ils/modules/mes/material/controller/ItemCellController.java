package com.ils.modules.mes.material.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.entity.WareFinishedStorageRelateArea;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareFeedingStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.mapper.WareFinishedStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.enums.ItemCellQrcodeStatusEnum;
import com.ils.modules.mes.enums.ItemManageWayEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.service.WorkProduceRecordService;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.material.service.ItemContainerManageService;
import com.ils.modules.mes.material.vo.*;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.service.SopControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 物料单元
 * @Author: wyssss
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "物料单元")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/material/itemCell")
public class ItemCellController extends ILSController<ItemCell, ItemCellService> {
    @Autowired
    private ItemCellService itemCellService;
    @Autowired
    private SopControlService sopControlService;
    @Autowired
    private WorkProduceTaskService workProduceTaskService;
    @Autowired
    private WareFeedingStorageRelateAreaMapper wareFeedingStorageRelateAreaMapper;
    @Autowired
    private WareStorageService wareStorageService;
    @Autowired
    private WareFinishedStorageRelateAreaMapper wareFinishedStorageRelateAreaMapper;
    @Autowired
    private WorkProduceRecordService workProduceRecordService;
    @Autowired
    private ItemContainerManageService itemContainerManageService;

    /**
     * 分页列表查询
     *
     * @param itemCell
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料单元-分页列表查询", notes = "物料单元-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemCell itemCell,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        Page<ItemCell> page = new Page<ItemCell>(pageNo, pageSize);
        IPage<ItemCell> pageList = itemCellService.listPage(queryWrapper, page);
        return Result.ok(pageList);
    }

    /**
     * 物料单元分页查询
     *
     * @param itemCell
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料单元-按标签码，批次码和无码区分的分页列表查询", notes = "物料单元-按标签码，批次码和无码区分的分页列表查询")
    @GetMapping(value = "/queryItemList")
    public Result<?> queryItemList(ItemCell itemCell,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        Page<ItemCell> page = new Page<ItemCell>(pageNo, pageSize);
        IPage<ItemCell> pageList = itemCellService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 标签码物料库存分页查询
     *
     * @param itemCell
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料单元-标签码物料库存分页查询", notes = "物料单元-标签码物料库存分页查询")
    @GetMapping(value = "/tagItemStockQtyPage")
    public Result<?> tagItemCellStorageQtyListPage(ItemCell itemCell,
                                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                                   HttpServletRequest req) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        queryWrapper.eq("manage_way", ItemManageWayEnum.QRCODE_MANAGE.getValue());
        queryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        Page<ItemCell> page = new Page<ItemCell>(pageNo, pageSize);
        IPage<ItemCell> pageList = itemCellService.listPage(queryWrapper, page);
        return Result.ok(pageList);
    }

    /**
     * 批次码物料库存分页查询
     *
     * @param itemCell
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料单元-批次码物料库存分页查询", notes = "物料单元-批次码物料库存分页查询")
    @GetMapping(value = "/batchItemStockQtyPage")
    public Result<?> batchItemCellStorageQtyListPage(ItemCell itemCell,
                                                     @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                     @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                                     HttpServletRequest req) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        queryWrapper.eq("manage_way", ItemManageWayEnum.BATCH_MANAGE.getValue());
        Page<ItemCell> page = new Page<ItemCell>(pageNo, pageSize);
        IPage<ItemCell> pageList = itemCellService.listPage(queryWrapper, page);
        return Result.ok(pageList);
    }


    /**
     * 标签码物料库存分页查询
     *
     * @param itemCell
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料单元-标签码物料库存分页查询", notes = "物料单元-标签码物料库存分页查询")
    @GetMapping(value = "/itemStockQtyPage")
    public Result<?> itemCellStorageQtyListPage(ItemCell itemCell,
                                                @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                                @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        Page<ItemCell> page = new Page<ItemCell>(pageNo, pageSize);
        IPage<ItemCell> pageList = itemCellService.listPageStorage(queryWrapper, page);
        return Result.ok(pageList);
    }

    /**
     * 分页汇总列表查询
     *
     * @param itemCell
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料单元-分页列表查询", notes = "物料单元-分页列表查询")
    @GetMapping(value = "/groupByList")
    public Result<?> queryPageGroupByList(ItemCell itemCell,
                                          @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                          @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                          HttpServletRequest req) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        queryWrapper.groupBy("item_code", "item_name", "spec", "unit_name", "storage_code", "storage_name", "area_code", "area_name", "house_code", "house_name", "qc_status", "qrcode", "batch");
        queryWrapper.select("item_code ,item_name,spec,unit_name,storage_code,storage_name,area_code,area_name,house_code,house_name,qc_status,sum(IFNULL(qty,0)) as qty,qrcode,batch");
        queryWrapper.gt("qty", 0);
        Page<ItemCell> page = new Page<ItemCell>(pageNo, pageSize);
        IPage<ItemCell> pageList = itemCellService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param itemCell
     * @return
     */
    @AutoLog("物料单元-添加")
    @ApiOperation(value = "物料单元-添加", notes = "物料单元-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemCell itemCell) {
        itemCellService.saveItemCell(itemCell);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param itemCell
     * @return
     */
    @AutoLog("物料单元-编辑")
    @ApiOperation(value = "物料单元-编辑", notes = "物料单元-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemCell itemCell) {
        itemCellService.updateItemCell(itemCell);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 位置物料查询批次
     *
     * @param itemId
     * @param storageId
     * @param unit
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询批次-位置物料查询批次", notes = "查询批次-位置物料查询批次")
    @GetMapping(value = "/queryBatchList")
    public Result<?> queryBatchList(@RequestParam(name = "itemId", required = true) String itemId,
                                    @RequestParam(name = "storageId", required = true) String storageId,
                                    @RequestParam(name = "unit", required = true) String unit,
                                    @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        Page<ItemCellBatchVO> page = new Page<ItemCellBatchVO>(pageNo, pageSize);
        IPage<ItemCellBatchVO> pageList = itemCellService.queryBatchList(itemId, storageId, unit, page);
        return Result.ok(pageList);
    }

    @ApiOperation(value = "物料单元-根据条件查询物料单元", notes = "物料单元-根据条件查询物料单元")
    @GetMapping(value = "/queryItemCellList")
    public Result<?> queryItemCellListByConditions(ItemCell itemCell,
                                                   HttpServletRequest req) {
        String storageCode = itemCell.getStorageCode();
        String qrcode = itemCell.getQrcode();
        itemCell.setStorageCode(null);
        itemCell.setQrcode(null);
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, req.getParameterMap());
        if (StringUtils.isNoneBlank(storageCode)) {
            String[] storageCodes = storageCode.split(",");
            List<String> storageCodeList = Arrays.asList(storageCodes);
            queryWrapper.in("storage_code", storageCodeList);
        }
        if (StringUtils.isNoneBlank(qrcode)) {
            String[] qrcodes = qrcode.split(",");
            List<String> qrcodeList = Arrays.asList(qrcodes);
            queryWrapper.in("qrcode", qrcodeList);
        }
        queryWrapper.gt("qty", 0);
        List<ItemCell> list = itemCellService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料单元-通过id删除")
    @ApiOperation(value = "物料单元-通过id删除", notes = "物料单元-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        itemCellService.delItemCell(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物料单元-批量删除")
    @ApiOperation(value = "物料单元-批量删除", notes = "物料单元-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.itemCellService.delBatchItemCell(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料单元-通过id查询", notes = "物料单元-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ItemCell itemCell = itemCellService.getById(id);
        return Result.ok(itemCell);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param itemCell
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemCell itemCell) {
        QueryWrapper<ItemCell> queryWrapper = QueryGenerator.initQueryWrapper(itemCell, request.getParameterMap());
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        if (StringUtils.isBlank(itemCell.getManageWay())) {
            queryWrapper.isNotNull("qty");
            queryWrapper.ge("qty", 0);
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //Step.2 获取导出数据
        List<ItemCell> itemCellList = itemCellService.list(queryWrapper);

        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "物料单元");
        mv.addObject(NormalExcelConstants.CLASS, ItemCell.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料单元", "导出人:" + sysUser.getRealname(), "物料单元"));
        mv.addObject(NormalExcelConstants.DATA_LIST, itemCellList);
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
        return super.importExcel(request, response, ItemCell.class);
    }

    /**
     * 无码收货
     *
     * @param itemCell
     * @return
     */
    @ApiOperation(value = "无码收货", notes = "无码收货")
    @PostMapping("/saveNoCodeItemCell")
    public Result<?> saveNoCodeItemCell(@RequestBody ItemCell itemCell) {
        itemCellService.saveNoCodeItemCell(itemCell);
        return Result.ok(itemCell);
    }

    /**
     * 批次码收货
     *
     * @param itemCell
     * @return
     */
    @ApiOperation(value = "有码（批次码）收货", notes = "有码（批次码）收货")
    @PostMapping("/saveBatchCodeItemCell")
    public Result<?> saveBatchCodeItemCell(@RequestBody ItemCell itemCell) {
        itemCellService.saveBatchCodeItemCell(itemCell);
        return Result.ok(itemCell);
    }

    /**
     * 批次码收货
     *
     * @param itemCellVO
     * @return
     */
    @ApiOperation(value = "标签码收货", notes = "标签码收货")
    @PostMapping("/saveQrcodeItemCell")
    public Result<?> saveQrcodeItemCell(@RequestBody ItemCellVO itemCellVO) {
        List<QrCodeVO> qrCodeVOList = itemCellVO.getQrCodeVOList();
        for (QrCodeVO qrCodeVO : qrCodeVOList) {
            ItemCell itemCell = new ItemCell();
            BeanUtils.copyProperties(itemCellVO, itemCell);
            itemCell.setQrcode(qrCodeVO.getQrcode());
            itemCell.setQty(qrCodeVO.getQty());
            itemCellService.saveQrcodeItemCell(itemCell);
        }
        return Result.ok(itemCellVO);
    }

    /**
     * 库存入库
     *
     * @param itemTransferRecordVO
     * @return
     */
    @ApiOperation(value = "物料单元-库存入库", notes = "物料单元-库存入库")
    @PostMapping(value = "/stocksOfInStorage")
    public Result<?> stocksOfInStorage(@RequestBody ItemTransferRecordVO itemTransferRecordVO) {
        itemCellService.stocksOfInStorage(itemTransferRecordVO);
        return Result.ok(itemTransferRecordVO);
    }

    /**
     * 标签码入库
     *
     * @param itemCellStorageVO
     * @return
     */
    @ApiOperation(value = "物料单元-标签码入库", notes = "标签码入库")
    @PostMapping(value = "/labelCodeInStorage")
    public Result<?> labelCodeInStorage(@RequestBody ItemCellStorageVO itemCellStorageVO) {
        itemCellService.labelCodeStorage(itemCellStorageVO);
        return Result.ok(itemCellStorageVO);
    }

    /**
     * 标签码出库
     *
     * @param itemCellStorageVO
     * @return
     */
    @PostMapping(value = "/labelCodeOutStorage")
    @ApiOperation(value = "物料单元-标签码出库", notes = "物料单元-标签码出库")
    public Result<?> labelCodeOutStorage(@RequestBody ItemCellStorageVO itemCellStorageVO) {
        itemCellService.labelCodeOutStorage(itemCellStorageVO);
        return Result.ok(itemCellStorageVO);
    }

    /**
     * 库存出库
     *
     * @param itemCellStorageVO
     * @return
     */
    @ApiOperation(value = "物料单元-库存出库", notes = "物料单元-库存出库")
    @PostMapping(value = "/stocksOfOutStorage")
    public Result<?> stocksOfOutStorage(@RequestBody ItemCellStorageVO itemCellStorageVO) {
        itemCellService.stocksOfOutStorage(itemCellStorageVO);
        return Result.ok(itemCellStorageVO);
    }

    @ApiOperation(value = "物料单元-库存出库时通过位置编码查询物料单元", notes = "物料单元-库存出库时通过位置编码查询物料单元")
    @GetMapping(value = "/queryItemCellByLocationCode")
    public Result<?> queryItemCellByLocationCode(
            @RequestParam(name = "code", required = true) String code,
            @RequestParam(name = "controlId", required = false) String controlId) {
        if (StringUtils.isEmpty(controlId)) {
            List<ItemCell> itemCellList = itemCellService.queryStocksOfOutStorage(code);
            return Result.ok(itemCellList);
        }
        //查询模板对应的任务下面的物料单元
        SopControl sopControl = sopControlService.getById(controlId);
        QueryWrapper<ItemCell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id", sopControl.getEntityItem());
        queryWrapper.gt("qty", 0);
        queryWrapper.in("manage_way", 2, 3);
        queryWrapper.and(item -> item.or().eq("storage_code", code).or().eq("area_code", code).or().eq("house_code", code));
        List<ItemCell> list = itemCellService.list(queryWrapper);
        return Result.ok(list);
    }

    /**
     * 入库时通过标签码查询物料单元
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "物料单元-入库时通过标签码查询物料单元", notes = "物料单元-入库时通过标签码查询物料单元")
    @GetMapping(value = "/queryItemCellByQrCodeInStorage")
    public Result<?> queryItemCellByQrCodeInStorage(@RequestParam(name = "qrcode", required = true) String qrcode,
                                                    @RequestParam(name = "storageCode", required = false) String storageCode,
                                                    @RequestParam(name = "controlId", required = false) String controlId) {
        ItemCell itemCell = itemCellService.queryItemCellByQrCodeInStorage(qrcode, storageCode);
        if (!StringUtils.isEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);

            if (!sopControl.getEntityItem().equals(itemCell.getItemId())) {
                throw new ILSBootException("P-SOP-0096");
            }
        }
        if (itemCell == null) {
            ItemContainerManageVO itemContainerManageVO = itemContainerManageService.queryDetailByQrcode(qrcode);
            itemContainerManageVO.setItemContainerLoadItemRecordList(null);
            itemContainerManageVO.setItemContainerTransferRecordList(null);
            return Result.ok(itemContainerManageVO);
        } else {
            boolean inContainer = itemContainerManageService.isInContainer(qrcode);
            if(inContainer){
                throw new ILSBootException("该物料在载具中，不能单独入库");
            }
            return Result.ok(itemCell);
        }

    }

    /**
     * 出库时通过标签码查询物料单元
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "物料单元-出库时通过标签码查询物料单元", notes = "物料单元-出库时通过标签码查询物料单元")
    @GetMapping(value = "/queryItemCellByQrCodeOutStorage")
    public Result<?> queryItemCellByQrCodeOutStorage(@RequestParam(name = "qrcode", required = true) String qrcode,
                                                     @RequestParam(name = "controlId", required = false) String controlId) {
        ItemCell itemCell = itemCellService.queryItemCellByQrCodeOutStorage(qrcode);
        if (!StringUtils.isEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);

            WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());

            QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("produce_task_id",produceTask.getId());
            queryWrapper.eq("qrcode",qrcode);
            queryWrapper.eq("is_deleted",ZeroOrOneEnum.ZERO.getStrCode());
            List<WorkProduceRecord> list = workProduceRecordService.list(queryWrapper);
            if (CollectionUtil.isEmpty(list)){
                throw new ILSBootException("P-SOP-0103");
            }
        }

        if (itemCell == null) {
            ItemContainerManageVO itemContainerManageVO = itemContainerManageService.queryDetailByQrcode(qrcode);
            itemContainerManageVO.setItemContainerLoadItemRecordList(null);
            itemContainerManageVO.setItemContainerTransferRecordList(null);
            return Result.ok(itemContainerManageVO);
        } else {
            boolean inContainer = itemContainerManageService.isInContainer(qrcode);
            if(inContainer){
                throw new ILSBootException("该物料在载具中，不能单独出库");
            }
            return Result.ok(itemCell);
        }
    }
    /**
     * 出库时通过标签码查询物料单元
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "物料单元-发货时通过标签码查询物料单元", notes = "物料单元-发货时通过标签码查询物料单元")
    @GetMapping(value = "/queryItemCellByQrCodeLabelCode")
    public Result<?> queryItemCellByQrCodeLabelCode(@RequestParam(name = "qrcode", required = true) String qrcode,
                                                     @RequestParam(name = "controlId", required = false) String controlId) {
        ItemCell itemCell = itemCellService.queryItemCellByQrCodeOutStorage(qrcode);
        if (!StringUtils.isEmpty(controlId)) {
            SopControl sopControl = sopControlService.getById(controlId);

            WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());

            QueryWrapper<WorkProduceRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("produce_task_id",produceTask.getId());
            queryWrapper.eq("qrcode",qrcode);
            queryWrapper.eq("is_deleted",ZeroOrOneEnum.ZERO.getStrCode());
            List<WorkProduceRecord> list = workProduceRecordService.list(queryWrapper);
            if (CollectionUtil.isEmpty(list)){
                throw new ILSBootException("P-SOP-0103");
            }
        }

        if (itemCell == null) {
            throw new ILSBootException("无效二维码，请检查二维码！");
        } else {
            return Result.ok(itemCell);
        }
    }

    /**
     * 物料追溯（标签码）
     *
     * @param qrCodeItemCellFollowVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料追溯（标签码）", notes = "物料追溯（标签码）")
    @GetMapping(value = "/queryMaterialLabelFollow")
    public Result<?> queryMaterialLabelFollow(QrCodeItemCellFollowVO qrCodeItemCellFollowVO,
                                              @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                              @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                              HttpServletRequest req) {
        QueryWrapper<QrCodeItemCellFollowVO> queryWrapper = QueryGenerator.initQueryWrapper(qrCodeItemCellFollowVO, req.getParameterMap());

        Page<QrCodeItemCellFollowVO> page = new Page<QrCodeItemCellFollowVO>(pageNo, pageSize);
        IPage<QrCodeItemCellFollowVO> pageQrCodeItemCellFollowVO = itemCellService.queryMaterialLabelFollow(page, queryWrapper);
        return Result.ok(pageQrCodeItemCellFollowVO);
    }

    /**
     * 标签码物料追溯
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料单元-标签码物料追溯", notes = "物料单元-标签码物料追溯")
    @GetMapping(value = "/traceItemCellQrcodeById")
    public Result<?> traceItemCellQrcodeById(@RequestParam(name = "id", required = true) String id) {
        TraceItemCellQrcodeVO traceItemCellQrcodeVO = itemCellService.traceItemCellWithQrcode(id);
        return Result.ok(traceItemCellQrcodeVO);
    }

    @ApiOperation(value = "物料单元-批次码物料追溯", notes = "物料单元-批次码物料追溯")
    @GetMapping(value = "/batchItemCellTraceById")
    public Result<?> batchItemCellTraceById(@RequestParam(name = "id", required = true) String id) {
        TraceItemCellQrcodeVO traceItemCellQrcodeVO = itemCellService.batchItemCellTraceById(id);
        return Result.ok(traceItemCellQrcodeVO);
    }

    /**
     * RF端批次码物料追溯
     *
     * @param batch
     * @param id
     * @return
     */
    @ApiOperation(value = "物料单元-RF端批次码物料追溯", notes = "物料单元-RF端批次码物料追溯")
    @GetMapping(value = "/rfBatchItemCellTrace")
    public Result<?> rfBatchItemCellTrace(@RequestParam(name = "batch", required = true) String batch,
                                          @RequestParam(name = "id", required = true) String id) {
        RfTraceRecordVO rfTraceRecordVO = itemCellService.rfBatchItemCellTrace(batch, id);
        return Result.ok(rfTraceRecordVO);
    }

    /**
     * RF端标签码物料追溯
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "物料单元-RF端标签码物料追溯", notes = "物料单元-RF端标签码物料追溯")
    @GetMapping(value = "/rfQrcodeItemCellTrace")
    public Result<?> rfQrcodeItemCellTrace(@RequestParam(name = "qrcode", required = true) String qrcode) {
        RfTraceRecordVO rfTraceRecordVO = itemCellService.rfQrcodeItemCellTrace(qrcode);
        return Result.ok(rfTraceRecordVO);
    }

    /**
     * 二维码扫描查询物料单元
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "物料单元-扫描二维码查询物料", notes = "物料单元-扫描二维码查询物料")
    @GetMapping(value = "/queryItemCellByQrcode")
    public Result<?> queryItemCellByQrcode(@RequestParam(name = "qrcode", required = true) String qrcode) {
        LambdaQueryWrapper<ItemCell> itemCellLambdaQueryWrapper = new LambdaQueryWrapper<>();
        itemCellLambdaQueryWrapper.eq(ItemCell::getQrcode, qrcode);
        itemCellLambdaQueryWrapper.eq(ItemCell::getDeleted, ZeroOrOneEnum.ZERO.getIcode());
        ItemCell itemCell = itemCellService.getOne(itemCellLambdaQueryWrapper);
        return Result.ok(itemCell);
    }

    /**
     * 查询批次码集合
     *
     * @param itemId
     * @return
     */
    @ApiOperation(value = "物料单元-查询批次码集合", notes = "物料单元-查询批次码集合")
    @GetMapping(value = "/queryItemBatchList")
    public Result queryItemBatchList(@RequestParam(name = "itemId", required = false) String itemId) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("item_id", itemId);
        itemCellQueryWrapper.isNotNull("batch");
        itemCellQueryWrapper.ne("batch", "");
        itemCellQueryWrapper.groupBy("batch");
        itemCellQueryWrapper.select("batch");
        List<ItemCell> list = itemCellService.list(itemCellQueryWrapper);
        String batchs = list.stream().map(ItemCell::getBatch).collect(Collectors.joining(","));
        return Result.ok(Arrays.asList(batchs.split(",")));
    }

    /**
     * 物料单元-普通发货（标签码）
     *
     * @param itemCellList
     * @return
     */
    @ApiOperation(value = "物料单元-普通发货（标签码）", notes = "物料单元-普通发货（标签码）")
    @PostMapping(value = "/labelCodeDeliveryGoods")
    public Result<?> labelCodeDeliveryGoods(@RequestBody List<ItemCell> itemCellList) {
        itemCellService.labelCodeDeliveryGoods(itemCellList);
        return Result.ok();
    }

    /**
     * 物料单元-普通发货（库存）
     *
     * @param itemCellList
     * @return
     */
    @ApiOperation(value = "物料单元-普通发货（库存）", notes = "物料单元-普通发货（库存）")
    @PostMapping(value = "/stocksOfDeliveryGoods")
    public Result<?> stocksOfDeliveryGoods(@RequestBody List<ItemCellOutStorageVO> itemCellList) {
        itemCellService.stocksOfDeliveryGoods(itemCellList);
        return Result.ok();
    }

    @ApiOperation(value = "物料单元-通过批次码查询物料单元", notes = "物料单元-通过批次码查询物料单元")
    @GetMapping(value = "/queryItemCellByBatch")
    public Result<?> queryItemCellByBatch(@RequestParam(name = "batch", required = true) String batch) {
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        itemCellQueryWrapper.eq("batch", batch);
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        itemCellQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<ItemCell> itemCellList = itemCellService.list(itemCellQueryWrapper);
        if (CollectionUtil.isEmpty(itemCellList)) {
            throw new ILSBootException("系统中没有该批次码，请重新输入");
        }
        return Result.ok(itemCellList);
    }

    /**
     * 通过当前工序的id查询不同物料的数量数据
     *
     * @return
     */
    @ApiOperation(value = "物料单元-通过当前工序的id查询不同物料的数量数据", notes = "物料单元-通过当前工序的id查询不同物料的数量数据")
    @GetMapping(value = "/queryNowProcessItemQty")
    public Result<?> queryNowProcessItemQty(@RequestParam(name = "processId", required = true) String processId) {
        return Result.ok(itemCellService.queryNowProcessItemQty(processId));
    }

    /**
     * 查询物料在制品数量和占比
     *
     * @return
     */
    @ApiOperation(value = "物料单元-查询物料在制品数量和占比", notes = "物料单元-查询物料在制品数量和占比")
    @GetMapping(value = "/queryItemProcessed")
    public Result<?> queryItemProcessed() {
        return Result.ok(itemCellService.queryAccountedForItemProcessed());
    }

    /**
     * 以工序为维度，查询每个工序下面的物料总数量
     *
     * @return
     */
    @ApiOperation(value = "物料单元-以工序为维度，查询每个工序下面的物料总数量", notes = "物料单元-以工序为维度，查询每个工序下面的物料总数量")
    @GetMapping(value = "/queryProcessedItemQty")
    public Result<?> queryProcessedItemQty() {
        return Result.ok(itemCellService.queryProcessedItemQty());
    }

    /**
     * 以工序统计物料数量看板
     *
     * @return
     */
    @ApiOperation(value = "物料单元-以工序统计物料数量看板", notes = "物料单元-以工序统计物料数量看板")
    @GetMapping(value = "/queryDashBoardProcessItemQtyVO")
    public Result<?> queryDashBoardProcessItemQtyVO() {
        return Result.ok(itemCellService.selectDashBoardProcessItemQtyVO());
    }

    @ApiOperation(value = "物料单元-通过sop模板控件id查询对应的物料单元", notes = "物料单元-通过sop模板控件id查询对应的物料单元")
    @GetMapping(value = "/queryByControlId")
    public Result queryItemCellBySopControlId(@RequestParam(name = "controlId", required = true) String controlId) {
        SopControl sopControl = sopControlService.getById(controlId);

        WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());
        //查询工位对应的完工仓库
        List<WareFeedingStorageRelateArea> wareFeedingStorageRelateAreas = wareFeedingStorageRelateAreaMapper.selectFeedingStorageByAreaId(produceTask.getStationId());

        if (wareFeedingStorageRelateAreas == null) {
            throw new ILSBootException("P-SOP-0094");
        }
        WareStorage wareStorage = wareStorageService.getById(wareFeedingStorageRelateAreas.get(0).getFeedingStorage());

        QueryWrapper<ItemCell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id", sopControl.getEntityItem());
        queryWrapper.gt("qty", 0);
        queryWrapper.in("manage_way", 2, 3);
        queryWrapper.and(item -> item.or().eq("storage_code", wareStorage.getStorageCode()).or().eq("area_code", wareStorage.getStorageCode()).or().eq("house_code", wareStorage.getStorageCode()));
        List<ItemCell> list = itemCellService.list(queryWrapper);
        return Result.ok(list);
    }

    @ApiOperation(value = "物料单元-通过sop模板控件id查询对应的完工仓库", notes = "物料单元-通过sop模板控件id查询对应的完工仓库")
    @GetMapping(value = "/queryStorageByControlId")
    public Result queryStorageByControlId(@RequestParam(name = "controlId", required = true) String controlId) {

        SopControl sopControl = sopControlService.getById(controlId);

        WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());
        //查询工位对应的完工仓库
        WareFinishedStorageRelateArea storage = wareFinishedStorageRelateAreaMapper.selectFinishStorageByAreaId(produceTask.getStationId());

        if (storage == null) {
            throw new ILSBootException("P-SOP-0094");
        }
        WareStorage wareStorage = wareStorageService.getById(storage.getFinishedStorage());
        return Result.ok(wareStorage);
    }

    @ApiOperation(value = "物料单元-通过sop模板控件id查询对应的完工仓库", notes = "物料单元-通过sop模板控件id查询对应的完工仓库")
    @GetMapping(value = "/queryFeedingStorageByControlId")
    public Result queryFeedingStorageByControlId(@RequestParam(name = "controlId", required = true) String controlId) {
        //能够出库的物料单元
        List<ItemCell> itemCellList = new ArrayList<>(16);
        //返回给前端的json
        JSONObject outStorage = new JSONObject();

        SopControl sopControl = sopControlService.getById(controlId);

        WorkProduceTask produceTask = workProduceTaskService.getById(sopControl.getRelatedTaskId());
        //查询工位对应的完工仓库
        WareFinishedStorageRelateArea wareFinishedStorageRelateArea = wareFinishedStorageRelateAreaMapper.selectFinishStorageByAreaId(produceTask.getStationId());

        if (wareFinishedStorageRelateArea == null) {
            throw new ILSBootException("P-SOP-0094");
        }

        outStorage.put("storage", wareStorageService.getById(wareFinishedStorageRelateArea.getFinishedStorage()));

        //查询产出记录
        QueryWrapper<WorkProduceRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("produce_task_id", sopControl.getRelatedTaskId());
        recordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        recordQueryWrapper.isNotNull("item_cell_state_id");
        recordQueryWrapper.groupBy("item_cell_state_id");
        recordQueryWrapper.select("item_cell_state_id");
        List<WorkProduceRecord> list = workProduceRecordService.list(recordQueryWrapper);

        //判断是否存在产出
        if (CollectionUtil.isNotEmpty(list)) {
            //存在产出，查询产出对应的物料单元
            for (WorkProduceRecord workProduceRecord : list) {
                if (StringUtils.isNotEmpty(workProduceRecord.getItemCellStateId())) {
                    //从任务中查询出 item_cell_state_id
                    QueryWrapper<ItemCell> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",workProduceRecord.getItemCellStateId());
                    queryWrapper.eq("is_deleted",ZeroOrOneEnum.ZERO.getStrCode());
                    queryWrapper.gt("qty",0);
                    List<ItemCell> cellList = itemCellService.list(queryWrapper);
                    itemCellList.addAll(cellList);
                }
            }
        }
        outStorage.put("itemCellList", itemCellList);
        return Result.ok(outStorage);
    }
}
