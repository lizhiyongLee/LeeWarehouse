package com.ils.modules.mes.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.vo.AutoScheParam;

/**
 * @Description: 自动排程服务
 * @author: fengyi
 * @date: 2021年2月24日 下午4:51:49
 */
public interface AutoScheService extends IService<WorkProcessTask> {

    /**
     * 
     * 自动排程服务
     * 
     * @param autoScheParam
     * @date 2021年2月24日
     */
    public void autoSche(AutoScheParam autoScheParam);

}
