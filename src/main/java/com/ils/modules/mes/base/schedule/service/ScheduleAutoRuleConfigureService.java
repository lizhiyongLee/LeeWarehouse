package com.ils.modules.mes.base.schedule.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;

/**
 * @Description: 自动排程规则设置
 * @Author: fengyi
 * @Date: 2021-02-22
 * @Version: V1.0
 */
public interface ScheduleAutoRuleConfigureService extends IService<ScheduleAutoRuleConfigure> {

    /**
     * 添加
     * @param scheduleAutoRuleConfigure
     */
    public void saveScheduleAutoRuleConfigure(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) ;
    
    /**
     * 修改
     * @param scheduleAutoRuleConfigure
     */
    public void updateScheduleAutoRuleConfigure(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure);
    
    /**
     * 删除
     * @param id
     */
    public void delScheduleAutoRuleConfigure (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchScheduleAutoRuleConfigure (List<String> idList);
}
