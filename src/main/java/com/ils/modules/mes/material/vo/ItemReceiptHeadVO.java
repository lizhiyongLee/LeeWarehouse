package com.ils.modules.mes.material.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.material.entity.ItemReceiptHead;
import com.ils.modules.mes.material.entity.ItemReceiptLine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * @Description: 收货单头
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemReceiptHeadVO extends ItemReceiptHead {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="收货单行")
	private List<ItemReceiptLine> itemReceiptLineList;
	
}
