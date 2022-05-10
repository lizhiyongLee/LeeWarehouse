package com.ils.modules.mes.base.schedule.service.impl;

import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;
import com.ils.modules.mes.base.schedule.mapper.ScheduleAutoRuleConfigureMapper;
import com.ils.modules.mes.base.schedule.service.ScheduleAutoRuleConfigureService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 自动排程规则设置
 * @Author: Conner
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@Service
public class ScheduleAutoRuleConfigureServiceImpl extends ServiceImpl<ScheduleAutoRuleConfigureMapper, ScheduleAutoRuleConfigure> implements ScheduleAutoRuleConfigureService {

    @Override
    public void saveScheduleAutoRuleConfigure(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
         baseMapper.insert(scheduleAutoRuleConfigure);
    }

    @Override
    public void updateScheduleAutoRuleConfigure(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
        baseMapper.updateById(scheduleAutoRuleConfigure);
    }

    @Override
    public void delScheduleAutoRuleConfigure(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchScheduleAutoRuleConfigure(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
