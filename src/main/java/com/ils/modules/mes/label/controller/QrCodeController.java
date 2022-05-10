package com.ils.modules.mes.label.controller;

import com.google.zxing.WriterException;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.QrCodeUtil;
import com.ils.modules.mes.util.SnowflakeConfig;
import com.ils.modules.system.service.CodeGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/30 14:59
 */
@Slf4j
@Api(tags = "二维码管理")
@RestController
@RequestMapping("/label/qrCode")
public class QrCodeController {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    SnowflakeConfig snowflakeConfig;

    private static final String DEFAULT_CODE = "QRCODE_";

    @ApiOperation(value = "生成二维码", notes = "生成二维码")
    @GetMapping(value = "/generate")
    @AutoLog(value = "标签管理-生成二维码")
    public Result<?> generateQrCode(@RequestParam Integer number,
                                    @RequestParam(required = false) String data,
                                    @RequestParam(required = false) String customPrefix,
                                    @RequestParam(required = false) Integer width,
                                    @RequestParam(required = false) Integer height,
                                    @RequestParam(required = false) String codeRule) {
        Map<String, byte[]> qrCodeList = new LinkedHashMap<>(number);
        if (width == null) {
            width = 100;
        }
        if (height == null) {
            height = 100;
        }
        List<String> dataCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(data)) {
            String[] dataCodes = data.split(",");
            dataCodeList = Arrays.asList(dataCodes);
        }
        String code;
        for (int i = 0; i < number; i++) {
            //是否使用编码规则
            if (StringUtils.isNotBlank(codeRule)) {
                code = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), codeRule, null);
            } else {
                //是否使用自定义前缀
                if (StringUtils.isNotBlank(customPrefix)) {
                    code = customPrefix + "_" + snowflakeConfig.snowflakeId();
                } else {
                    code = DEFAULT_CODE + snowflakeConfig.snowflakeId();
                }
            }
            try {
                //是否自定义打印
                if (!CommonUtil.isEmptyOrNull(dataCodeList)) {
                    for (String codeData : dataCodeList) {
                        qrCodeList.put(codeData, QrCodeUtil.generateQrCodeImage(codeData, width, height));
                    }
                } else {
                    qrCodeList.put(code, QrCodeUtil.generateQrCodeImage(code, width, height));
                }
            } catch (WriterException | IOException e) {
                return Result.error("二维码生成失败，请检查数据是否正常或联系管理员处理。");
            }
        }
        return Result.ok(qrCodeList);
    }
}
