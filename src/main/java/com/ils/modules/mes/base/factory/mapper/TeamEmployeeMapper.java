package com.ils.modules.mes.base.factory.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;

/**
 * @Description: 班组人员
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
public interface TeamEmployeeMapper extends ILSMapper<TeamEmployee> {
    
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
	public List<TeamEmployee> selectByMainId(String mainId);
}
