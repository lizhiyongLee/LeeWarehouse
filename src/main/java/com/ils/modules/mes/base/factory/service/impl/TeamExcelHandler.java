package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.Team;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;
import com.ils.modules.mes.base.factory.service.TeamService;
import com.ils.modules.mes.base.factory.vo.TeamVO;
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
public class TeamExcelHandler extends AbstractExcelHandler {

    @Autowired
    private TeamService teamService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        //主表
        Class<Team> teamClass =  Team.class;
        //关联表
        Class<TeamEmployee> teamEmployeeClass = TeamEmployee.class;

        //获取属性列表
        List<String> teamFieldNameList =  this.getFieldNameList(teamClass);
        List<String> teamEmployeeFieldNameList =  this.getFieldNameList(teamEmployeeClass);

        Map<String, TeamVO> teamMap = new HashMap<>(16);
        Set<String> tineCodeList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            tineCodeList.add((String) data.get("teamCode"));
        }
        //根据唯一属性建立获取主表
        tineCodeList.forEach(teamCode -> {
            TeamVO teamVO = new TeamVO();
            List<TeamEmployee> teamEmployeeList = new ArrayList<>();
            teamVO.setTeamEmployeeList(teamEmployeeList);
            teamMap.put(teamCode, teamVO);
        });

        //遍历每一行
        for (Map<String, Object> data : dataList) {
            TeamVO teamVO = teamMap.get((String) data.get("teamCode"));
            TeamEmployee teamEmployee = new TeamEmployee();
            //赋值
            for (String s : data.keySet()) {
                if (teamFieldNameList.contains(s)) {
                    this.invokeData(teamClass, s, teamVO, (String) data.get(s));
                }
                if (teamEmployeeFieldNameList.contains(s)) {
                    this.invokeData(teamEmployeeClass, s, teamEmployee, (String) data.get(s));
                }
            }
            teamVO.getTeamEmployeeList().add(teamEmployee);
        }
        
        //保存
        teamMap.values().forEach(this::add);
    }

    private void add(TeamVO teamVO) {
        Team team = new Team();
        BeanUtils.copyProperties(teamVO, team);
        team.setTenantId(getTenantId());
        teamService.saveMain(team, teamVO.getTeamEmployeeList());
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

