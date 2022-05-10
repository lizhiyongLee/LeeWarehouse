package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.machine.entity.MachineFaultAppearance;
import com.ils.modules.mes.base.machine.mapper.MachineFaultAppearanceMapper;
import com.ils.modules.mes.base.machine.service.MachineFaultAppearanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 故障现象
 * @Author: Conner
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class MachineFaultAppearanceServiceImpl extends ServiceImpl<MachineFaultAppearanceMapper, MachineFaultAppearance> implements MachineFaultAppearanceService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineFaultAppearance(MachineFaultAppearance machineFaultAppearance) {
         baseMapper.insert(machineFaultAppearance);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineFaultAppearance(MachineFaultAppearance machineFaultAppearance) {
        baseMapper.updateById(machineFaultAppearance);
    }


    @Override
    public List<DictModel> queryDictFaultAppearance() {
        return baseMapper.queryDictFaultAppearance();
    }


}
