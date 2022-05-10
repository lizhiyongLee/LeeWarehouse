package com.ils.modules.mes.base.factory.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;

/**
 * @Description: 车位人员
 * @Author: hezhigang
 * @Date:   2020-10-14
 * @Version: V1.0
 */
public interface WorkstationEmployeeMapper extends ILSMapper<WorkstationEmployee> {
    
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
	public List<WorkstationEmployee> selectByMainId(String mainId);
}
