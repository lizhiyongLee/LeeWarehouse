package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.vo.WorkShopVO;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.ils.modules.mes.util.CommonUtil.getTenantId;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/21 15:55
 */

@Service
@Slf4j
public class WorkShopExcelHandler extends AbstractExcelHandler {

    @Autowired
    private WorkShopService workShopService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<WorkShop> workShopClass = WorkShop.class;
        //关联表
        Class<WorkShopEmployee> workShopEmployeeClass = WorkShopEmployee.class;

        //获取属性列表
        List<String> workShopFieldNameList = this.getFieldNameList(workShopClass);
        List<String> workShopEmployeeFieldNameList =  this.getFieldNameList(workShopEmployeeClass);

        Map<String, WorkShopVO> mapWorkShopVO = new HashMap<>(16);
        Set<String> shopCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            shopCodeList.add((String) data.get("shopCode"));
        }
        //根据唯一属性建立获取主表
        shopCodeList.forEach(shopCode -> {
            WorkShopVO workShopVO = new WorkShopVO();
            List<WorkShopEmployee> workShopEmployeeList = new ArrayList<>();
            workShopVO.setWorkShopEmployeeList(workShopEmployeeList);
            mapWorkShopVO.put(shopCode, workShopVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            WorkShopVO workShopVO = mapWorkShopVO.get((String) data.get("shopCode"));
            WorkShopEmployee workShopEmployee = new WorkShopEmployee();
            //赋值
            for (String s : data.keySet()) {
                if (workShopFieldNameList.contains(s)) {
                    this.invokeData(workShopClass, s, workShopVO, (String) data.get(s));
                }
                if (workShopEmployeeFieldNameList.contains(s)) {
                    this.invokeData(workShopEmployeeClass, s, workShopEmployee, (String) data.get(s));
                }
            }
            workShopVO.getWorkShopEmployeeList().add(workShopEmployee);
        }

        //保存
        mapWorkShopVO.values().forEach(workShopVO -> workShopService.saveMain(workShopVO));
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

