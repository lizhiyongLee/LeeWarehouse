package com.ils.modules.mes.qc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.qc.entity.QcStateAjustRecord;
import com.ils.modules.mes.qc.vo.QcStateAjustRecordVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 质量调整记录
 * @Author: Tian
 * @Date: 2021-03-04
 * @Version: V1.0
 */
public interface QcStateAjustRecordMapper extends ILSMapper<QcStateAjustRecord> {
    /**
     * 分页查询质量调整记录
     *
     * @param queryWrapper
     * @param page
     * @return
     */
    public IPage<QcStateAjustRecordVO> listPage(@Param("ew") QueryWrapper<QcStateAjustRecord> queryWrapper, Page<QcStateAjustRecord> page);
}
