package com.ils.modules.mes.execution.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.vo.ProcessRecordVO;

/**
 * @Description: 产出记录
 * @Author: fengyi
 */
public interface WorkProduceRecordMapper extends ILSMapper<WorkProduceRecord> {

    /**
     * 
     * 查询统计工序任务报工数量
     * 
     * @param queryWrapper
     * @return
     * @date 2021年1月11日
     */
    List<ProcessRecordVO> queryProcessSubmitQty(@Param("ew") QueryWrapper<ProcessRecordVO> queryWrapper);
}
