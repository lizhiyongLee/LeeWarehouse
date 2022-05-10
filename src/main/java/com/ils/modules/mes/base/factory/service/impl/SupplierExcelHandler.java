package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.Supplier;
import com.ils.modules.mes.base.factory.service.SupplierService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/21 15:55
 */

@Service
@Slf4j
public class SupplierExcelHandler extends AbstractExcelHandler {

    @Autowired
    private SupplierService supplierService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        Class<Supplier> supplierClass = Supplier.class;
        List<String> supplierFieldNameList = this.getFieldNameList(supplierClass);
        List<Supplier> supplierList = new ArrayList<>();
        //遍历每一行
        for (Map<String, Object> data : dataList) {
            Supplier supplier = new Supplier();
            //赋值
            for (String s : data.keySet()) {
                if (supplierFieldNameList.contains(s)) {
                    this.invokeData(supplierClass, s, supplier, (String) data.get(s));
                }
            }
            supplierList.add(supplier);
        }
        //保存
        supplierList.forEach(supplier -> supplierService.saveSupplier(supplier));
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

