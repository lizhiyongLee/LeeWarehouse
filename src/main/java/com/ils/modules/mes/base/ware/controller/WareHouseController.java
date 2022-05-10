package com.ils.modules.mes.base.ware.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.vo.WareHouseVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
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
 * @Description: 仓库定义
 * @Author: Tian
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "仓库定义")
@RestController
@RequestMapping("/base/ware/wareHouse")
public class WareHouseController extends ILSController<WareHouse, WareHouseService> {
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    /**
     * 分页列表查询
     *
     * @param wareHouse
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "仓库定义-分页列表查询", notes = "仓库定义-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WareHouse wareHouse,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WareHouse> queryWrapper = QueryGenerator.initQueryWrapper(wareHouse, req.getParameterMap());
        Page<WareHouse> page = new Page<>(pageNo, pageSize);
        IPage<WareHouse> pageList = wareHouseService.page(page, queryWrapper);
        return Result.ok(pageList);
    }


    /**
     * 添加
     *
     * @param wareHouse
     * @return
     */
    @AutoLog("仓库定义-添加")
    @ApiOperation(value = "仓库定义-添加", notes = "仓库定义-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WareHouseVO wareHouseVO) {
        wareHouseService.saveWareHouse(wareHouseVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    /**
     * 编辑
     *
     * @param wareHouse
     * @return
     */
    @AutoLog("仓库定义-添加")
    @ApiOperation(value = "仓库定义-编辑", notes = "仓库定义-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WareHouseVO wareHouseVO) {
        wareHouseService.updateWareHouse(wareHouseVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "仓库定义-通过id删除")
    @ApiOperation(value = "仓库定义-通过id删除", notes = "仓库定义-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wareHouseService.delWareHouse(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "仓库定义-批量删除")
    @ApiOperation(value = "仓库定义-批量删除", notes = "仓库定义-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        this.wareHouseService.delBatchWareHouse(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "仓库定义-通过id查询", notes = "仓库定义-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WareHouse wareHouse = wareHouseService.getById(id);
        WareHouseVO wareHouseVO = new WareHouseVO();
        BeanUtils.copyProperties( wareHouse,wareHouseVO);
        List<DefineFieldValueVO> lstDefineFields =
                defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WARE_HOUSE_TABLE_CODE, id);
        wareHouseVO.setLstDefineFields(lstDefineFields);
        return Result.ok(wareHouseVO);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param wareHouse
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WareHouse wareHouse) {
        return super.exportXls(request, wareHouse, WareHouse.class, "仓库定义");
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
        return super.importExcel(request, response, WareHouse.class);
    }

    /**
     * 查询仓库
     *
     * @return
     */
    @ApiOperation(value = "查询仓库", notes = "查询仓库")
    @GetMapping(value = "/queryWareHouseList")
    public Result<?> queryWareHouseList() {
        List<DictModel> wareHouseList = wareHouseService.queryWareHouseList();
        return Result.ok(wareHouseList);
    }

    @ApiOperation(value = "仓库定义-查询所以仓库", notes = "仓库定义-查询所以仓库")
    @GetMapping(value = "/queryWareHouse")
    public Result<?> queryWareHouseCode() {
        List<WareHouse> wareHouseList = wareHouseService.queryWareHouse();
        return Result.ok(wareHouseList);
    }
}
