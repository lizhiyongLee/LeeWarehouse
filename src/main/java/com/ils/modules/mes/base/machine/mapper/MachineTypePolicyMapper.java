package com.ils.modules.mes.base.machine.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;

import java.util.List;

/**
 * @Description: 设备类型关联策略组
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface MachineTypePolicyMapper extends ILSMapper<MachineTypePolicy> {

    /**
     * 删除所有策略
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);

    /**
     * 查询所有跟策略id绑定的数据
     * @param id
     * @return
     */
    public List<MachineTypePolicy> selectAllByMachineTypeId(String id);

}
