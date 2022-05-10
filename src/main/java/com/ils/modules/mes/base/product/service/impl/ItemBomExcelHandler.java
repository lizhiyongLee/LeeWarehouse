package com.ils.modules.mes.base.product.service.impl;

import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.mapper.ItemMapper;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.product.entity.ItemBom;
import com.ils.modules.mes.base.product.entity.ItemBomDetail;
import com.ils.modules.mes.base.product.service.ItemBomService;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.base.product.vo.ItemBomVO;
import com.ils.modules.mes.enums.FieldTypeEnum;
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
import java.math.BigDecimal;
import java.util.*;


/**
 * @author lishaojie
 * @description
 * @date 2021/6/21 15:55
 */

@Service
@Slf4j
public class ItemBomExcelHandler extends AbstractExcelHandler {

    @Autowired
    private ItemBomService itemBomService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemService itemService;

    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";

    private static final String DETAIL = "detail";

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");
        //主表
        Class<ItemBom> itemBomClass = ItemBom.class;
        //关联表
        Class<ItemBomDetail> itemBomDetailClass = ItemBomDetail.class;

        //获取属性列表
        Map<String, String> itemBomFieldNameTypeMap = this.getFieldNameList(itemBomClass);
        Map<String, String> itemBomDetailFieldNameTypeMap = this.getFieldNameList(itemBomDetailClass);

        Map<String, ItemBomVO> itemBomMap = new HashMap<>(16);
        Set<String> itemCodeSet = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            itemCodeSet.add((String) data.get("itemCode"));
        }
        //根据唯一属性建立获取主表
        itemCodeSet.forEach(itemCode -> {
            ItemBomVO itemBomVO = new ItemBomVO();
            List<ItemBomDetailVO> itemBomDetailVOList = new ArrayList<>();
            itemBomVO.setItemBomDetailList(itemBomDetailVOList);
            itemBomMap.put(itemCode, itemBomVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            ItemBomVO itemBomVO = itemBomMap.get((String) data.get("itemCode"));
            ItemBomDetailVO itemBomDetailVO = new ItemBomDetailVO();
            //赋值
            for (String s : data.keySet()) {
                if (itemBomFieldNameTypeMap.containsKey(s)) {
                    if ("itemCode".equals(s)) {
                        Item item = itemMapper.queryItemByCode((String) data.get(s));
                        BeanUtils.copyProperties(item, itemBomVO, ignoreProperty);
                        Unit unit = itemService.queryItemUnitListByMainId(item.getId()).get(0);
                        itemBomVO.setUnit(unit.getId());
                        itemBomVO.setItemId(item.getId());
                    }
                    this.invokeData(itemBomClass, s, itemBomVO, (String) data.get(s), itemBomFieldNameTypeMap.get(s));
                }
                if (s.contains(DETAIL)) {
                  String  fieldData=  (String) data.get(s);
                    if ("itemCode".equals(StringUtils.uncapitalize(s.split(DETAIL)[1]))) {
                        Item item = itemMapper.queryItemByCode((String) data.get(s));
                        BeanUtils.copyProperties(item, itemBomDetailVO, ignoreProperty);
                        Unit unit = itemService.queryItemUnitListByMainId(item.getId()).get(0);
                        itemBomDetailVO.setUnit(unit.getId());
                        itemBomDetailVO.setUnitName(unit.getUnitName());
                        itemBomDetailVO.setItemId(item.getId());
                    }
                    s = StringUtils.uncapitalize(s.split(DETAIL)[1]);
                    this.invokeData(itemBomDetailClass, s, itemBomDetailVO,fieldData, itemBomDetailFieldNameTypeMap.get(s));
                }
            }
            itemBomVO.getItemBomDetailList().add(itemBomDetailVO);
        }

        //保存
        Collection<ItemBomVO> values = itemBomMap.values();
        for (ItemBomVO value : values) {
            itemBomService.saveMain(value);
        }
    }


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
    private void invokeData(Class<?> clazz, String field, Object object, String data, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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

