package com.ils.modules.mes.base.qc.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.qc.entity.QcMethod;

import java.util.List;

/**
 * @Description: 质检方案
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcMethodMapper extends ILSMapper<QcMethod> {

    /**
     * 查询质检方案
     *
     * @param qcType
     * @param id
     * @return
     */
    public List<QcMethod> queryQcMeThodByItemIdAndQcType(String qcType, String id);

    /**
     * 通过工序路线明细id查询质检方案
     *
     * @param routeLineId
     * @param qcType
     * @return
     */
    public List<QcMethod> queryByRouteLineId(String qcType, String routeLineId);

    /**
     * 通过产品工艺路线bomid查询质检方案
     *
     * @param productLineId
     * @param qcType
     * @return
     */
    public List<QcMethod> queryByProductLineId(String qcType, String productLineId);

    /**
     * 通过生产任务id和质检类型查询对应的质检方案
     * @param qcType
     * @param taskId
     * @return
     */
    public List<QcMethod> queryByTaskId(String qcType,String taskId);
}
