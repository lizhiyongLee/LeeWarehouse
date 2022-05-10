package com.ils.modules.mes.base.product.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.product.entity.ItemBom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 物料BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemBomVO extends ItemBom {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="物料BOM明细表")
    private List<ItemBomDetailVO> itemBomDetailList;
	
}
