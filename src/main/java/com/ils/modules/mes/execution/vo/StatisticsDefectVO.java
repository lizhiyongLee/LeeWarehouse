package com.ils.modules.mes.execution.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/10 13:44
 */
@Data
public class StatisticsDefectVO {
    /**
     * 工序名称
     */
    private String processName;
    /**
     * 数量
     */
    private BigDecimal defectQty;
    /**
     * 缺陷名称
     */
    private String ngItemName;
    /**
     * 工位名称
     */
    private String stationName;
    /**
     * 总数百分比
     */
    private BigDecimal percentage;
}
