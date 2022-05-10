package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkOrderLine;

/**
 * @Description: 工单工序明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderLineService extends IService<WorkOrderLine> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkOrderLine> selectByMainId(String mainId);
}
