package com.ils.modules.mes.machine.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.machine.entity.MachineParaDetail;

/**
 * @Description: 设备关联参数
 * @Author: Tian
 * @Date: 2021-10-19
 * @Version: V1.0
 */
public interface MachineParaDetailMapper extends ILSMapper<MachineParaDetail> {
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);
}
