package com.ils.modules.mes.label.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.label.entity.LabelManage;
import com.ils.modules.mes.label.service.LabelManageService;
import com.ils.modules.mes.label.vo.LabelManageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:54
 */
@Slf4j
@Api(tags = "物料标签管理")
@RestController
@RequestMapping("/label/manage")
public class LabelManageController extends ILSController<LabelManage, LabelManageService> {
    @Autowired
    private LabelManageService labelManageService;

    /**
     * 分页列表查询
     *
     * @param labelManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "物料标签管理-分页列表查询", notes = "物料标签管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LabelManage labelManage,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   @RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String endTime,
                                   HttpServletRequest req) {
        IPage<LabelManageVO> pageList = labelManageService.queryPageList(labelManage, pageNo, pageSize, startTime, endTime, req);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param labelManageList
     * @return
     */
    @AutoLog("物料标签管理-添加")
    @ApiOperation(value = "物料标签管理-添加", notes = "物料标签管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody List<LabelManage> labelManageList) {
        labelManageList.forEach(labelManage -> labelManageService.saveMain(labelManage));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }


    /**
     * 编辑
     *
     * @param labelManage
     * @return
     */
    @AutoLog("物料标签管理-编辑")
    @ApiOperation(value = "物料标签管理-编辑", notes = "物料标签管理-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody LabelManage labelManage) {
        labelManageService.updateMain(labelManage);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料标签管理-通过id查询", notes = "物料标签管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LabelManage labelManage = labelManageService.getDetailById(id);
        return Result.ok(labelManage);
    }

    /**
     * 通过ids删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "物料标签管理-通过ids删除", notes = "物料标签管理-通过ids删除")
    @PostMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestBody String ids) {
        ids = (String) JSONObject.parseObject(ids).get("ids");
        List<String> idList = Arrays.asList(ids.split(","));
        labelManageService.removeByIds(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }


    /**
     * 批量完结
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "物料标签管理-批量完结", notes = "物料标签管理-批量完结")
    @PostMapping(value = "/completionBatch")
    public Result<?> completionBatch(@RequestBody String ids) {
        ids = (String) JSONObject.parseObject(ids).get("ids");
        List<String> idList = Arrays.asList(ids.split(","));
        labelManageService.completionBatch(idList);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param labelManage
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LabelManage labelManage) {
        return super.exportXls(request, labelManage, LabelManage.class, "物料标签管理");
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
        return super.importExcel(request, response, LabelManage.class);
    }
}
