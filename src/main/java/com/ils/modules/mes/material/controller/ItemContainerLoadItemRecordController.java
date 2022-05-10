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
import com.ils.modules.mes.material.entity.ItemContainerLoadItemRecord;
import com.ils.modules.mes.material.service.ItemContainerLoadItemRecordService;
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
@RequestMapping("/material/itemContainerLoadRecord")
public class ItemContainerLoadItemRecordController extends ILSController<ItemContainerLoadItemRecord, ItemContainerLoadItemRecordService> {
    @Autowired
    private ItemContainerLoadItemRecordService itemContainerLoadItemRecordService;

    /**
     * 分页列表查询
     *
     * @param itemContainerLoadItemRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "转移记录-分页列表查询", notes = "转移记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ItemContainerLoadItemRecord itemContainerLoadItemRecord,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ItemContainerLoadItemRecord> queryWrapper = QueryGenerator.initQueryWrapper(itemContainerLoadItemRecord, req.getParameterMap());
        Page<ItemContainerLoadItemRecord> page = new Page<>(pageNo, pageSize);
        IPage<ItemContainerLoadItemRecord> pageList = itemContainerLoadItemRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param itemContainerLoadItemRecord
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemContainerLoadItemRecord itemContainerLoadItemRecord) {
        return super.exportXls(request, itemContainerLoadItemRecord, ItemContainerLoadItemRecord.class, "装载记录");
    }

}
