package com.ils.modules.mes.base.product.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.product.entity.Product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产品
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductVO extends Product {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="产品工艺路线明细")
    private List<ProductLineVO> productLineList;
    /** 工艺路线编码 */
    @Excel(name = "工艺路线编码", width = 15)
    @ApiModelProperty(value = "工艺路线编码", position = 2)
    private String routeCode;
    /** 工艺路线名称 */
    @Excel(name = "工艺路线名称", width = 15)
    @ApiModelProperty(value = "工艺路线名称", position = 3)
    private String routeName;

    /** 物料清单BOM 版本 */
    @Excel(name = " 物料清单BOM 版本", width = 15)
    private String itemBomVersion;


}
