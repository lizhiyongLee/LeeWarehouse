package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.MachineRepairTaskEmployee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 维修任务计划执行人
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineRepairTaskEmployeeService extends IService<MachineRepairTaskEmployee> {

    /**
     * 添加
     * @param machineRepairTaskEmployee
     */
    public void saveMachineRepairTaskEmployee(MachineRepairTaskEmployee machineRepairTaskEmployee) ;
    
    /**
     * 修改
     * @param machineRepairTaskEmployee
     */
    public void updateMachineRepairTaskEmployee(MachineRepairTaskEmployee machineRepairTaskEmployee);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineRepairTaskEmployee (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineRepairTaskEmployee (List<String> idList);
}
