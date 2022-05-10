package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.machine.entity.MachineFaultAppearance;
import com.ils.modules.mes.base.machine.service.MachineFaultAppearanceService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 故障现象导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:05
 */
@Service
@Slf4j
public class MachineFaultAppearanceImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineFaultAppearanceService machineFaultAppearanceService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在的名字集合
        List<MachineFaultAppearance> list = machineFaultAppearanceService.list();

        List<String> names = list.stream().map(MachineFaultAppearance::getFaultCode).collect(Collectors.toList());

        //导入集合list
        List<MachineFaultAppearance> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineFaultAppearance machineFaultAppearance = BeanUtil.toBeanIgnoreError(data, MachineFaultAppearance.class);

            //判断如果编码已存在，则抛出导入失败的异常
            if (names.contains(machineFaultAppearance.getFaultCode())) {
                throw new ILSBootException("P-MC-0108", machineFaultAppearance.getFaultCode());
            }
            machineFaultAppearance.setTenantId(CommonUtil.getTenantId());
            machineFaultAppearance.setCreateBy(loginUser.getUsername());
            machineFaultAppearance.setCreateTime(currentDate);
            machineFaultAppearance.setUpdateBy(loginUser.getUsername());
            machineFaultAppearance.setUpdateTime(currentDate);
            machineFaultAppearance.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineFaultAppearance);
        }
        machineFaultAppearanceService.saveBatch(importList);
    }
}
