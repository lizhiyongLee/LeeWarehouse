package com.ils.modules.mes.produce.service.impl;

import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Customer;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.CustomerService;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.enums.AuditStatusEnum;
import com.ils.modules.mes.enums.FieldTypeEnum;
import com.ils.modules.mes.enums.SaleOrderStatusEnum;
import com.ils.modules.mes.produce.entity.SaleOrder;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.mapper.SaleOrderLineMapper;
import com.ils.modules.mes.produce.service.SaleOrderService;
import com.ils.modules.mes.produce.vo.SaleOrderVO;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/1 15:43
 */
@Component
public class SaleOrderExcelHandler extends AbstractExcelHandler {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    SaleOrderLineMapper saleOrderLineMapper;

    @Override
    @SneakyThrows
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<SaleOrder> saleOrderClass = SaleOrder.class;
        Class<SaleOrderLine> saleOrderLineClass = SaleOrderLine.class;
        //单位和客户
        List<Unit> unitList = unitService.list();
        List<Customer> customerList = customerService.list();
        List<Item> itemList = itemService.list();
        //获取属性列表
        Map<String, String> saleOrderFieldNameMap = this.getFieldNameList(saleOrderClass);
        Map<String, String> saleOrderLineFieldNameMap = this.getFieldNameList(saleOrderLineClass);
        //获取主表数量
        Set<String> saleOrderCodeSet = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            saleOrderCodeSet.add((String) data.get("saleOrderNo"));
        }
        //根据主表数量建立实体
        Map<String, SaleOrderVO> saleOrderMap = new HashMap<>(16);
        saleOrderCodeSet.forEach(code -> {
            SaleOrderVO saleOrderVO = new SaleOrderVO();
            List<SaleOrderLine> saleOrderLineList = new ArrayList<>();
            saleOrderVO.setSaleOrderLineList(saleOrderLineList);
            saleOrderMap.put(code, saleOrderVO);
        });
        //根据数据为主表实体赋值，为关联表建立实体并且赋值
        initData(dataList, saleOrderClass, saleOrderLineClass, unitList, customerList, itemList, saleOrderFieldNameMap, saleOrderLineFieldNameMap, saleOrderMap);

        //保存
        Collection<SaleOrderVO> values = saleOrderMap.values();
        values.forEach(saleOrderVO -> saleOrderService.saveMain(saleOrderVO, saleOrderVO.getSaleOrderLineList()));
    }

    /**
     * 赋值数据
     *
     * @param dataList                  数据来源
     * @param saleOrderClass
     * @param saleOrderLineClass
     * @param unitList                  单位表
     * @param customerList              客户表
     * @param itemList                  物料表
     * @param saleOrderFieldNameMap
     * @param saleOrderLineFieldNameMap
     * @param saleOrderMap              待赋值实体
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ParseException
     */
    private void initData(List<Map<String, Object>> dataList, Class<SaleOrder> saleOrderClass, Class<SaleOrderLine> saleOrderLineClass, List<Unit> unitList, List<Customer> customerList, List<Item> itemList, Map<String, String> saleOrderFieldNameMap, Map<String, String> saleOrderLineFieldNameMap, Map<String, SaleOrderVO> saleOrderMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        for (Map<String, Object> data : dataList) {
            //检查
            boolean unitCheck = true;
            boolean userCheck = true;
            boolean itemCheck = true;
            SaleOrderVO saleOrderVO = saleOrderMap.get((String) data.get("saleOrderNo"));
            SaleOrderLine saleOrderLine = new SaleOrderLine();
            for (String s : data.keySet()) {
                if (saleOrderFieldNameMap.containsKey(s)) {
                    if (s.contains("customer")) {
                        for (Customer customer : customerList) {
                            if (customer.getCustomerName().equals((String) data.get(s))) {
                                userCheck = false;
                                saleOrderVO.setCustomerId(customer.getId());
                                saleOrderVO.setCustomerName(customer.getCustomerName());
                            }
                        }
                    } else {
                        this.invokeData(saleOrderClass, s, saleOrderVO, (String) data.get(s), saleOrderFieldNameMap.get(s));
                    }
                }
                if (saleOrderLineFieldNameMap.containsKey(s)) {
                    if (s.contains("unit")) {
                        for (Unit unit : unitList) {
                            if (unit.getUnitName().equals((String) data.get(s))) {
                                unitCheck = false;
                                saleOrderLine.setUnit(unit.getId());
                            }
                        }
                    } else if (s.contains("itemCode")) {
                        for (Item item : itemList) {
                            if (item.getItemCode().equals((String) data.get(s))) {
                                itemCheck = false;
                                saleOrderLine.setItemId(item.getId());
                                saleOrderLine.setItemCode(item.getItemCode());
                            }
                        }
                    } else {
                        this.invokeData(saleOrderLineClass, s, saleOrderLine, (String) data.get(s), saleOrderLineFieldNameMap.get(s));
                    }
                }
            }
            if (unitCheck || userCheck || itemCheck) {
                throw new ILSBootException("销售订单存在单位、物料或客户未录入！单号：" + saleOrderVO.getSaleOrderNo());
            }

            saleOrderVO.getSaleOrderLineList().add(saleOrderLine);
            saleOrderVO.setStatus(SaleOrderStatusEnum.NEW.getValue());
            saleOrderVO.setAuditStatus(AuditStatusEnum.AUDIT_NEW.getValue());
        }
    }

    /**
     * 根据类获取属性
     *
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
     *
     * @param clazz  当前类
     * @param field  赋值的属性
     * @param object 被赋值的对象
     * @param data   数据来源
     * @param type   属性的类型
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void invokeData(Class<?> clazz, String field, Object object, String data, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
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
        if (type.contains(FieldTypeEnum.DATE.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Date.class);
            Date date = DateUtils.parseDate(data,"yyyy-MM-dd HH:mm:ss");
            method.invoke(object, date);
            check = true;
        }
        if (!check) {
            throw new NoSuchMethodException();
        }
    }
}
