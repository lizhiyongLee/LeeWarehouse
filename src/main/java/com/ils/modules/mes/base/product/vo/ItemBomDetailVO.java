package com.ils.modules.mes.base.product.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.product.entity.ItemBomDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 物料BOM明细
 * @author: fengyi
 * @date: 2020年10月26日 下午3:58:56
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemBomDetailVO extends ItemBomDetail {
    private static final long serialVersionUID = 1L;

    @ExcelCollection(name = "物料BOM替代料")
    private List<ItemBomSubstituteVO> itemBomSubstituteList;
}
