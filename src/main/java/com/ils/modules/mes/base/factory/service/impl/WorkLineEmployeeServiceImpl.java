package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;
import com.ils.modules.mes.base.factory.mapper.WorkLineEmployeeMapper;
import com.ils.modules.mes.base.factory.service.WorkLineEmployeeService;


/**
 * @Description: 产线人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Service
public class WorkLineEmployeeServiceImpl extends ServiceImpl<WorkLineEmployeeMapper, WorkLineEmployee> implements WorkLineEmployeeService {
	
	@Autowired
	private WorkLineEmployeeMapper workLineEmployeeMapper;
	
	@Override
	public List<WorkLineEmployee> selectByMainId(String mainId) {
		return workLineEmployeeMapper.selectByMainId(mainId);
	}
}
