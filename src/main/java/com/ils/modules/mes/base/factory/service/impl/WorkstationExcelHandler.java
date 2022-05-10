package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;
import com.ils.modules.mes.base.factory.mapper.WorkLineMapper;
import com.ils.modules.mes.base.factory.mapper.WorkShopMapper;
import com.ils.modules.mes.base.factory.service.WorkLineService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.factory.vo.NodeTreeVO;
import com.ils.modules.mes.base.factory.vo.WorkstationVO;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class WorkstationExcelHandler extends AbstractExcelHandler {

    @Autowired
    private WorkstationService workstationService;
    @Autowired
    private WorkShopService workShopService;
    @Autowired
    private WorkLineService workLineService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<Workstation> workstationClass = Workstation.class;
        //关联表
        Class<WorkstationEmployee> workstationEmployeeClass = WorkstationEmployee.class;

        //获取属性列表
        List<String> workstationFieldNameList = this.getFieldNameList(workstationClass);
        List<String> workstationEmployeeFieldNameList = this.getFieldNameList(workstationEmployeeClass);

        Map<String, WorkstationVO> workstationMap = new HashMap<>(16);
        Set<String> stationCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            stationCodeList.add((String) data.get("stationCode"));
        }
        //根据唯一属性建立获取主表
        stationCodeList.forEach(stationCode -> {
            WorkstationVO workstationVO = new WorkstationVO();
            List<WorkstationEmployee> workstationEmployeeList = new ArrayList<>();
            workstationVO.setWorkstationEmployeeList(workstationEmployeeList);
            workstationMap.put(stationCode, workstationVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            WorkstationVO workstationVO = workstationMap.get((String) data.get("stationCode"));
            WorkstationEmployee workstationEmployee = new WorkstationEmployee();
            //赋值
            for (String s : data.keySet()) {
                if (workstationFieldNameList.contains(s)) {
                    this.invokeData(workstationClass, s, workstationVO, (String) data.get(s));
                }
                if (workstationEmployeeFieldNameList.contains(s)) {
                    this.invokeData(workstationEmployeeClass, s, workstationEmployee, (String) data.get(s));
                }
            }
            workstationVO.getWorkstationEmployeeList().add(workstationEmployee);
        }

        //保存
        workstationMap.values().forEach(workstationVO -> {
            String upAreaName = workstationVO.getUpAreaName();
            List<WorkShop> workShopList = workShopService.list();
            List<WorkLine> workLineList = workLineService.list();
            workShopList.forEach(workShop -> {
                if (upAreaName.equals(workShop.getShopName())) {
                    workstationVO.setUpArea(workShop.getId());
                }
            });
            workLineList.forEach(workLine -> {
                if (upAreaName.equals(workLine.getLineName())) {
                    workstationVO.setUpArea(workLine.getId());
                }
            });
            if(!CollectionUtils.isEmpty(workstationVO.getWorkstationEmployeeList())){
                if (StringUtils.isBlank( workstationVO.getWorkstationEmployeeList().get(0).getEmployeeName())) {
                    workstationVO.setWorkstationEmployeeList(new ArrayList<>());
                }
            }
            workstationService.saveMain(workstationVO);
        });
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

