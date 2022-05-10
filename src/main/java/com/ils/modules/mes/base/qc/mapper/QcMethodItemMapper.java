package com.ils.modules.mes.base.qc.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.qc.entity.QcMethodItem;

/**
 * @Description: 质检方案关联物料
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcMethodItemMapper extends ILSMapper<QcMethodItem> {
    
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
	public List<QcMethodItem> selectByMainId(String mainId);
}
