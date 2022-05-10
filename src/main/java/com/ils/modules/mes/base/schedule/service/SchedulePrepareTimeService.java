package com.ils.modules.mes.base.schedule.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.schedule.entity.SchedulePrepareTime;

/**
 * @Description: 动态准备时间
 * @Author: fengyi
 * @Date: 2021-02-05
 * @Version: V1.0
 */
public interface SchedulePrepareTimeService extends IService<SchedulePrepareTime> {

    /**
     * 添加
     * @param schedulePrepareTime
     */
    public void saveSchedulePrepareTime(SchedulePrepareTime schedulePrepareTime) ;
    
    /**
     * 修改
     * @param schedulePrepareTime
     */
    public void updateSchedulePrepareTime(SchedulePrepareTime schedulePrepareTime);
    
    /**
     * 删除
     * @param id
     */
    public void delSchedulePrepareTime (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSchedulePrepareTime (List<String> idList);
}
