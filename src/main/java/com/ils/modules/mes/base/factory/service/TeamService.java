package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Team;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;

/**
 * @Description: 班组
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
public interface TeamService extends IService<Team> {

	/**
	 * 添加一对多
	 * @param team
    * @param teamEmployeeList
	 */
	public void saveMain(Team team,List<TeamEmployee> teamEmployeeList) ;
	
	/**
	 * 修改一对多
	 * @param team
    * @param teamEmployeeList
	 */
	public void updateMain(Team team,List<TeamEmployee> teamEmployeeList);
	
	/**
	 * 删除一对多
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 * @param idList
	 */
	public void delBatchMain (List<String> idList);
	
}
