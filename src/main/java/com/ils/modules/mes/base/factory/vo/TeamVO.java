package com.ils.modules.mes.base.factory.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.factory.entity.Team;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 班组
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TeamVO extends Team {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="班组人员")
	private List<TeamEmployee> teamEmployeeList;
	
}
