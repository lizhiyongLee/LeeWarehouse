package com.ils.modules.mes.produce.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.vo.RequiredMaterialVO;
import com.ils.modules.mes.qc.vo.QcTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 工单
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderMapper extends ILSMapper<WorkOrder> {
    /**
     * 查询子工单列表
     *
     * @param fatherOrderId 父工单id
     * @return 子工单id列表
     */
    List<WorkOrder> listByFatherOrderId(String fatherOrderId);

    /**
     * 执行传入的sql
     *
     * @param sql
     * @author niushuai
     * @date: 2021/11/10 13:34:11
     * @return: {@link List< String>}
     */
    List<String> executeSql(@Param("sql") String sql);

    /**
     * 获取报表数据
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<RequiredMaterialVO> getRequiredMaterial(Page<RequiredMaterialVO> page, @Param("ew") QueryWrapper<RequiredMaterialVO> queryWrapper);
}
