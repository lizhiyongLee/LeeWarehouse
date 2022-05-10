package com.ils.modules.mes.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import com.ils.modules.mes.produce.mapper.WorkOrderBomMapper;
import com.ils.modules.mes.produce.service.WorkOrderBomService;
import com.ils.modules.mes.produce.vo.WorkOrderBomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 工单产品BOM
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Service
public class WorkOrderBomServiceImpl extends ServiceImpl<WorkOrderBomMapper, WorkOrderBom> implements WorkOrderBomService {
	
	@Autowired
	private WorkOrderBomMapper workOrderBomMapper;
	
	@Override
	public List<WorkOrderBom> selectByMainId(String mainId) {
		return workOrderBomMapper.selectByMainId(mainId);
	}

	@Override
	public IPage<WorkOrderBomVO> listPageWorkOrderBom(Page<WorkOrderBomVO> page, QueryWrapper<WorkOrderBomVO> queryWrapper) {
		return baseMapper.listPageWorkOrderBom(page, queryWrapper);
	}
}
