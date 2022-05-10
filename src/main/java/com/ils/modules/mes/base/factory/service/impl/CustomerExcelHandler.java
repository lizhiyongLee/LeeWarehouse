package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.Customer;
import com.ils.modules.mes.base.factory.service.CustomerService;
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
public class CustomerExcelHandler extends AbstractExcelHandler {

    @Autowired
    private CustomerService customerService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        Class<Customer> customerClass = Customer.class;
        List<String> customerFieldNameList = this.getFieldNameList(customerClass);
        List<Customer> customerList = new ArrayList<>();
        //遍历每一行
        for (Map<String, Object> data : dataList) {
            Customer customer = new Customer();
            //赋值
            for (String s : data.keySet()) {
                if (customerFieldNameList.contains(s)) {
                    this.invokeData(customerClass, s, customer, (String) data.get(s));
                }
            }
            customerList.add(customer);
        }
        //保存
        customerList.forEach(customer -> customerService.saveCustomer(customer));
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

