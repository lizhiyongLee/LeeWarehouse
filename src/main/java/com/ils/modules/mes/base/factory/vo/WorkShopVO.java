package com.ils.modules.mes.base.factory.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;

import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 车间
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkShopVO extends WorkShop {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="车间人员")
	private List<WorkShopEmployee> workShopEmployeeList;

	private List<DefineFieldValueVO> lstDefineFields;
	
}
