package com.ils.modules.mes.base.craft.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;

import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工序
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ProcessVO extends Process {
	
	private static final long serialVersionUID = 1L;
    /** 工序关联质检 */
	@ExcelCollection(name="工序关联质检")
    private List<ProcessQcMethodVO> processQcMethodList;
    /** 工序关联不良项 */
	@ExcelCollection(name="工序关联不良项")
	private List<ProcessNgItem> processNgItemList;
    @ExcelCollection(name="自定义字段")
    private List<DefineFieldValueVO> lstDefineFields;
    /** 工序关联参数**/
    private List<ProcessParaVO> processParaVOList;
    /** 工位 */
    private String processStation;
    /** 工位名称 */
    private String processStationName;

}
