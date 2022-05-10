package com.ils.modules.mes.material.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.material.entity.ItemContainer;
import com.ils.modules.mes.base.material.entity.ItemContainerQty;
import com.ils.modules.mes.base.material.service.ItemContainerService;
import com.ils.modules.mes.base.material.vo.ItemContainerVO;
import com.ils.modules.mes.enums.ItemCellPositionStatusEnum;
import com.ils.modules.mes.enums.ItemCellQrcodeStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemContainerManage;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.material.service.ItemContainerManageService;
import com.ils.modules.mes.material.vo.ItemCellStorageVO;
import com.ils.modules.mes.material.vo.ItemContainerLoadVO;
import com.ils.modules.mes.material.vo.ItemContainerManageVO;
import com.ils.modules.mes.material.vo.ItemContainerStorageVO;
import com.ils.modules.mes.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/27 15:47
 */
@Slf4j
@Api(tags = "载具管理")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/material/ItemContainerManage")
public class ItemContainerManageController extends ILSController<ItemContainerManage, ItemContainerManageService> {
    @Autowired
    ItemContainerManageService itemContainerManageService;
    @Autowired
    ItemContainerService itemContainerService;

    /**
     * 分页查询
     *
     * @param itemContainerManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "根据id查询详情")
    @GetMapping(value = "/list")
    public Result<?> list(ItemContainerManage itemContainerManage,
                          @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                          @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<ItemContainerManage> queryWrapper = QueryGenerator.initQueryWrapper(itemContainerManage, req.getParameterMap());
        Page<ItemContainerManage> page = new Page<>(pageNo, pageSize);
        IPage<ItemContainerManage> pageList = itemContainerManageService.listPage(queryWrapper, page);
        return Result.ok(pageList);
    }

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情")
    @GetMapping(value = "/queryDetailById")
    public Result<?> queryDetailById(@RequestParam String id) {
        ItemContainerManageVO itemContainerManageVO = itemContainerManageService.queryDetailById(id);
        return Result.ok(itemContainerManageVO);
    }

    /**
     * 根据标签码查询详情
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "根据标签码查询详情", notes = "根据标签码查询详情")
    @GetMapping(value = "/queryDetailByQrcode")
    public Result<?> queryDetailByQrcode(@RequestParam String qrcode) {
        ItemContainerManageVO itemContainerManageVO = itemContainerManageService.queryDetailByQrcode(qrcode);
        return Result.ok(itemContainerManageVO);
    }

    /**
     * 根据标签码检查物料单元
     *
     * @param qrcode
     * @return
     */
    @ApiOperation(value = "根据标签码检查物料单元", notes = "根据标签码检查物料单元")
    @GetMapping(value = "/checkItemCellByQrcode")
    public Result<?> checkItemCellByQrcode(@RequestParam String containerQrcode,@RequestParam String itemCellQrcode) {
        ItemCell itemCell = itemContainerService.checkItemCellByQrcode(containerQrcode,itemCellQrcode);
        if(null==itemCell){
            throw new ILSBootException("当前物料不在该载具的物料列表中。");
        }
        return Result.ok(itemCell);
    }


    /**
     * 装载/卸载物料
     *
     * @param itemContainerManageLoadVO
     * @return
     */
    @ApiOperation(value = "载具装载/卸载物料", notes = "载具装载/卸载物料")
    @PostMapping(value = "/loadItemCell")
    public Result<?> loadItemCell(@RequestBody ItemContainerLoadVO itemContainerManageLoadVO) {
        itemContainerManageService.loadItemCell(itemContainerManageLoadVO);
        return Result.ok(itemContainerManageLoadVO);
    }

    /**
     * 载具入库
     *
     * @param itemContainerStorageVO
     * @return
     */
    @ApiOperation(value = "载具入库", notes = "载具入库")
    @PostMapping(value = "/inStorage")
    public Result<?> inStorage(@RequestBody ItemContainerStorageVO itemContainerStorageVO) {
        itemContainerManageService.inStorage(itemContainerStorageVO);
        return Result.ok(itemContainerStorageVO);
    }

    /**
     * 载具出库
     *
     * @param itemContainerStorageVO
     * @return
     */
    @PostMapping(value = "/outStorage")
    @ApiOperation(value = "载具出库", notes = "物载具出库")
    public Result<?> outStorage(@RequestBody ItemContainerStorageVO itemContainerStorageVO) {
        itemContainerManageService.outStorage(itemContainerStorageVO);
        return Result.ok(itemContainerStorageVO);
    }
}
