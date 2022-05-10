package com.ils.modules.mes.base.craft.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.vo.RouteLineVO;

/**
 * @Description: 工艺路线明细表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
public interface RouteLineMapper extends ILSMapper<RouteLine> {
    
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
	public boolean deleteByMainId(String mainId);
    
    /**
     * 通过主表 Id 查询
     * @param mainId
     * @return
     */
    public List<RouteLineVO> selectByMainId(String mainId);
}
