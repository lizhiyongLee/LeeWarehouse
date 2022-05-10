package com.ils.modules.mes.base.material.service.impl;

import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.mapper.UnitMapper;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.entity.ItemType;
import com.ils.modules.mes.base.material.mapper.ItemTypeMapper;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.material.service.ItemTypeService;
import com.ils.modules.mes.base.material.vo.ItemVO;
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
public class ItemExcelHandler extends AbstractExcelHandler {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ItemTypeService itemTypeService;

    private static final String STRING = "String";
    private static final String BIG_DECIMAL = "BigDecimal";
    private static final String INTEGER = "Integer";
    private static final String LONG = "Long";

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        List<Unit> unitList = unitService.list();
        List<ItemType> itemTypeList = itemTypeService.list();
        //主表
        Class<Item> itemClass = Item.class;

        //获取属性列表
        Map<String, String> itemFieldNameTypeMap = this.getFieldNameList(itemClass);

        Map<String, ItemVO> itemMap = new HashMap<>(16);
        Set<String> itemCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            itemCodeList.add((String) data.get("itemCode"));
        }
        //根据唯一属性建立获取主表
        itemCodeList.forEach(itemCode -> {
            ItemVO itemVO = new ItemVO();
            itemMap.put(itemCode, itemVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            ItemVO itemVO = itemMap.get((String) data.get("itemCode"));
            //赋值
            boolean unitCheck = true;
            boolean typeCheck = true;
            for (String s : data.keySet()) {
                if (itemFieldNameTypeMap.containsKey(s)) {
                    if (s.contains("mainUnit")) {
                        for (Unit unit : unitList) {
                            if (unit.getUnitName().equals((String) data.get(s))) {
                                unitCheck = false;
                                itemVO.setMainUnit(unit.getId());
                            }
                        }
                    } else if (s.contains("itemTypeId")) {
                        for (ItemType itemType : itemTypeList) {
                            if (itemType.getTypeName().equals((String) data.get(s))) {
                                typeCheck = false;
                                itemVO.setItemTypeId(itemType.getId());
                            }
                        }
                    } else {
                        this.invokeData(itemClass, s, itemVO, (String) data.get(s), itemFieldNameTypeMap.get(s));
                    }
                }
            }
            if (unitCheck || typeCheck) {
                throw new ILSBootException("单位或者物料类型未录入！");
            }
        }

        //保存
        itemMap.values().forEach(this::add);
    }

    private void add(ItemVO itemVO) {
        itemService.saveMain(itemVO);
    }

    private Map<String, String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldNameTypeMap = new HashMap<>(16);
        for (Field field : fields) {
            fieldNameTypeMap.put(field.getName(), field.getType().getName());
        }
        return fieldNameTypeMap;
    }

    private void invokeData(Class<?> clazz, String field, Object object, String data, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        boolean check = false;
        Method method = null;
        if (type.contains(STRING)) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), String.class);
            method.invoke(object, data);
            check = true;
        }
        if (type.contains(BIG_DECIMAL)) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), BigDecimal.class);
            BigDecimal newData = new BigDecimal(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(INTEGER)) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Integer.class);
            Integer newData = new Integer(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(LONG)) {
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

