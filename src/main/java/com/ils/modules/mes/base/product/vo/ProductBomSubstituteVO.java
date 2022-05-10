package com.ils.modules.mes.base.product.vo;

import com.ils.modules.mes.base.product.entity.ProductBomSubstitute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 产品工艺物料替代物VO
 * @author: fengyi
 * @date: 2020年11月5日 下午1:30:37
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProductBomSubstituteVO extends ProductBomSubstitute {

    private static final long serialVersionUID = 1L;

    /** 物料名称 */
    private String itemName;

    /** 物料编码 */
    private String itemCode;
}
