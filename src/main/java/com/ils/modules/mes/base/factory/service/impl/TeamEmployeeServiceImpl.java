package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;
import com.ils.modules.mes.base.factory.mapper.TeamEmployeeMapper;
import com.ils.modules.mes.base.factory.service.TeamEmployeeService;

/**
 * @Description: 班组人员
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Service
public class TeamEmployeeServiceImpl extends ServiceImpl<TeamEmployeeMapper, TeamEmployee> implements TeamEmployeeService {
	
	@Autowired
	private TeamEmployeeMapper teamEmployeeMapper;
	
	@Override
	public List<TeamEmployee> selectByMainId(String mainId) {
		return teamEmployeeMapper.selectByMainId(mainId);
	}
}
