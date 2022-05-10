package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.mapper.WorkShopEmployeeMapper;
import com.ils.modules.mes.base.factory.service.WorkShopEmployeeService;
import com.ils.modules.mes.base.factory.vo.UserPosiztionVO;


/**
 * @Description: 车间人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Service
public class WorkShopEmployeeServiceImpl extends ServiceImpl<WorkShopEmployeeMapper, WorkShopEmployee> implements WorkShopEmployeeService {
	
	@Autowired
	private WorkShopEmployeeMapper workShopEmployeeMapper;
	
	@Override
	public List<WorkShopEmployee> selectByMainId(String mainId) {
		return workShopEmployeeMapper.selectByMainId(mainId);
	}

    @Override
    public IPage<UserPosiztionVO> queryUserPositionInfo(IPage<UserPosiztionVO> page,
        QueryWrapper<UserPosiztionVO> queryWrapper) {
        return workShopEmployeeMapper.queryUserPositionInfo(page, queryWrapper);
    }
}
