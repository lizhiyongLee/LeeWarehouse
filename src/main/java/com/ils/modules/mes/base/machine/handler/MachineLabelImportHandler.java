package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.machine.entity.MachineLabel;
import com.ils.modules.mes.base.machine.service.MachineLabelService;
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
 * 设备标准导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:04
 */
@Service
@Slf4j
public class MachineLabelImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineLabelService machineLabelService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在的名字集合
        List<MachineLabel> list = machineLabelService.list();

        List<String> names = list.stream().map(MachineLabel::getMachineLabelName).collect(Collectors.toList());

        //导入集合list
        List<MachineLabel> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineLabel machineLabel = BeanUtil.toBeanIgnoreError(data, MachineLabel.class);

            //判断如果名字已存在，则抛出导入失败的异常
            if (names.contains(machineLabel.getMachineLabelName())) {
                throw new ILSBootException("P-MC-0106", machineLabel.getMachineLabelName());
            }
            machineLabel.setTenantId(CommonUtil.getTenantId());
            machineLabel.setCreateBy(loginUser.getUsername());
            machineLabel.setCreateTime(currentDate);
            machineLabel.setUpdateBy(loginUser.getUsername());
            machineLabel.setUpdateTime(currentDate);
            machineLabel.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineLabel);
        }
        machineLabelService.saveBatch(importList);
    }

}
