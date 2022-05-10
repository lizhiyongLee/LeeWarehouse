package com.ils.modules.mes.base.machine.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.machine.entity.MachineTypeParaDetail;

/**
 * @Description: 设备类型关联参数明细表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
public interface MachineTypeParaDetailMapper extends ILSMapper<MachineTypeParaDetail> {
    /**
     * 通过主表 Id 删除
     * @param machineTypeId
     * @return
     */
    public boolean deleteByMainId(String machineTypeId);
}
