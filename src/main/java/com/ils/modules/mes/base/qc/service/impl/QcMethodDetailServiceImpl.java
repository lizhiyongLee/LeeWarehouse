package com.ils.modules.mes.base.qc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import com.ils.modules.mes.base.qc.mapper.QcMethodDetailMapper;
import com.ils.modules.mes.base.qc.service.QcMethodDetailService;

/**
 * @Description: 质检方案关联质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Service
public class QcMethodDetailServiceImpl extends ServiceImpl<QcMethodDetailMapper, QcMethodDetail> implements QcMethodDetailService {
	
	@Autowired
	private QcMethodDetailMapper qcMethodDetailMapper;
	
	@Override
	public List<QcMethodDetail> selectByMainId(String mainId) {
		return qcMethodDetailMapper.selectByMainId(mainId);
	}
}
