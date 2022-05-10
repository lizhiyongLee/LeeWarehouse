package com.ils.modules.mes.base.ware.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.common.api.vo.Result;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.service.WareFeedingStorageRelateAreaService;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.modules.mes.base.ware.service.WareStorageRelateAreaService;
import com.ils.modules.mes.base.ware.vo.WareRelateAreaVO;
import com.ils.modules.mes.util.TreeNode;
import lombok.extern.slf4j.Slf4j;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.constant.CommonConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 投料仓位
 * @Author: Tian
 * @Date: 2020-12-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "投料仓位")
@RestController
@RequestMapping("/base/ware/wareStorageRelateArea")
public class WareStorageRelateAreaController extends ILSController<WareFeedingStorageRelateArea, WareFeedingStorageRelateAreaService> {
    @Autowired
    private WareFeedingStorageRelateAreaService wareFeedingStorageRelateAreaService;
    @Autowired
    private WareStorageRelateAreaService wareStorageRelateAreaService;
    /**
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "关联仓位-查询", notes = "关联仓位查询")
    @GetMapping(value = "/queryStorageRelateArea")
    public Result<?> queryStorageRelateArea(HttpServletRequest req) {
        String status = req.getParameter("status");
        String name = req.getParameter("name");
        List<TreeNode> treeNodeList = wareStorageRelateAreaService.queryInstitutionTreeList(name, status);
        return Result.ok(treeNodeList);
    }

    @AutoLog("关联仓位，更新关联仓位")
    @ApiOperation(value = "关联仓位,更新关联仓位",notes = "更新关联仓位")
    @GetMapping(value = "/addfeedingStorageRelateArea")
    public Result<?> addFeedingStorage(HttpServletRequest req){
        String storageId = req.getParameter("storageId");
        String areaId = req.getParameter("areaId");
        wareStorageRelateAreaService.addFeedingStorageRelateArea(storageId,areaId);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    @AutoLog("关联仓位，更新关联仓位")
    @ApiOperation(value = "关联仓位，更新关联仓位",notes = "更新关联仓位")
    @GetMapping(value = "/addfinishedStorageRelateArea")
    public Result<?> addFinishedStorage(HttpServletRequest req){
        String storageId = req.getParameter("storageId");
        String areaId = req.getParameter("areaId");
        wareStorageRelateAreaService.addFinishedStorageRelateArea(storageId,areaId);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }

    @AutoLog(value = "通过参数查询关联仓位")
    @ApiOperation(value="通过参数查询关联仓位", notes="通过参数查询关联仓位")
    @GetMapping(value = "/queryRelateStorageByParameter")
    public Result<?> queryRelateStorageByParameter(@RequestParam(name="name",required=true) String name,@RequestParam(name="status",required=true) String status) {
        List<TreeNode> treeNodes = wareStorageRelateAreaService.queryRelateStorageByParameter(name, status);
        return Result.ok(treeNodes);
    }

    @AutoLog(value = "查询关联仓位")
    @ApiOperation(value="查询关联仓位", notes="查询关联仓位")
    @GetMapping(value = "/queryRelateHouse")
    public Result<?> queryRelateHouse(@RequestParam(name="parentId",required=false) String parentId) {
        List<TreeNode> treeNodes = wareStorageRelateAreaService.queryRelateHouse(parentId);
        return Result.ok(treeNodes);
    }

}
