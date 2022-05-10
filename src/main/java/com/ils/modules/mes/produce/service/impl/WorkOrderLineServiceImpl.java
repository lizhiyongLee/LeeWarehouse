package com.ils.modules.mes.produce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import com.ils.modules.mes.produce.mapper.WorkOrderLineMapper;
import com.ils.modules.mes.produce.service.WorkOrderLineService;

/**
 * @Description: 工单工序明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Service
public class WorkOrderLineServiceImpl extends ServiceImpl<WorkOrderLineMapper, WorkOrderLine> implements WorkOrderLineService {
	
	@Autowired
	private WorkOrderLineMapper workOrderLineMapper;
	
	@Override
	public List<WorkOrderLine> selectByMainId(String mainId) {
		return workOrderLineMapper.selectByMainId(mainId);
	}
}
