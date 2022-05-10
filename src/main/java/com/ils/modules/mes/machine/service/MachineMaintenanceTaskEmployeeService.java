package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTaskEmployee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 维保任务计划执行人
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineMaintenanceTaskEmployeeService extends IService<MachineMaintenanceTaskEmployee> {

    /**
     * 添加
     * @param machineMaintenanceTaskEmployee
     */
    public void saveMachineMaintenanceTaskEmployee(MachineMaintenanceTaskEmployee machineMaintenanceTaskEmployee) ;
    
    /**
     * 修改
     * @param machineMaintenanceTaskEmployee
     */
    public void updateMachineMaintenanceTaskEmployee(MachineMaintenanceTaskEmployee machineMaintenanceTaskEmployee);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineMaintenanceTaskEmployee (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineMaintenanceTaskEmployee (List<String> idList);
}
