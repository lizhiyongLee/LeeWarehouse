package com.ils.modules.mes.base.product.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.base.product.entity.ProductRouteMethod;

import lombok.*;

/**
 * @Description: 产品工艺路线关联质检方案
 * @author: fengyi
 * @date: 2020年11月5日 下午1:29:19
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductRouteMethodVO extends ProductRouteMethod {

    private static final long serialVersionUID = 1L;

    /** 质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检 */
    @Dict(dicCode = "mesQcType")
    private String qcType;
    /** 质检方案名称 */
    private String methodName;
}
