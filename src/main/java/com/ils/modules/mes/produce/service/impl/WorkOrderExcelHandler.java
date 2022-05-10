package com.ils.modules.mes.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.Route;
import com.ils.modules.mes.base.craft.service.RouteService;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.product.entity.ItemBom;
import com.ils.modules.mes.base.product.entity.Product;
import com.ils.modules.mes.base.product.service.ItemBomService;
import com.ils.modules.mes.base.product.service.ProductService;
import com.ils.modules.mes.enums.FieldTypeEnum;
import com.ils.modules.mes.enums.WorkflowTypeEnum;
import com.ils.modules.mes.produce.service.WorkOrderService;
import com.ils.modules.mes.produce.vo.BatchWorkOrderVO;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.AbstractExcelHandler;
import com.ils.modules.system.service.UserService;
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
public class WorkOrderExcelHandler extends AbstractExcelHandler {
    @Autowired
    WorkOrderService workOrderService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService sysUserService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private ItemBomService itemBomService;
    @Autowired
    private ProductService productService;

    @Override
    @SneakyThrows
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<BatchWorkOrderVO> batchWorkOrderClass = BatchWorkOrderVO.class;
        //获取属性列表
        Map<String, String> workOrderFieldNameList = this.getFieldNameList(batchWorkOrderClass);
        //单位和用户
        List<Unit> unitList = unitService.list();
        List<User> userList = sysUserService.list();
        List<Item> itemList = itemService.list();
        //获取主表数量
        Set<String> workOrderCodeSet = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            workOrderCodeSet.add((String) data.get("orderNo"));
        }
        //根据主表数量建立实体
        Map<String, BatchWorkOrderVO> workOrderMap = new HashMap<>(16);
        workOrderCodeSet.forEach(code -> {
            BatchWorkOrderVO batchWorkOrderVO = new BatchWorkOrderVO();
            workOrderMap.put(code, batchWorkOrderVO);
        });
        //赋值
        initData(dataList, batchWorkOrderClass, workOrderFieldNameList, unitList, userList, itemList, workOrderMap);
        //保存
        Collection<BatchWorkOrderVO> values = workOrderMap.values();
        List<BatchWorkOrderVO> lstBatchWorkOrderVO = new ArrayList<>(values);
        values.forEach(batchWorkOrderVO -> workOrderService.batchSaleAdd(lstBatchWorkOrderVO));
    }

    /**
     * 赋值数据
     *
     * @param dataList               数据来源
     * @param batchWorkOrderClass
     * @param workOrderFieldNameList
     * @param unitList               单位表
     * @param userList               用户表
     * @param workOrderMap           待赋值实体
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void initData(List<Map<String, Object>> dataList, Class<BatchWorkOrderVO> batchWorkOrderClass, Map<String, String> workOrderFieldNameList, List<Unit> unitList, List<User> userList, List<Item> itemList, Map<String, BatchWorkOrderVO> workOrderMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        for (Map<String, Object> data : dataList) {
            //检查
            boolean unitCheck = true;
            boolean userCheck = true;
            boolean itemCheck = true;

            BatchWorkOrderVO batchWorkOrderVO = workOrderMap.get((String) data.get("orderNo"));
            //赋值基础数据，将unit、pmc和director转换为id
            for (String s : data.keySet()) {
                if (workOrderFieldNameList.containsKey(s)) {
                    if (s.contains("unit")) {
                        for (Unit unit : unitList) {
                            if (unit.getUnitName().equals((String) data.get(s))) {
                                unitCheck = false;
                                batchWorkOrderVO.setUnit(unit.getId());
                            }
                        }
                    } else if (s.contains("itemCode")) {
                        for (Item item : itemList) {
                            if (item.getItemCode().equals((String) data.get(s))) {
                                itemCheck = false;
                                batchWorkOrderVO.setItemId(item.getId());
                                batchWorkOrderVO.setItemCode(item.getItemCode());
                                batchWorkOrderVO.setSpec(item.getSpec());
                            }
                        }
                    } else if (s.contains("pmc")) {
                        for (User user : userList) {
                            if (user.getRealname().equals((String) data.get(s))) {
                                userCheck = false;
                                batchWorkOrderVO.setPmc(user.getId());
                            }
                        }
                    } else if (s.contains("director")) {
                        for (User user : userList) {
                            if (user.getRealname().equals((String) data.get(s))) {
                                userCheck = false;
                                batchWorkOrderVO.setDirector(user.getId());
                            }
                        }
                    } else {
                        this.invokeData(batchWorkOrderClass, s, batchWorkOrderVO, (String) data.get(s), workOrderFieldNameList.get(s));
                    }
                }
            }
            //将routeCode、itemBomVersion和productVersion转换为id
            boolean productCheck = turnProduct(data, batchWorkOrderVO);
            if (unitCheck || userCheck || productCheck || itemCheck) {
                throw new ILSBootException("工单存在单位、物料、人员或者生产工艺未录入！工单号：" + batchWorkOrderVO.getOrderNo());
            }
        }
    }

    /**
     * 将routeCode、itemBomVersion和productVersion转换为id
     *
     * @param data             数据源
     * @param productCheck     是否有值
     * @param batchWorkOrderVO 待赋值实体
     * @return
     */
    private boolean turnProduct(Map<String, Object> data, BatchWorkOrderVO batchWorkOrderVO) {
        boolean productCheck = true;
        for (String s : data.keySet()) {
            if (batchWorkOrderVO.getWorkflowType().equals(WorkflowTypeEnum.ROUTE.getValue()) & s.contains("routeCode")) {
                QueryWrapper<Route> wrapper = new QueryWrapper();
                wrapper.eq("route_code", data.get(s));
                Route route = routeService.getOne(wrapper);
                batchWorkOrderVO.setRouteId(route.getId());
                productCheck = false;
            }
            if (batchWorkOrderVO.getWorkflowType().equals(WorkflowTypeEnum.ROUTE_ITEM_BOM.getValue()) & s.contains("itemBomVersion")) {
                QueryWrapper<ItemBom> wrapper = new QueryWrapper();
                wrapper.eq("item_name", batchWorkOrderVO.getItemName());
                wrapper.eq("item_bom_version", data.get(s));
                ItemBom itemBom = itemBomService.getOne(wrapper);
                batchWorkOrderVO.setBomId(itemBom.getId());
                productCheck = false;
            }
            if (batchWorkOrderVO.getWorkflowType().equals(WorkflowTypeEnum.PRODUCT_BOM.getValue()) & s.contains("productVersion")) {
                QueryWrapper<Product> wrapper = new QueryWrapper();
                wrapper.eq("item_name", batchWorkOrderVO.getItemName());
                wrapper.eq("version", data.get(s));
                Product product = productService.getOne(wrapper);
                batchWorkOrderVO.setProductId(product.getId());
                productCheck = false;
            }
        }
        return productCheck;
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
            Date date = DateUtils.parseDate(data, "yyyy-MM-dd HH:mm:ss");
            method.invoke(object, date);
            check = true;
        }
        if (!check) {
            throw new NoSuchMethodException();
        }
    }
}
