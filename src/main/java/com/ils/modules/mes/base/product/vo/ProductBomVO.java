package com.ils.modules.mes.base.product.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.product.entity.ProductBom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产品工艺物料BOM
 * @author: fengyi
 * @date: 2020年11月5日 下午1:28:19
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductBomVO extends ProductBom {

    private static final long serialVersionUID = 1L;

    @ExcelCollection(name = "产品BOM替代料")
    private List<ProductBomSubstituteVO> itemBomSubstituteList;

}
