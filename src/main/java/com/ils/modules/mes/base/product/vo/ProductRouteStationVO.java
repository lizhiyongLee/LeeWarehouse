package com.ils.modules.mes.base.product.vo;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.base.product.entity.ProductRouteStation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产品路线工序关联工位
 * @author: fengyi
 * @date: 2020年11月5日 下午1:29:37
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductRouteStationVO extends ProductRouteStation {

    private static final long serialVersionUID = 1L;

    /** 工位编码 */
    @Excel(name = "工位编码", width = 15)
    private String stationCode;
    /** 工位名称 */
    @Excel(name = "工位名称", width = 15)
    private String stationName;
}
