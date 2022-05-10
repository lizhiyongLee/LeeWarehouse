package com.ils.modules.mes.produce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkOrderLineStation;

import java.util.List;

/**
 * @Description: 工单工序工位
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderLineStationService extends IService<WorkOrderLineStation> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkOrderLineStation> selectByMainId(String mainId);
}
