package com.ils.modules.mes.base.craft.service.impl;

import com.ils.modules.mes.base.craft.service.ProcessService;
import com.ils.modules.mes.base.craft.vo.ProcessVO;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.enums.FieldTypeEnum;
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
import java.math.BigDecimal;
import java.util.*;


/**
 * @author lishaojie
 * @description
 * @date 2021/6/21 15:55
 */

@Service
@Slf4j
public class ProcessExcelHandler extends AbstractExcelHandler {

    @Autowired
    private ProcessService processService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<Process> processClass = Process.class;

        //获取属性列表
        Map<String, String> processFieldNameTypeMap = this.getFieldNameList(processClass);

        Map<String, ProcessVO> processMap = new HashMap<>(16);
        Set<String> lineCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            lineCodeList.add((String) data.get("processCode"));
        }
        //根据唯一属性建立获取主表
        lineCodeList.forEach(lineCode -> {
            ProcessVO processVO = new ProcessVO();
            processMap.put(lineCode, processVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            ProcessVO processVO = processMap.get((String) data.get("processCode"));
            //赋值
            for (String s : data.keySet()) {
                if (processFieldNameTypeMap.containsKey(s)) {
                    this.invokeData(processClass, s, processVO, (String) data.get(s),processFieldNameTypeMap.get(s));
                }
            }
        }

        //保存
        processMap.values().forEach(processVO -> processService.saveMain(processVO));
    }

    /**
     * 获取属性
     * @param clazz
     * @return
     */
    private Map<String, String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldNameTypeMap = new HashMap<>(16);
        for (Field field : fields) {
            fieldNameTypeMap.put(field.getName(), field.getType().getName());
        }
        return fieldNameTypeMap;
    }

    /**
     * 赋值函数
     * @param clazz 当前类
     * @param field 赋值的属性
     * @param object 被赋值的对象
     * @param data 数据来源
     * @param type 属性的类型
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void invokeData(Class<?> clazz, String field, Object object,  String data, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        boolean check = false;
        Method method = null;
        if (type.contains(FieldTypeEnum.STRING.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), String.class);
            method.invoke(object, data);
            check = true;
        }
        if (type.contains(FieldTypeEnum.BIG_DECIMAL.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), BigDecimal.class);
            BigDecimal newData = new BigDecimal(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(FieldTypeEnum.INTEGER.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Integer.class);
            Integer newData = new Integer(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(FieldTypeEnum.LONG.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Long.class);
            Long newData = new Long(data);
            method.invoke(object, newData);
            check = true;
        }
        if (!check) {
            throw new NoSuchMethodException();
        }
    }
}

