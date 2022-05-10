package com.ils.modules.mes.produce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkOrderLineStation;
import com.ils.modules.mes.produce.mapper.WorkOrderLineStationMapper;
import com.ils.modules.mes.produce.service.WorkOrderLineStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 工单工序工位
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Service
public class WorkOrderLineStationServiceImpl extends ServiceImpl<WorkOrderLineStationMapper, WorkOrderLineStation> implements WorkOrderLineStationService {
	
	@Autowired
	private WorkOrderLineStationMapper workOrderLineStationMapper;
	
	@Override
	public List<WorkOrderLineStation> selectByMainId(String mainId) {
		return workOrderLineStationMapper.selectByMainId(mainId);
	}
}
