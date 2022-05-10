package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.machine.entity.MachineTaskLog;

import java.util.List;

/**
 * @Description: 设备维修日志
 * @Author: Tian
 * @Date:   2020-11-25
 * @Version: V1.0
 */
public interface MachineTaskLogService extends IService<MachineTaskLog> {

    /**
     * 添加
     * @param machineTaskLog
     */
    public void addMachineTaskLog(MachineTaskLog machineTaskLog);
    /**
     * 修改
     * @param machineTaskLog
     */
    public void updateMachineTaskLog(MachineTaskLog machineTaskLog);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineTaskLog (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineTaskLog (List<String> idList);

    /**
     * 保存设备任务信息
     * @param oldStatus
     * @param nowStatus
     * @param taskId
     * @param taskCode
     * @param taskType
     */
    public void saveMachineTaskLog(String oldStatus, String nowStatus, String taskId, String taskCode ,String taskType);
}
