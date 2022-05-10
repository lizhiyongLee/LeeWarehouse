package com.ils.modules.mes.base.factory.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;

/**
 * @Description: 产线人员
 * @Author: hezhigang
 * @Date:   2020-10-14
 * @Version: V1.0
 */
public interface WorkLineEmployeeMapper extends ILSMapper<WorkLineEmployee> {
    
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
	public List<WorkLineEmployee> selectByMainId(String mainId);
}
