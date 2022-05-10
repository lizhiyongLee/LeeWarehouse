package com.ils.modules.mes.execution.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lishaojie
 * @description
 * @date 2021/12/16 10:00
 */
@Data
public class MaterialRecordReportVO {
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    private String itemName;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    private String itemCode;
    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 20)
    private String orderNo;
    /**
     * 工序
     */
    @Excel(name = "工序", width = 15)
    private String processName;
    /**
     * 工序编码
     */
    @Excel(name = "工序编码", width = 15)
    private String processCode;
    /**
     * 工位
     */
    @Excel(name = "工位", width = 15)
    private String stationName;
    /**
     * 工位编码
     */
    @Excel(name = "工位编码", width = 15)
    private String stationCode;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    private BigDecimal qty;

    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    private String unitName;

    private String groupBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
