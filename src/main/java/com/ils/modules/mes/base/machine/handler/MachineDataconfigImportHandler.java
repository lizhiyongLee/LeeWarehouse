package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.machine.entity.MachineDataconfig;
import com.ils.modules.mes.base.machine.service.MachineDataconfigService;
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
 * 读数配置导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:05
 */
@Service
@Slf4j
public class MachineDataconfigImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineDataconfigService machineDataconfigService;
    @Autowired
    private UnitService unitService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在的名字集合
        List<MachineDataconfig> list = machineDataconfigService.list();

        List<String> names = list.stream().map(MachineDataconfig::getDataName).collect(Collectors.toList());

        //查询系统中已存在的读数单位
        List<Unit> units = unitService.list();
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(Unit::getUnitName, Unit::getId));
        //导入集合list
        List<MachineDataconfig> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineDataconfig machineDataconfig = BeanUtil.toBeanIgnoreError(data, MachineDataconfig.class);

            //判断如果名字已存在，则抛出导入失败的异常
            if (names.contains(machineDataconfig.getDataName())) {
                throw new ILSBootException("P-MC-0107", machineDataconfig.getDataName());
            }
            //系统中查询读数单位
            if (!unitMap.containsKey(machineDataconfig.getDataUnit())){
                throw new ILSBootException("P-MC-0110",machineDataconfig.getDataUnit());
            }
            //设置读数地址
            machineDataconfig.setDataUnit(unitMap.get(machineDataconfig.getDataUnit()));
            machineDataconfig.setTenantId(CommonUtil.getTenantId());
            machineDataconfig.setCreateBy(loginUser.getUsername());
            machineDataconfig.setCreateTime(currentDate);
            machineDataconfig.setUpdateBy(loginUser.getUsername());
            machineDataconfig.setUpdateTime(currentDate);
            machineDataconfig.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineDataconfig);
        }
        machineDataconfigService.saveBatch(importList);
    }
}
