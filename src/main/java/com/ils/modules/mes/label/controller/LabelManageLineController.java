package com.ils.modules.mes.label.controller;

import com.alibaba.fastjson.JSONObject;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.label.entity.LabelManage;
import com.ils.modules.mes.label.entity.LabelManageLine;
import com.ils.modules.mes.label.service.LabelManageLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:54
 */
@Slf4j
@Api(tags = "物料标签行管理")
@RestController
@RequestMapping("/label/manageLine")
public class LabelManageLineController extends ILSController<LabelManageLine, LabelManageLineService> {
    @Autowired
    private LabelManageLineService labelManageLineService;

    /**
     * 批量打印
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料标签行管理-批量打印", notes = "物料标签行管理-批量打印")
    @PostMapping(value = "/printingBatch")
    public Result<?> printingBatch(@RequestBody String ids) {
        ids = (String) JSONObject.parseObject(ids).get("ids");
        List<String> idList = Arrays.asList(ids.split(","));
        labelManageLineService.printingBatch(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 批量作废
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料标签行管理-批量作废", notes = "物料标签行管理-批量作废")
    @PostMapping(value = "/cancelBatch")
    public Result<?> cancelBatch(@RequestBody String ids) {
        ids = (String) JSONObject.parseObject(ids).get("ids");
        List<String> idList = Arrays.asList(ids.split(","));
        labelManageLineService.cancelBatch(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }


    /**
     * 通过ids删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料标签行管理-通过ids删除", notes = "物料标签行管理-通过ids删除")
    @PostMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestBody String ids) {
        ids = (String) JSONObject.parseObject(ids).get("ids");
        List<String> idList = Arrays.asList(ids.split(","));
        labelManageLineService.removeByIds(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料标签管理-通过id查询", notes = "物料标签管理-通过id查询")
    @GetMapping(value = "/queryByCode")
    public Result<?> queryByCode(@RequestParam(name = "code", required = true) String code) {
        LabelManageLine labelManageLine = labelManageLineService.queryByCode(code);
        return Result.ok(labelManageLine);
    }
}
