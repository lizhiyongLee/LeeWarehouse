package com.ils.modules.mes.base.factory.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.Team;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;
import com.ils.modules.mes.base.factory.mapper.TeamEmployeeMapper;
import com.ils.modules.mes.base.factory.mapper.TeamMapper;
import com.ils.modules.mes.base.factory.service.TeamService;

/**
 * @Description: 班组
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private TeamEmployeeMapper teamEmployeeMapper;
	
	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void saveMain(Team team, List<TeamEmployee> teamEmployeeList) {
		teamMapper.insert(team);
		for(TeamEmployee entity:teamEmployeeList) {
			//外键设置
			entity.setTeamId(team.getId());
			teamEmployeeMapper.insert(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void updateMain(Team team,List<TeamEmployee> teamEmployeeList) {
		teamMapper.updateById(team);
		
		//1.先删除子表数据
		teamEmployeeMapper.deleteByMainId(team.getId());
		
		//2.子表数据重新插入
		for(TeamEmployee entity:teamEmployeeList) {
			//外键设置
			entity.setTeamId(team.getId());
			teamEmployeeMapper.insert(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delMain(String id) {
		teamEmployeeMapper.deleteByMainId(id);
		teamMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delBatchMain(List<String> idList) {
		for(Serializable id:idList) {
			teamEmployeeMapper.deleteByMainId(id.toString());
			teamMapper.deleteById(id);
		}
	}
	
}
