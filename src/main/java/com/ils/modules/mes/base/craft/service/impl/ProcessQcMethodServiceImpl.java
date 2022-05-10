package com.ils.modules.mes.base.craft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ProcessQcMethod;
import com.ils.modules.mes.base.craft.mapper.ProcessQcMethodMapper;
import com.ils.modules.mes.base.craft.service.ProcessQcMethodService;
import com.ils.modules.mes.base.craft.vo.ProcessQcMethodVO;

/**
 * @Description: 工序关联质检
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Service
public class ProcessQcMethodServiceImpl extends ServiceImpl<ProcessQcMethodMapper, ProcessQcMethod> implements ProcessQcMethodService {
	
	@Autowired
	private ProcessQcMethodMapper processQcMethodMapper;
	
	@Override
    public List<ProcessQcMethodVO> selectByMainId(String mainId) {
		return processQcMethodMapper.selectByMainId(mainId);
	}
}
