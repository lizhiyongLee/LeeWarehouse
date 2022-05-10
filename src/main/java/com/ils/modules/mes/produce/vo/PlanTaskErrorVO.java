package com.ils.modules.mes.produce.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 错误提示信息
 * @author: fengyi
 * @date: 2020年11月30日 下午3:47:59
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PlanTaskErrorVO {
    /** 生产任务单号 */
    @Excel(name = "生产任务单号", width = 15)
    @ApiModelProperty(value = "生产任务单号", position = 2)
    private String taskCode;

    /** 工单号 */
    @Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 6)
    private String orderNo;

    /** 工序 */
    @Excel(name = "工序", width = 15)
    @ApiModelProperty(value = "工序", position = 6)
    private String processInfo;

    /** 排程数量 */
    @Excel(name = "排程数量", width = 15)
    @ApiModelProperty(value = "排程数量", position = 17)
    private BigDecimal planQty;

    /** 计划开始时间 */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 24)
    private Date planStartTime;

    /** 计划完工时间 */
    @Excel(name = "计划完工时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划完工时间", position = 27)
    private Date planEndTime;

    /** 计划开始时间 */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 24)
    private Date orderStartTime;

    /** 计划完工时间 */
    @Excel(name = "计划完工时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划完工时间", position = 27)
    private Date orderEndTime;

    /**
     * 错误提示信息
     */
    private String errMsg;
}
