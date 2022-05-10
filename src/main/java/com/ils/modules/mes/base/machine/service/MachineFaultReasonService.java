package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.MachineFaultReason;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 故障原因
 * @Author: Tian
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface MachineFaultReasonService extends IService<MachineFaultReason> {

    /**
     * 添加
     * @param machineFaultReason
     */
    public void saveMachineFaultReason(MachineFaultReason machineFaultReason) ;
    
    /**
     * 修改
     * @param machineFaultReason
     */
    public void updateMachineFaultReason(MachineFaultReason machineFaultReason);
    

}
