package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.MachineStopTime;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备关联停机时间
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineStopTimeService extends IService<MachineStopTime> {

    /**
     * 添加
     * @param machineStopTime
     */
    public void saveMachineStopTime(MachineStopTime machineStopTime) ;
    
    /**
     * 修改
     * @param machineStopTime
     */
    public void updateMachineStopTime(MachineStopTime machineStopTime);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineStopTime (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineStopTime (List<String> idList);
}
