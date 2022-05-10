package com.ils.modules.mes.base.qc.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;

/**
 * @Description: 质检方案关联质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcMethodDetailMapper extends ILSMapper<QcMethodDetail> {
    
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
	public List<QcMethodDetail> selectByMainId(String mainId);
}
