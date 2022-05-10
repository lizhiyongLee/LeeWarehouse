package com.ils.modules.mes.base.product.vo;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.base.product.entity.Product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 列表查询VO
 * @author: fengyi
 * @date: 2020年11月6日 下午2:21:23
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductListVO extends Product {

    private static final long serialVersionUID = 1L;

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
