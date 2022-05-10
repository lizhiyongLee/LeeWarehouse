package com.ils.modules.mes.base.factory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.vo.NodeVO;

/**
 * @Description: 车间
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkShopMapper extends ILSMapper<WorkShop> {

    
    /**
     * 
     * 查询机构定义
     * 
     * @param queryWrapper
     * @return List<NodeVO>
     * @date 2020年10月22日
     */
    List<NodeVO> queryInstitutionList(@Param("ew") QueryWrapper queryWrapper);

    /**
     * 
     * 查询车间、产线、工位
     * 
     * @param queryWrapper
     * @return List<NodeVO>
     * @date 2020年10月22日
     */
    List<NodeVO> queryStationList(@Param("ew") QueryWrapper queryWrapper);

    /**
     * 
     * 查询车间、产线、工位
     * 
     * @param queryWrapper
     * @return List<NodeVO>
     * @date 2020年10月22日
     */
    List<NodeVO> queryAssignStationTreeList(@Param("ew") QueryWrapper queryWrapper);

}
