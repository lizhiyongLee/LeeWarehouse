package com.ils.modules.mes.base.material.controller;

import com.alibaba.fastjson.JSONArray;
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
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.*;
import com.ils.modules.mes.base.material.service.*;
import com.ils.modules.mes.base.material.vo.ItemQualityVO;
import com.ils.modules.mes.base.material.vo.ItemSupplierVO;
import com.ils.modules.mes.base.material.vo.ItemVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.ItemManageWayEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 物料定义
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/material/item")
@Api(tags = "物料定义")
@Slf4j
public class ItemController extends ILSController<Item, ItemService> {
    private static final String NOT_ITEM_IDS = "notItemIds";
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemUnitService itemUnitService;
    @Autowired
    private ItemStockService itemStockService;
    @Autowired
    private ItemQualityEmployeeService itemQualityEmployeeService;
    @Autowired
    private ItemQualityService itemQualityService;
    @Autowired
    private ItemSupplierService itemSupplierService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    /**
     * 分页列表查询
     *
     * @param item
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料定义-分页列表查询", notes = "物料定义-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Item item,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Item> queryWrapper = QueryGenerator.initQueryWrapper(item, req.getParameterMap());

        String notItemId = req.getParameter(NOT_ITEM_IDS);
        if (StringUtils.isNotBlank(notItemId)) {
            queryWrapper.notIn("id", Arrays.asList(notItemId.split(",")));
        }

        Page<Item> page = new Page<Item>(pageNo, pageSize);
        IPage<Item> pageList = itemService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param itemPage
     * @return
     */
    @AutoLog("物料定义-添加")
    @ApiOperation(value = "物料定义-添加", notes = "物料定义-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemVO itemVO) {
        itemService.saveMain(itemVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param itemPage
     * @return
     */
    @AutoLog("物料定义-编辑")
    @ApiOperation(value = "物料定义-编辑", notes = "物料定义-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemVO itemVO) {
        itemService.updateMain(itemVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料定义-通过id删除")
    @ApiOperation(value = "物料定义-通过id删除", notes = "物料定义-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        itemService.delMain(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物料定义-批量删除")
    @ApiOperation(value = "物料定义-批量删除", notes = "物料定义-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.itemService.delBatchMain(Arrays.asList(ids.split(",")));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义-通过id查询", notes = "物料定义-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Item item = itemService.getById(id);
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(item, itemVO);
        List<ItemUnit> itemUnitList = itemUnitService.selectByMainId(id);
        itemVO.setItemUnitList(itemUnitList);
        List<ItemStock> itemStockList = itemStockService.selectByMainId(id);

        if (null != itemStockList && itemStockList.size() > 0) {
            itemVO.setItemStock(itemStockList.get(0));
        }
        List<ItemQualityEmployee> itemQualityEmployeeList = itemQualityEmployeeService.selectByMainId(id);
        if (null != itemQualityEmployeeList && itemQualityEmployeeList.size() > 0) {
            itemVO.setQualityEmployeeIds(itemQualityEmployeeList.stream()
                    .map(itemQualityEmployee -> itemQualityEmployee.getEmployeeId())
                    .collect(Collectors.joining(",")));
        }
        List<ItemQualityVO> itemQualityList = itemQualityService.selectByMainId(id);
        itemVO.setItemQualityList(itemQualityList);
        List<ItemSupplierVO> itemSupplierList = itemSupplierService.selectByMainId(id);
        itemVO.setItemSupplierList(itemSupplierList);
        List<DefineFieldValueVO> istDefineFields = defineFieldValueService.queryDefineFieldValue(TableCodeConstants.ITEM_TABLE_CODE, item.getId());
        itemVO.setLstDefineFields(istDefineFields);
        return Result.ok(itemVO);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义-通过物料定义id查询物料转换单位", notes = "物料定义-通过物料定义id查询物料转换单位")
    @GetMapping(value = "/queryItemUnitByMainId")
    public Result<?> queryItemUnitListByMainId(@RequestParam(name = "id", required = false) String id) {
        if (StringUtils.isBlank(id)) {
            return Result.ok(new ArrayList<Unit>(0));
        }
        List<Unit> lstUnit = itemService.queryItemUnitListByMainId(id);
        return Result.ok(lstUnit);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义-通过物料定义id查询物料关联库存", notes = "物料定义-通过物料定义id查询物料关联库存")
    @GetMapping(value = "/queryItemStockByMainId")
    public Result<?> queryItemStockListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ItemStock> itemStockList = itemStockService.selectByMainId(id);
        return Result.ok(itemStockList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义-通过物料定义id查询物料关联质检人员", notes = "物料定义-通过物料定义id查询物料关联质检人员")
    @GetMapping(value = "/queryItemQualityEmployeeByMainId")
    public Result<?> queryItemQualityEmployeeListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ItemQualityEmployee> itemQualityEmployeeList = itemQualityEmployeeService.selectByMainId(id);
        return Result.ok(itemQualityEmployeeList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义-通过物料定义id查询物料关联质检方案", notes = "物料定义-通过物料定义id查询物料关联质检方案")
    @GetMapping(value = "/queryItemQualityByMainId")
    public Result<?> queryItemQualityListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ItemQualityVO> itemQualityList = itemQualityService.selectByMainId(id);
        return Result.ok(itemQualityList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义-通过物料定义id查询物料关联供应商", notes = "物料定义-通过物料定义id查询物料关联供应商")
    @GetMapping(value = "/queryItemSupplierByMainId")
    public Result<?> queryItemSupplierListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ItemSupplierVO> itemSupplierList = itemSupplierService.selectByMainId(id);
        return Result.ok(itemSupplierList);
    }

    @ApiOperation(value = "物料定义-通过物料定义id查询物料管理方式", notes = "物料定义-通过物料定义id查询物料管理方式")
    @GetMapping("/queryItemManageWayById")
    public Result<?> queryItemManageWayById(@RequestParam(name = "id", required = true) String id) {
        Item item = itemService.getById(id);
        String manageWay = null;
        //判断是标签管理还是无码还是批次吗
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(item.getQrcode())) {
            if (ZeroOrOneEnum.ZERO.getStrCode().equals(item.getBatch())) {
                manageWay = ItemManageWayEnum.NONE_MANAGE.getValue();
            } else {
                manageWay = ItemManageWayEnum.BATCH_MANAGE.getValue();
            }
        } else {
            manageWay = ItemManageWayEnum.QRCODE_MANAGE.getValue();
        }
        return Result.ok(manageWay);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param item
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Item item) {
        // Step.1 组装查询条件
        QueryWrapper<Item> queryWrapper = QueryGenerator.initQueryWrapper(item, request.getParameterMap());
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<ItemVO> pageList = new ArrayList<ItemVO>();
        List<Item> itemList = itemService.list(queryWrapper);

        List<String> idList = new ArrayList<>();
        for (Item temp : itemList) {
            idList.add(temp.getId());
        }
        //2.1 取出符合条件的所有数据
        getPageList(pageList, itemList, idList);

        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "物料定义");
        mv.addObject(NormalExcelConstants.CLASS, ItemVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料定义数据", "导出人:" + sysUser.getRealname(), "物料定义"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 导出数据赋值
     *
     * @param pageList
     * @param itemList
     * @param idList
     */
    private void getPageList(List<ItemVO> pageList, List<Item> itemList, List<String> idList) {
        QueryWrapper<ItemUnit> itemUnitQueryWrapper = new QueryWrapper<>();
        itemUnitQueryWrapper.in("item_id", idList);
        List<ItemUnit> itemUnitList = itemUnitService.list(itemUnitQueryWrapper);

        QueryWrapper<ItemSupplier> itemSupplierQueryWrapper = new QueryWrapper<>();
        itemUnitQueryWrapper.in("item_id", idList);
        List<ItemSupplier> itemSupplierList = itemSupplierService.list(itemSupplierQueryWrapper);

        QueryWrapper<ItemQualityEmployee> itemQualityEmployeeQueryWrapper = new QueryWrapper<>();
        itemUnitQueryWrapper.in("item_id", idList);
        List<ItemQualityEmployee> itemQualityEmployeeList = itemQualityEmployeeService.list(itemQualityEmployeeQueryWrapper);

        QueryWrapper<ItemQuality> itemQualityQueryWrapper = new QueryWrapper<>();
        itemUnitQueryWrapper.in("item_id", idList);
        List<ItemQuality> itemQualityList = itemQualityService.list(itemQualityQueryWrapper);

        QueryWrapper<ItemStock> itemStockQueryWrapper = new QueryWrapper<>();
        itemUnitQueryWrapper.in("item_id", idList);
        List<ItemStock> itemStockList = itemStockService.list(itemStockQueryWrapper);
        //2.2 数据分类
        for (Item temp : itemList) {
            List<ItemVO> tempItemVOList = new ArrayList<>();
            List<ItemVO> itemVOList = new ArrayList<>();
            //单位
            itemUnitList.forEach(itemUnit -> {
                if (itemUnit.getItemId().equals(temp.getId())) {
                    ItemVO vo = new ItemVO();
                    BeanUtils.copyProperties(temp, vo);
                    vo.setItemUnitList(Collections.singletonList(itemUnit));
                    itemVOList.add(vo);
                }
            });
            //供应商
            itemSupplierList.forEach(itemSupplier -> {
                if (itemSupplier.getItemId().equals(temp.getId())) {
                    Iterator<ItemVO> iterator = itemVOList.iterator();
                    ItemSupplierVO itemSupplierVO = new ItemSupplierVO();
                    BeanUtils.copyProperties(itemSupplier, itemSupplierVO);
                    if (iterator.hasNext()) {
                        iterator.next().setItemSupplierList(Collections.singletonList(itemSupplierVO));
                    } else {
                        ItemVO vo = new ItemVO();
                        BeanUtils.copyProperties(temp, vo);
                        vo.setItemSupplierList(Collections.singletonList(itemSupplierVO));
                        tempItemVOList.add(vo);
                    }
                }
            });
            itemVOList.addAll(tempItemVOList);
            tempItemVOList.clear();
            //质检人员
            itemQualityEmployeeList.forEach(itemQualityEmployee -> {
                if (itemQualityEmployee.getItemId().equals(temp.getId())) {
                    Iterator<ItemVO> iterator = itemVOList.iterator();
                    if (iterator.hasNext()) {
                        iterator.next().setItemQualityEmployeeList(Collections.singletonList(itemQualityEmployee));
                    } else {
                        ItemVO vo = new ItemVO();
                        BeanUtils.copyProperties(temp, vo);
                        vo.setItemQualityEmployeeList(Collections.singletonList(itemQualityEmployee));
                        tempItemVOList.add(vo);
                    }
                }
            });
            itemVOList.addAll(tempItemVOList);
            tempItemVOList.clear();
            //质检方案
            itemQualityList.forEach(itemQuality -> {
                if (itemQuality.getItemId().equals(temp.getId())) {
                    Iterator<ItemVO> iterator = itemVOList.iterator();
                    ItemQualityVO itemQualityVO = new ItemQualityVO();
                    BeanUtils.copyProperties(itemQuality, itemQualityVO);
                    if (iterator.hasNext()) {
                        iterator.next().setItemQualityList(Collections.singletonList(itemQualityVO));
                    } else {
                        ItemVO vo = new ItemVO();
                        BeanUtils.copyProperties(temp, vo);
                        vo.setItemQualityList(Collections.singletonList(itemQualityVO));
                        tempItemVOList.add(vo);
                    }
                }
            });
            itemVOList.addAll(tempItemVOList);
            tempItemVOList.clear();
            //库存
            itemStockList.forEach(itemStock -> {
                if (itemStock.getItemId().equals(temp.getId())) {
                    if (itemVOList.size() > 0) {
                        itemVOList.forEach(vo -> vo.setItemStock(itemStock));
                    } else {
                        ItemVO vo = new ItemVO();
                        BeanUtils.copyProperties(temp, vo);
                        vo.setItemStock(itemStock);
                        itemVOList.add(vo);
                    }
                }
            });
            //null值置空
            itemVOList.forEach(vo -> {
                if (CommonUtil.isEmptyOrNull(vo.getItemQualityEmployeeList())) {
                    vo.setItemQualityEmployeeList(new ArrayList<>());
                }
                if (CommonUtil.isEmptyOrNull(vo.getItemQualityList())) {
                    vo.setItemQualityList(new ArrayList<>());
                }
                if (CommonUtil.isEmptyOrNull(vo.getItemUnitList())) {
                    vo.setItemUnitList(new ArrayList<>());
                }
                if (CommonUtil.isEmptyOrNull(vo.getItemSupplierList())) {
                    vo.setItemSupplierList(new ArrayList<>());
                }
                vo.setLstDefineFields(new ArrayList<>());
            });
            pageList.addAll(itemVOList);
        }
    }

    /**
     * 物料定义查看是否有产出业务数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料定义查看是否有产出业务数据-通过物料定义id查询", notes = "物料定义查看是否有产出业务数据-通过物料定义id查询")
    @GetMapping(value = "/queryBussDataByItemId")
    public Result<?> queryBussDataByItemId(@RequestParam(name = "itemId", required = true) String itemId) {
        int count = itemService.queryBussDataByItemId(itemId);
        if (count > 0) {
            throw new ILSBootException("B-FCT-0011");
        }
        return Result.ok();
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
        return super.importExcel(request, response, Item.class);
    }

    /**
     * 根据物料id查询主单位和转换单位
     *
     * @param itemId 物料id
     * @return
     */
    @ApiOperation(value = "根据物料id查询主单位和转换单位", notes = "根据物料id查询主单位和转换单位")
    @GetMapping(value = "/getUnitByItemId")
    public Result<?> getUnitByItemId(@RequestParam(name = "itemId", required = true) String itemId) {
        Item item = itemService.getById(itemId);
        JSONArray dataList = new JSONArray();
        //主单位
        JSONObject mainUnit = new JSONObject();
        String mainUnitId = item.getMainUnit();
        mainUnit.put("value", mainUnitId);
        mainUnit.put("text", unitService.getById(mainUnitId).getUnitName());
        mainUnit.put("title", unitService.getById(mainUnitId).getUnitName());
        dataList.add(mainUnit);
        //转换单位
        List<ItemUnit> itemUnits = itemUnitService.selectByMainId(itemId);
        if (!CommonUtil.isEmptyOrNull(itemUnits)) {
            for (ItemUnit itemUnit : itemUnits) {
                if (itemUnit.getStatus().equals(ZeroOrOneEnum.ONE.getStrCode())) {
                    JSONObject convertUnit = new JSONObject();
                    String convertUnitId = itemUnit.getConvertUnit();
                    convertUnit.put("value", convertUnitId);
                    convertUnit.put("text", unitService.getById(convertUnitId).getUnitName());
                    convertUnit.put("title", unitService.getById(convertUnitId).getUnitName());
                    dataList.add(convertUnit);
                }
            }
        }
        return Result.ok(dataList);
    }
}
