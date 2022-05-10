package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.machine.entity.MachineStopReason;
import com.ils.modules.mes.base.machine.service.MachineStopReasonService;
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
 * 停机原因导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:06
 */
@Service
@Slf4j
public class MachineStopReasonImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineStopReasonService machineStopReasonService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在的名字集合
        List<MachineStopReason> list = machineStopReasonService.list();
        List<String> names = list.stream().map(MachineStopReason::getStopCode).collect(Collectors.toList());

        //导入集合list
        List<MachineStopReason> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineStopReason machineStopReason = BeanUtil.toBeanIgnoreError(data, MachineStopReason.class);

            //判断如果名字已存在，则抛出导入失败的异常
            if (names.contains(machineStopReason.getStopCode())) {
                throw new ILSBootException("P-MC-0109", machineStopReason.getStopCode());
            }
            machineStopReason.setTenantId(CommonUtil.getTenantId());
            machineStopReason.setCreateBy(loginUser.getUsername());
            machineStopReason.setCreateTime(currentDate);
            machineStopReason.setUpdateBy(loginUser.getUsername());
            machineStopReason.setUpdateTime(currentDate);
            machineStopReason.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineStopReason);
        }
        machineStopReasonService.saveBatch(importList);
    }
}
