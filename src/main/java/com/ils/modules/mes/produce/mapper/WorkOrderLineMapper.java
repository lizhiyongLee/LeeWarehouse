package com.ils.modules.mes.produce.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.vo.RouteVO;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 工单工序明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderLineMapper extends ILSMapper<WorkOrderLine> {

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
    public List<WorkOrderLine> selectByMainId(String mainId);

    /**
     * 查询工艺相关信息
     *
     * @param orderIdList
     * @author niushuai
     * @date: 2021/11/11 15:54:37
     * @return: {@link List<RouteVO>}
     */
    List<RouteVO> selectRouteByOrderId(@Param("orderIdList") List<String> orderIdList);
}
