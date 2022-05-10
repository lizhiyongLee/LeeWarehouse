package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.machine.entity.MachineFaultReason;
import com.ils.modules.mes.base.machine.service.MachineFaultReasonService;
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
 * 故障原因导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:06
 */
@Service
@Slf4j
public class MachineFaultReasonImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineFaultReasonService machineFaultReasonService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在的名字集合
        List<MachineFaultReason> list = machineFaultReasonService.list();
        List<String> names = list.stream().map(MachineFaultReason::getFaultCode).collect(Collectors.toList());

        //导入集合list
        List<MachineFaultReason> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineFaultReason machineFaultReason = BeanUtil.toBeanIgnoreError(data, MachineFaultReason.class);

            //判断如果名字已存在，则抛出导入失败的异常
            if (names.contains(machineFaultReason.getFaultCode())) {
                throw new ILSBootException("P-MC-0109", machineFaultReason.getFaultCode());
            }
            machineFaultReason.setTenantId(CommonUtil.getTenantId());
            machineFaultReason.setCreateBy(loginUser.getUsername());
            machineFaultReason.setCreateTime(currentDate);
            machineFaultReason.setUpdateBy(loginUser.getUsername());
            machineFaultReason.setUpdateTime(currentDate);
            machineFaultReason.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineFaultReason);
        }
        machineFaultReasonService.saveBatch(importList);
    }
}
