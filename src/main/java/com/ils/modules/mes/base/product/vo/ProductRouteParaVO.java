package com.ils.modules.mes.base.product.vo;

import com.ils.modules.mes.base.product.entity.ProductRouteParaDetail;
import com.ils.modules.mes.base.product.entity.ProductRouteParaHead;
import lombok.Data;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 16:16
 */
@Data
public class ProductRouteParaVO extends ProductRouteParaHead {
    private List<ProductRouteParaDetail> productRouteParaDetailList;
}
