package com.ils.modules.mes.base.factory.service.impl;


import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/21 15:55
 */

@Service
@Slf4j
public class UnitExcelHandler extends AbstractExcelHandler {

    @Autowired
    private UnitService unitService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        Class<Unit> unitClass = Unit.class;
        List<String> unitFieldNameList = this.getFieldNameList(unitClass);
        List<Unit> unitList = new ArrayList<>();
        //遍历每一行
        for (Map<String, Object> data : dataList) {
            Unit unit = new Unit();
            //赋值
            for (String s : data.keySet()) {
                if (unitFieldNameList.contains(s)) {
                    this.invokeData(unitClass, s, unit, (String) data.get(s));
                }
            }
            unitList.add(unit);
        }
        //保存
        unitList.forEach(unit -> unitService.saveUnit(unit));
    }

    private List<String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNameList = new ArrayList<>();
        for (Field field : fields) {
            fieldNameList.add(field.getName());
        }
        return fieldNameList;
    }

    private void invokeData(Class<?> clazz, String field, Object object, String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), String.class);
        method.invoke(object, data);
    }
}

