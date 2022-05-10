package com.ils.modules.mes.execution.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.execution.entity.WorkProduceDefectClassification;
import com.ils.modules.mes.execution.vo.StatisticsDefectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/3 10:58
 */
public interface WorkProduceDefectClassificationMapper extends ILSMapper<WorkProduceDefectClassification> {
    /**
     * 统计缺陷前五的工序
     *
     * @return
     */
    List<String> statisticsDefectProcessMax();

    /**
     * 根据工序名称查询
     *
     * @param processName
     * @return
     */
    List<StatisticsDefectVO> statisticsDefectByProcess(String processName);

    /**
     * 统计缺陷前五的工位
     *
     * @return
     */
    List<String> statisticsDefectStationMax();

    /**
     * 根据工位名称查询
     *
     * @param stationName
     * @return
     */
    List<StatisticsDefectVO> statisticsDefectByStation(String stationName);

    /**
     * 质量柏拉图
     *
     * @param tenantId
     * @return
     */
    List<StatisticsDefectVO> selectQualityPlato(@Param(value = "tenantId")String tenantId);
}
