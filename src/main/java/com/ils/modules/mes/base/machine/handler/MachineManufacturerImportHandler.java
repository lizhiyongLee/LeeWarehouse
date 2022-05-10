package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.machine.entity.MachineMaintainPolicy;
import com.ils.modules.mes.base.machine.entity.MachineManufacturer;
import com.ils.modules.mes.base.machine.service.MachineManufacturerService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备制造商导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:04
 */
@Service
@Data
public class MachineManufacturerImportHandler extends AbstractExcelHandler {

    @Autowired
    private MachineManufacturerService machineManufacturerService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {

        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在的名字集合
        List<MachineManufacturer> list = machineManufacturerService.list();

        List<String> names = list.stream().map(MachineManufacturer::getManufacturerName).collect(Collectors.toList());

        //导入集合list
        List<MachineManufacturer> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineManufacturer machineManufacturer = BeanUtil.toBeanIgnoreError(data, MachineManufacturer.class);

            //判断如果名字已存在，则抛出导入失败的异常
            if (names.contains(machineManufacturer.getManufacturerName())){
                throw new ILSBootException("P-MC-0105",machineManufacturer.getManufacturerName());
            }
            machineManufacturer.setTenantId(CommonUtil.getTenantId());
            machineManufacturer.setCreateBy(loginUser.getUsername());
            machineManufacturer.setCreateTime(currentDate);
            machineManufacturer.setUpdateBy(loginUser.getUsername());
            machineManufacturer.setUpdateTime(currentDate);
            machineManufacturer.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineManufacturer);
        }
        machineManufacturerService.saveBatch(importList);
    }
}
