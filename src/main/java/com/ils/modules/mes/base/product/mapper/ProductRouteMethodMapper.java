package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductRouteMethod;
import com.ils.modules.mes.base.product.vo.ProductRouteMethodVO;
import com.ils.modules.mes.base.qc.entity.QcMethod;

/**
 * @Description: 产品路线工序质检方案
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductRouteMethodMapper extends ILSMapper<ProductRouteMethod> {
    
    /**
     * 通过主表 Id 删除
     * 
     * @param productId
     * @return
     */
    public boolean deleteByProductId(String productId);
    
    /**
     * 通过主表 Id 查询
     * 
     * @param productId
     * @return
     */
    public List<ProductRouteMethodVO> selectByProductId(String productId);

    /**
     * 通过产品bom行明显id和质检方案类型查询相对应的质检方案
     * @param productLineId
     * @param methodType
     * @return
     */
    public List<QcMethod> selectByProductLineIdAndQcMethodType(String productLineId,String methodType);
}
