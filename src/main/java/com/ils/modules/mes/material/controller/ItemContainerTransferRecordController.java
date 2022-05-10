package com.ils.modules.mes.material.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.sop.service.SopTemplateService;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareFeedingStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.mapper.WareHouseMapper;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.enums.ItemManageWayEnum;
import com.ils.modules.mes.enums.ItemTransferStatusEnum;
import com.ils.modules.mes.enums.SopControlLogic;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.material.entity.ItemContainerTransferRecord;
import com.ils.modules.mes.material.service.ItemContainerTransferRecordService;
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
@RequestMapping("/material/itemContainerTransferRecord")
public class ItemContainerTransferRecordController extends ILSController<ItemContainerTransferRecord, ItemContainerTransferRecordService> {
    @Autowired
    private ItemContainerTransferRecordService itemContainerTransferRecordService;

    /**
     * 分页列表查询
     *
     * @param itemContainerTransferRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "转移记录-分页列表查询", notes = "转移记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemContainerTransferRecord itemContainerTransferRecord,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemContainerTransferRecord> queryWrapper = QueryGenerator.initQueryWrapper(itemContainerTransferRecord, req.getParameterMap());
        Page<ItemContainerTransferRecord> page = new Page<>(pageNo, pageSize);
        IPage<ItemContainerTransferRecord> pageList = itemContainerTransferRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
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
        ItemContainerTransferRecord itemContainerTransferRecord = itemContainerTransferRecordService.getById(id);
        return Result.ok(itemContainerTransferRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param itemContainerTransferRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemContainerTransferRecord itemContainerTransferRecord) {
        return super.exportXls(request, itemContainerTransferRecord, ItemContainerTransferRecord.class, "转移记录");
    }

}
