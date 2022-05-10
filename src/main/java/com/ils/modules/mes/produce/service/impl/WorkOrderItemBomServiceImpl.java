package com.ils.modules.mes.produce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkOrderItemBom;
import com.ils.modules.mes.produce.mapper.WorkOrderItemBomMapper;
import com.ils.modules.mes.produce.service.WorkOrderItemBomService;

/**
 * @Description: 工单物料清单明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Service
public class WorkOrderItemBomServiceImpl extends ServiceImpl<WorkOrderItemBomMapper, WorkOrderItemBom> implements WorkOrderItemBomService {
	
	@Autowired
	private WorkOrderItemBomMapper workOrderItemBomMapper;
	
	@Override
	public List<WorkOrderItemBom> selectByMainId(String mainId) {
		return workOrderItemBomMapper.selectByMainId(mainId);
	}
}
