package com.ils.modules.mes.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkOrderLineMethod;

import java.util.List;

/**
 * @Description: 工单工序质检任务
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderLineMethodService extends IService<WorkOrderLineMethod> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkOrderLineMethod> selectByMainId(String mainId);
}
