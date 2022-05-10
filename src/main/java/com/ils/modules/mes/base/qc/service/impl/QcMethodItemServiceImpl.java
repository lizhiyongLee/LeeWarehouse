package com.ils.modules.mes.base.qc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.qc.entity.QcMethodItem;
import com.ils.modules.mes.base.qc.mapper.QcMethodItemMapper;
import com.ils.modules.mes.base.qc.service.QcMethodItemService;

/**
 * @Description: 质检方案关联物料
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Service
public class QcMethodItemServiceImpl extends ServiceImpl<QcMethodItemMapper, QcMethodItem> implements QcMethodItemService {
	
	@Autowired
	private QcMethodItemMapper qcMethodItemMapper;
	
	@Override
	public List<QcMethodItem> selectByMainId(String mainId) {
		return qcMethodItemMapper.selectByMainId(mainId);
	}
}
