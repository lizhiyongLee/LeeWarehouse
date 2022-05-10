package com.ils.modules.mes.base.product.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.product.entity.ProductLine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产品工艺路线VO
 * @author: fengyi
 * @date: 2020年11月5日 下午1:25:43
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductLineVO extends ProductLine {
    private static final long serialVersionUID = 1L;
    @ExcelCollection(name = "产品BOM")
    private List<ProductBomVO> productBomList;
    @ExcelCollection(name = "产品路线工序工位")
    private List<ProductRouteStationVO> productRouteStationList;
    @ExcelCollection(name = "产品路线工序质检方案")
    private List<ProductRouteMethodVO> productRouteMethodList;

    private List<ProductRouteParaVO> productRouteParaVOList;
}
