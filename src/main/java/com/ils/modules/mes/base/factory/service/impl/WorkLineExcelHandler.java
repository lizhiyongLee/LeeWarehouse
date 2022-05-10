package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;
import com.ils.modules.mes.base.factory.service.WorkLineService;
import com.ils.modules.mes.base.factory.vo.WorkLineVO;
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
public class WorkLineExcelHandler extends AbstractExcelHandler {

    @Autowired
    private WorkLineService workLineService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<WorkLine> workLineClass =  WorkLine.class;
        //关联表
        Class<WorkLineEmployee> workLineEmployeeClass = WorkLineEmployee.class;

        //获取属性列表
        List<String> workLineFieldNameList =  this.getFieldNameList(workLineClass);
        List<String> workLineEmployeeFieldNameList =  this.getFieldNameList(workLineEmployeeClass);

        Map<String, WorkLineVO> workLineMap = new HashMap<>(16);
        Set<String> lineCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            lineCodeList.add((String) data.get("LineCode"));
        }
        //根据唯一属性建立获取主表
        lineCodeList.forEach(lineCode -> {
            WorkLineVO workLineVO = new WorkLineVO();
            List<WorkLineEmployee> workLineEmployeeList = new ArrayList<>();
            workLineVO.setWorkLineEmployeeList(workLineEmployeeList);
            workLineMap.put(lineCode, workLineVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            WorkLineVO workLineVO = workLineMap.get((String) data.get("lineCode"));
            WorkLineEmployee workLineEmployee = new WorkLineEmployee();
            //赋值
            for (String s : data.keySet()) {
                if (workLineFieldNameList.contains(s)) {
                    this.invokeData(workLineClass, s, workLineVO, (String) data.get(s));
                }
                if (workLineEmployeeFieldNameList.contains(s)) {
                    this.invokeData(workLineEmployeeClass, s, workLineEmployee, (String) data.get(s));
                }
            }
            workLineVO.getWorkLineEmployeeList().add(workLineEmployee);
        }
        //保存
        workLineMap.values().forEach(workLineVO ->  workLineService.saveMain(workLineVO));
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

