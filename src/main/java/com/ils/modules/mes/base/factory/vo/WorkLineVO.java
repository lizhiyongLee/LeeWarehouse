package com.ils.modules.mes.base.factory.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;

import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产线
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkLineVO extends WorkLine {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="产线人员")
	private List<WorkLineEmployee> workLineEmployeeList;

	private List<DefineFieldValueVO> lstDefineFields;
}
