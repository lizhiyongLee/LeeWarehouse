package com.ils.modules.mes.base.machine.service.impl;

import com.ils.modules.mes.base.machine.entity.MachineFaultReason;
import com.ils.modules.mes.base.machine.mapper.MachineFaultReasonMapper;
import com.ils.modules.mes.base.machine.service.MachineFaultReasonService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 故障原因
 * @Author: Conner
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class MachineFaultReasonServiceImpl extends ServiceImpl<MachineFaultReasonMapper, MachineFaultReason> implements MachineFaultReasonService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineFaultReason(MachineFaultReason machineFaultReason) {
         baseMapper.insert(machineFaultReason);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineFaultReason(MachineFaultReason machineFaultReason) {
        baseMapper.updateById(machineFaultReason);
    }

}
