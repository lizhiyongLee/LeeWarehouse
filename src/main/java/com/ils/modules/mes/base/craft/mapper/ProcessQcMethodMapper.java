package com.ils.modules.mes.base.craft.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.craft.entity.ProcessQcMethod;
import com.ils.modules.mes.base.craft.vo.ProcessQcMethodVO;

/**
 * @Description: 工序关联质检
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessQcMethodMapper extends ILSMapper<ProcessQcMethod> {
    
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
    public List<ProcessQcMethodVO> selectByMainId(String mainId);
}
