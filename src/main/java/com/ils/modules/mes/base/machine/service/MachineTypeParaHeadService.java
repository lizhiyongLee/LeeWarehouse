package com.ils.modules.mes.base.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.machine.entity.MachineTypeParaHead;

/**
 * @Description: 设备类型关联参数主表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
public interface MachineTypeParaHeadService extends IService<MachineTypeParaHead> {
    /**
     * 根据主表删除
     *
     * @param machineTypeId
     */
    void deleteByMachineTypeId(String machineTypeId);

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(String id);

}
