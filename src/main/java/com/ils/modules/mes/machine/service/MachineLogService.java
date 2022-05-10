package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.MachineLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备日志
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineLogService extends IService<MachineLog> {

    /**
     * 添加
     * @param machineLog
     */
    public void saveMachineLog(MachineLog machineLog) ;
    
    /**
     * 修改
     * @param machineLog
     */
    public void updateMachineLog(MachineLog machineLog);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineLog (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineLog (List<String> idList);

    /**
     * 通过设备id查询该设备日志
     * @param machineId
     * @return
     */
    public List<MachineLog> queryByMachineId(String machineId);
}
