package com.ils.modules.mes.qc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.qc.entity.QcStateAjustRecord;
import com.ils.modules.mes.qc.vo.QcStateAjustRecordVO;

/**
 * @Description: 质量调整记录
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcStateAjustRecordService extends IService<QcStateAjustRecord> {
    /**
     * 分页查询质量调整记录
     * @param queryWrapper
     * @param page
     * @return
     */
    public IPage<QcStateAjustRecordVO> listPage(QueryWrapper<QcStateAjustRecord> queryWrapper, Page<QcStateAjustRecord> page);
}
