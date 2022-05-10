package com.ils.modules.mes.qc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.qc.entity.QcStateAjustRecord;
import com.ils.modules.mes.qc.mapper.QcStateAjustRecordMapper;
import com.ils.modules.mes.qc.service.QcStateAjustRecordService;
import com.ils.modules.mes.qc.vo.QcStateAjustRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 质量调整记录
 * @Author: Conner
 * @Date: 2021-03-04
 * @Version: V1.0
 */
@Service
public class QcStateAjustRecordServiceImpl extends ServiceImpl<QcStateAjustRecordMapper, QcStateAjustRecord> implements QcStateAjustRecordService {


    @Override
    public IPage<QcStateAjustRecordVO> listPage(QueryWrapper<QcStateAjustRecord> queryWrapper, Page<QcStateAjustRecord> page) {
        return baseMapper.listPage(queryWrapper,page);
    }
}
