package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.MachineData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备类型关联读数
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineDataService extends IService<MachineData> {

    /**
     * 添加
     * @param machineData
     */
    public void saveMachineData(MachineData machineData) ;
    
    /**
     * 修改
     * @param machineData
     */
    public void updateMachineData(MachineData machineData);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineData (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineData (List<String> idList);
}
