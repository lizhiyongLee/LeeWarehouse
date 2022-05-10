package com.ils.modules.mes.produce.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ProductRouteParaDetail;
import com.ils.modules.mes.produce.entity.WorkOrderLinePara;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 10:59
 */
public interface WorkOrderLineParaMapper extends ILSMapper<WorkOrderLinePara> {
    /**
     * 通过主表 Id 删除
     *
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);

    /**
     * 通过主表 Id 查询
     *
     * @param mainId
     * @return
     */
    public List<ProductRouteParaDetail> selectByMainId(String mainId);
}
