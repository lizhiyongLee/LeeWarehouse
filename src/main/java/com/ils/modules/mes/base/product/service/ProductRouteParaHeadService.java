package com.ils.modules.mes.base.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.RouteLineParaHead;
import com.ils.modules.mes.base.craft.vo.RouteLineParaVO;
import com.ils.modules.mes.base.product.entity.ProductRouteParaHead;
import com.ils.modules.mes.base.product.vo.ProductRouteParaVO;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 14:42
 */
public interface ProductRouteParaHeadService extends IService<ProductRouteParaHead> {
    /**
     * 保存数据
     * @param productRouteParaVO
     */
    void saveProductRoutePara(ProductRouteParaVO productRouteParaVO);

    /**
     * 更新数据
     * @param productRouteParaVO
     */
    void updateProductRoutePara(ProductRouteParaVO productRouteParaVO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ProductRouteParaVO queryById(String id);

    /**
     * 根据工艺路线行id查询
     * @param productId
     * @return
     */
    List<ProductRouteParaVO> queryByProductId(String productId);

    /**
     * 根据工艺路线行id删除
     * @param productId
     */
    void deleteByProductId(String productId);
}
