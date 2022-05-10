package com.ils.modules.mes.produce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkOrderLineMethod;
import com.ils.modules.mes.produce.mapper.WorkOrderLineMethodMapper;
import com.ils.modules.mes.produce.service.WorkOrderLineMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 工单工序质检任务
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Service
public class WorkOrderLineMethodServiceImpl extends ServiceImpl<WorkOrderLineMethodMapper, WorkOrderLineMethod> implements WorkOrderLineMethodService {
	
	@Autowired
	private WorkOrderLineMethodMapper workOrderLineMethodMapper;
	
	@Override
	public List<WorkOrderLineMethod> selectByMainId(String mainId) {
		return workOrderLineMethodMapper.selectByMainId(mainId);
	}
}
