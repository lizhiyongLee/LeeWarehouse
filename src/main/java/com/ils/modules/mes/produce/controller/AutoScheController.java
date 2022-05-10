package com.ils.modules.mes.produce.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.service.AutoScheService;
import com.ils.modules.mes.produce.vo.AutoScheParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 自动排程
 * @author: fengyi
 * @date: 2021年2月24日 下午4:49:48
 */
@Slf4j
@Api(tags = "自动排程")
@RestController
@RequestMapping("/produce/autoSche")
public class AutoScheController extends ILSController<WorkProcessTask, AutoScheService> {

    public static final String P_AU_10081 = "P-AU-10081";
    @Autowired
    private AutoScheService autoScheService;

    /**
     * 自动排程
     *
     * @param autoScheParam
     * @return
     */
    @AutoLog("自动排程")
    @ApiOperation(value = "自动排程", notes = "自动排程")
    @PostMapping(value = "/schedule")
    public Result<?> schedule(@RequestBody AutoScheParam autoScheParam) {
        // 接收的参数 = 待排程的数据list + 排程规则全局配置 + 排程基准时间

        // 基准时间
        if (null == autoScheParam.getBaseTime()) {
            throw new ILSBootException("P-AU-0068");
        }

        // 规则全局配置
        if (null == autoScheParam.getScheAutoRuleConfigure()) {
            throw new ILSBootException("P-AU-0069");
        }

        // 待排程工序任务
        if (CollectionUtil.isEmpty(autoScheParam.getLstAutoScheProcessVO())) {
            throw new ILSBootException("P-AU-0070");
        }

        try {
            autoScheService.autoSche(autoScheParam);
        } catch (ILSBootException e) {
            if (P_AU_10081.equals(e.getErrorCode())) {
                return Result.error(10081, e.getErrorCode());
            }
            throw e;
        }


        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
    }
}
