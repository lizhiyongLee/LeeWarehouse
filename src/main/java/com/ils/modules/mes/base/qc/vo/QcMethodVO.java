package com.ils.modules.mes.base.qc.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 质检方案
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Data
public class QcMethodVO extends QcMethod {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="质检方案关联质检项",type = ArrayList.class)
	private List<QcMethodDetail> qcMethodDetailList;
	
}
