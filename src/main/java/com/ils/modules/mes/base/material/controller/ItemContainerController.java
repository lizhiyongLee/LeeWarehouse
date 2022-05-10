package com.ils.modules.mes.base.material.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.craft.entity.ProcessStation;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.entity.ItemContainer;
import com.ils.modules.mes.base.material.entity.ItemType;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.service.ItemContainerService;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.material.service.ItemTypeService;
import com.ils.modules.mes.base.material.service.ItemUnitService;
import com.ils.modules.mes.base.material.vo.ItemContainerVO;
import com.ils.modules.mes.base.material.vo.ItemVO;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
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
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 物料载具
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "物料载具")
@RestController
@RequestMapping("/base/material/itemContainer")
public class ItemContainerController extends ILSController<ItemContainer, ItemContainerService> {
    @Autowired
    private ItemContainerService itemContainerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ItemUnitService itemUnitService;
    @Autowired
    private ItemTypeService itemTypeService;

    /**
     * 分页列表查询
     *
     * @param itemContainer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料载具-分页列表查询", notes = "物料载具-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemContainer itemContainer,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemContainer> queryWrapper = QueryGenerator.initQueryWrapper(itemContainer, req.getParameterMap());
        Page<ItemContainer> page = new Page<ItemContainer>(pageNo, pageSize);
        IPage<ItemContainer> pageList = itemContainerService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param itemContainerVO
     * @return
     */
    @AutoLog("物料载具-添加")
    @ApiOperation(value = "物料类型-添加", notes = "物料类型-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemContainerVO itemContainerVO) {
        itemContainerService.saveMain(itemContainerVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param itemContainerVO
     * @return
     */
    @AutoLog("物料载具-编辑")
    @ApiOperation(value = "物料类型-编辑", notes = "物料类型-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemContainerVO itemContainerVO) {
        itemContainerService.updateMain(itemContainerVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 停用启用物料载具
     *
     * @param itemContainerVO
     * @return
     */
    @ApiOperation(value = "物料载具-停用启用", notes = "物料载具-停用启用")
    @PostMapping(value = "/changeStatus")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<?> changeStatus(@RequestBody ItemContainerVO itemContainerVO) {
        itemContainerService.updateById(itemContainerVO);
        return Result.ok();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ItemContainerVO itemContainerVO = itemContainerService.selectById(id);
        itemContainerVO.getItemContainerQtyList().forEach(itemContainerQtyVO -> itemContainerQtyVO.setUnitList(getUnitByItemId(itemContainerQtyVO.getItemId())));
        return Result.ok(itemContainerVO);
    }

    /**
     * 获取物料列表
     *
     * @return
     */
    @ApiOperation(value = "获取物料列表", notes = "获取物料列表")
    @GetMapping(value = "/queryItemList")
    public Result<?> queryItemList(Item queryItem,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        QueryWrapper<Item> itemQueryWrapper = QueryGenerator.initQueryWrapper(queryItem, new HashMap<>());
        itemQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        Page<Item> page = new Page<>(pageNo, pageSize);
        Page<Item> itemPage = itemService.page(page, itemQueryWrapper);
        Page<Object> dataPage = new Page<>();
        List<Item> itemList = itemPage.getRecords();

        JSONArray jsonArray = new JSONArray();
        itemList.forEach(item -> {
            JSONObject itemObject = JSONObject.parseObject(JSONObject.toJSONString(item));
            itemObject.put("itemId", item.getId());
            Unit unit = unitService.getById(item.getMainUnit());
            itemObject.put("MainUnit_dictText", unit.getUnitName());
            ItemType itemType = itemTypeService.getById(item.getItemTypeId());
            itemObject.put("itemTypeId_dictText", itemType.getTypeName());
            itemObject.put("unitList", getUnitByItemId(item.getId()));
            jsonArray.add(itemObject);
        });
        BeanUtils.copyProperties(itemPage, dataPage);
        dataPage.setRecords(jsonArray);
        return Result.ok(dataPage);
    }

    private JSONArray getUnitByItemId(String itemId) {
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
                    Unit byId = unitService.getById(convertUnitId);
                    if (byId != null) {
                        convertUnit.put("text", byId.getUnitName());
                        convertUnit.put("title", byId.getUnitName());
                    }
                    dataList.add(convertUnit);
                }
            }
        }
        return dataList;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param itemContainer
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemContainer itemContainer) {
        // Step.1 组装查询条件
        QueryWrapper<ItemContainer> queryWrapper = QueryGenerator.initQueryWrapper(itemContainer, request.getParameterMap());
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<ItemContainerVO> pageList = new ArrayList<>();
        List<ItemContainer> list = itemContainerService.list(queryWrapper);

        list.forEach(container -> pageList.add(itemContainerService.selectById(container.getId())));

        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "物料载具");
        mv.addObject(NormalExcelConstants.CLASS, ItemContainerVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料载具", "导出人:" + sysUser.getRealname(), "物料载具定义"));
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
        return super.importExcel(request, response, ItemContainer.class);
    }
}
