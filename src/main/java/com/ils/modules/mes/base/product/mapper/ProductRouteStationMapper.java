package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductRouteStation;
import com.ils.modules.mes.base.product.vo.ProductRouteStationVO;

/**
 * @Description: 产品路线工序工位
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductRouteStationMapper extends ILSMapper<ProductRouteStation> {
    
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
    public boolean deleteByProductId(String mainId);
    
    /**
     * 通过主表 Id 查询
     * @param mainId
     * @return
     */
    public List<ProductRouteStationVO> selectByProductId(String mainId);
}
