package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkOrderItemBom;

/**
 * @Description: 工单物料清单明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderItemBomService extends IService<WorkOrderItemBom> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkOrderItemBom> selectByMainId(String mainId);
}
