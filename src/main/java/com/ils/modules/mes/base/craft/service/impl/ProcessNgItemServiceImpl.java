package com.ils.modules.mes.base.craft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;
import com.ils.modules.mes.base.craft.mapper.ProcessNgItemMapper;
import com.ils.modules.mes.base.craft.service.ProcessNgItemService;

/**
 * @Description: 工序关联不良项
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Service
public class ProcessNgItemServiceImpl extends ServiceImpl<ProcessNgItemMapper, ProcessNgItem> implements ProcessNgItemService {
	
	@Autowired
	private ProcessNgItemMapper processNgItemMapper;
	
	@Override
	public List<ProcessNgItem> selectByMainId(String mainId) {
		return processNgItemMapper.selectByMainId(mainId);
	}
}
