package com.ils.modules.mes.machine.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.machine.entity.MachineParaHead;

/**
 * @Description: 设备关联参数主表
 * @Author: Tian
 * @Date: 2021-10-19
 * @Version: V1.0
 */
public interface MachineParaHeadMapper extends ILSMapper<MachineParaHead> {
    /**
     * 通过主表 Id 删除
     * @param machineId
     * @return
     */
    public boolean deleteByMainId(String machineId);
}
