package com.ils.modules.mes.base.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareStorageMapper;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.base.ware.vo.*;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.util.TreeNode;
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
 * @Description: 仓位定义
 * @Author: Tian
 * @Date: 2020-11-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "仓位定义")
@RestController
@RequestMapping("/base/ware/wareStorage")
public class WareStorageController extends ILSController<WareStorage, WareStorageService> {
    @Autowired
    private WareStorageService wareStorageService;

    @Autowired
    private WareHouseService wareHouseService;

    @Autowired
    private WareStorageMapper wareStorageMapper;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "仓位定义-分页列表查询", notes = "仓位定义-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<?> queryPageList(
            @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            HttpServletRequest req) {
        Page<WareStorageVO> page = new Page<WareStorageVO>(pageNo, pageSize);
        IPage<WareStorageVO> pageList = wareHouseService.queryWareHouseList(page);
        return Result.ok(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param stationId
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "查询二级仓位-分页列表查询", notes = "查询二级仓位-分页列表查询")
    @GetMapping(value = "/querySecondStorageByStationId")
    public Result<?> querySecondStorageByStationIdList(
            @RequestParam(name = "stationId", required = true) String stationId,
            @RequestParam(name = "itemId", required = true) String itemId,
            @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        Page<ProduceWareStorageVO> page = new Page<ProduceWareStorageVO>(pageNo, pageSize);
        IPage<ProduceWareStorageVO> wareStoragePage = wareStorageService.selectStorageByStationId(stationId, itemId, page);
        return Result.ok(wareStoragePage);
    }

    /**
     * @param upStorageId
     * @return
     */
    @ApiOperation(value = "通过childid查询", notes = "通过childid查询")
    @GetMapping(value = "/childList")
    public Result<?> queryByChildList(@RequestParam(name = "upStorageId", required = false) String upStorageId) {
        if (upStorageId.isEmpty()) {
            upStorageId = "0";
        }
        List<WareStorageVO> wareStorageVOList = wareStorageService.selcetChildList(upStorageId);
        ReturnListWareStorageVO returnListWareStorageVO = new ReturnListWareStorageVO();
        returnListWareStorageVO.setWareStorageVOList(wareStorageVOList);
        return Result.ok(returnListWareStorageVO);
    }


    /**
     * 查询仓位上级区域，以树形结构展示
     *
     * @return
     */
    @ApiOperation(value = "查询上级区域，以树形结构展示-树形查询", notes = "查询上级区域，以树形结构展示-树形查询")
    @RequestMapping(value = "/queryShopLineTreeList", method = RequestMethod.GET)
    public Result<?> queryShopLineTreeList() {
        List<TreeNode> treeNodes = wareStorageService.queryWareStorageTreeList();
        return Result.ok(treeNodes);
    }

    /**
     * 添加
     *
     * @param wareStorageVO
     * @return
     */
    @AutoLog(value = "仓位定义-添加",logType = CommonConstant.OPERATE_TYPE_ADD)
    @ApiOperation(value = "仓位定义-添加", notes = "仓位定义-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WareStorageVO wareStorageVO) {
        wareStorageService.saveWareStorage(wareStorageVO);
        return Result.ok(wareStorageVO);
    }

    /**
     * 编辑
     *
     * @param wareStorageVO
     * @return
     */
    @AutoLog(value = "仓位定义-编辑",logType = CommonConstant.OPERATE_TYPE_EDIT)
    @ApiOperation(value = "仓位定义-编辑", notes = "仓位定义-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody WareStorageVO wareStorageVO) {
        wareStorageService.updateWareStorage(wareStorageVO);
        return Result.ok(wareStorageVO);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "仓位定义-通过id查询", notes = "仓位定义-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WareStorage wareStorage = wareStorageService.getById(id);
        WareStorageVO wareStorageVO = new WareStorageVO();
        BeanUtils.copyProperties(wareStorage, wareStorageVO);
        List<DefineFieldValueVO> lstDefineFields =
                defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WARE_STORAGE_TABLE_CODE, id);
        wareStorageVO.setLstDefineFields(lstDefineFields);
        return Result.ok(wareStorageVO);
    }

    /**
     * 通过车间id查询仓位树形结构
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "仓位定义-通过id查询", notes = "仓位定义-通过id查询")
    @GetMapping(value = "/queryTreeStorageById")
    public Result<?> queryTreeStorage(@RequestParam(name = "id", required = true) String id) {
        List<TreeNode> treeNodeList = wareStorageService.queryTreeStorage(id);
        return Result.ok(treeNodeList);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param wareStorage
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WareStorage wareStorage) {
        return super.exportXls(request, wareStorage, WareStorage.class, "仓位定义");
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
        return super.importExcel(request, response, WareStorage.class);
    }
    /**
     * 查询仓位树形结构
     * @return
     */
    @ApiOperation(value = "收货入库时查询仓位树形结构", notes = "收货入库时查询仓位树形结构")
    @GetMapping(value = "/queryReceivingGoodsTreeStorage")
    public Result<?> queryReceivingGoodsTreeStorage(HttpServletRequest req){
        String status = req.getParameter("status");
        String name = req.getParameter("name");
        List<TreeNode> treeNodeList = service.queryReceivingGoodsTreeStorage(name,status);
        return Result.ok(treeNodeList);
    }

    /**
     * 通过仓库id查询该仓库下有哪些子仓位
     * @param wareHouseId
     * @return
     */
    @ApiOperation(value = "通过仓库id查询该仓库下有哪些子仓位",notes = "通过仓库id查询该仓库下有哪些子仓位")
    @GetMapping(value = "/queryWareStorageByWareHouseId")
    public Result<?> queryWareStorageByWareHouseId(@RequestParam(name = "wareHouseId",required = true)String wareHouseId){
        List<WareStorageListVO> wareStorageListVOList = wareStorageService.queryWareStorageByWareHouseId(wareHouseId);
        return Result.ok(wareStorageListVOList);
    }
}
