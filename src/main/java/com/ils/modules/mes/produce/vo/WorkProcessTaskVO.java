package com.ils.modules.mes.produce.vo;

import java.util.Date;

import com.ils.modules.mes.produce.entity.WorkOrderLine;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.produce.entity.WorkProcessTask;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工序任务
 * @author: fengyi
 * @date: 2020年11月23日 上午9:55:23
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkProcessTaskVO extends WorkProcessTask implements Comparable<WorkProcessTaskVO>{

    private static final long serialVersionUID = 1L;

    private String saleOrderNo;

    @Excel(name = "工单创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "工单创建时间", position = 19)
    private String orderCreateTime;

    @Excel(name = "工单状态", width = 15,dicCode = "mesWorkOrderStatus")
    @ApiModelProperty(value = "工单状态", position = 30)
    @Dict(dicCode = "mesWorkOrderStatus")
    private String status;

    /** 计划开始时间 */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 19)
    private Date planStartTime;
    /** 计划结束时间 */
    @Excel(name = "计划结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划结束时间", position = 20)
    private Date planEndTime;
    /** 实际开始时间 */
    @Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际开始时间", position = 21)
    private Date realStartTime;
    /** 实际结束时间 */
    @Excel(name = "实际结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际结束时间", position = 22)
    private Date realEndTime;

    @Override
    public int compareTo(WorkProcessTaskVO workProcessTaskVO) {
        return this.getSeq() - workProcessTaskVO.getSeq();
    }
}
