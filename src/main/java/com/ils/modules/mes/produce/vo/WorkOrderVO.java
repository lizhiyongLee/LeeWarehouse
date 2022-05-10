package com.ils.modules.mes.produce.vo;

import java.math.BigDecimal;
import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.product.entity.ItemBom;
import com.ils.modules.mes.base.product.entity.Product;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import com.ils.modules.mes.produce.entity.WorkOrderItemBom;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import com.ils.modules.mes.produce.entity.WorkOrderRelateSale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工单
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkOrderVO extends WorkOrder {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="工单工序明细")
	private List<WorkOrderLine> workOrderLineList;
	@ExcelCollection(name="工单产品BOM")
	private List<WorkOrderBom> workOrderBomList;
	@ExcelCollection(name="工单物料清单明细")
	private List<WorkOrderItemBom> workOrderItemBomList;

	private List<WorkOrderVO> subWorkOrderVOList;
    /** 工单关联订单行 */
    private List<WorkOrderRelateSale> saleOrderLineList;

	private List<DefineFieldValueVO> lstDefineFields;

	/**
	 * 计划员姓名列表，辅助前端展示
	 */
	private String pmcNameList;

	/**
	 * 主管姓名列表，辅助前端展示
	 */
    private String directorNameList;

	/**
	 * 父工单工序明细
	 */
	private List<WorkOrderLine> parentWorkOrderLineList;
	/**
	 * 可选的产品方案
	 */
	private List<Product> optionalProductList;
	/**
	 * 可选的物料清单方案
	 */
	private List<ItemBom> optionalItemBomList;

	/**
	 * 辅助前端计算，数值与planQty一致
	 */
	private BigDecimal planQtyTemp;

}
