package com.ils.modules.mes.base.factory.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;

import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工位
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkstationVO extends Workstation {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="车位人员")
    private List<WorkstationEmployee> workstationEmployeeList;

	private List<DefineFieldValueVO> lstDefineFields;
}
