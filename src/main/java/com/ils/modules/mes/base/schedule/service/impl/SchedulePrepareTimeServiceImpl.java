package com.ils.modules.mes.base.schedule.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.schedule.entity.SchedulePrepareTime;
import com.ils.modules.mes.base.schedule.mapper.SchedulePrepareTimeMapper;
import com.ils.modules.mes.base.schedule.service.SchedulePrepareTimeService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.CodeGeneratorService;

/**
 * @Description: 动态准备时间
 * @Author: fengyi
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Service
public class SchedulePrepareTimeServiceImpl extends ServiceImpl<SchedulePrepareTimeMapper, SchedulePrepareTime> implements SchedulePrepareTimeService {

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSchedulePrepareTime(SchedulePrepareTime schedulePrepareTime) {
        // 生成单号
        String no = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.PREPARE_TIME_CODE,
            schedulePrepareTime);
        schedulePrepareTime.setPrepareCode(no);
        Workstation workstation = workstationService.getById(schedulePrepareTime.getStationId());
        schedulePrepareTime.setStationCode(workstation.getStationCode());
        schedulePrepareTime.setStationName(workstation.getStationName());
            baseMapper.insert(schedulePrepareTime);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSchedulePrepareTime(SchedulePrepareTime schedulePrepareTime) {
        Workstation workstation = workstationService.getById(schedulePrepareTime.getStationId());
        schedulePrepareTime.setStationCode(workstation.getStationCode());
        schedulePrepareTime.setStationName(workstation.getStationName());

        baseMapper.updateById(schedulePrepareTime);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSchedulePrepareTime(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSchedulePrepareTime(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
