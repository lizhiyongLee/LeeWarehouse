package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;
import com.ils.modules.mes.base.factory.mapper.WorkstationEmployeeMapper;
import com.ils.modules.mes.base.factory.service.WorkstationEmployeeService;


/**
 * @Description: 车位人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Service
public class WorkstationEmployeeServiceImpl extends ServiceImpl<WorkstationEmployeeMapper, WorkstationEmployee> implements WorkstationEmployeeService {
	
	@Autowired
	private WorkstationEmployeeMapper workstationEmployeeMapper;
	
	@Override
	public List<WorkstationEmployee> selectByMainId(String mainId) {
		return workstationEmployeeMapper.selectByMainId(mainId);
	}
}
