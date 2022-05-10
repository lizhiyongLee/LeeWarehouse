package com.ils.modules.mes.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.config.entity.DefineFieldValue;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 自定义字段值存储
 * @Author: hezhigang
 * @Date:   2020-10-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="自定义字段值存储")
@RestController
@RequestMapping("/config/defineFieldValue")
public class DefineFieldValueController extends ILSController<DefineFieldValue, DefineFieldValueService> {
	@Autowired
	private DefineFieldValueService defineFieldValueService;
	
	/**
     * 自定义字段值-查询
     *
     * @param defineFieldValue
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "自定义字段值-查询", notes = "自定义字段值-表查询")
    @GetMapping(value = "/list")
    public Result<?> queryTableDefineList(@RequestParam(name = "tableCode", required = true) String tableCode,
        @RequestParam(name = "mainId", required = false) String mainId) {
        List<DefineFieldValueVO> lstDefineFields = defineFieldValueService.queryDefineFieldValue(tableCode, mainId);
        return Result.ok(lstDefineFields);
	}
	

	
}
