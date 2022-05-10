package com.ils.modules.mes.execution.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.util.BigDecimalUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: web 端查询生产任务VO
 * @author: fengyi
 * @date: 2021年1月13日 上午11:38:13
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkProduceTaskQueryVO implements Serializable {
    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /** 执行任务ID */
    private String id;

    /** 项目id */
    private String orderId;
    /** 项目号 */
    private String orderNo;
    /** 物料id */
    private String itemId;
    /** 物料编号 */
    private String itemCode;
    /** 名称 */
    private String itemName;
    /** 任务编号 */
    private String taskCode;
    /** 批号 */
    private String batchNo;
    /** 状态 */
    @Dict(dicCode = "mesPlanTaskExeStatus")
    private String exeStatus;
    /** 开始时间延期 */
    @Dict(dicCode = "mesProduceTaskDelayFlag")
    private String startFlag;
    /** 结束时间延期 */
    @Dict(dicCode = "mesProduceTaskDelayFlag")
    private String endFlag;
    /** 进度-已报工 */
    private BigDecimal goodQty;
    /** 进度-排程数量 */
    private BigDecimal planQty;
    /** 工序Id */
    private String processId;
    /** 工序编号 */
    private String processCode;
    /** 名称 */
    private String processName;
    /** 工位id */
    private String stationId;
    /** 工位编码 */
    private String stationCode;
    /** 工位名称 */
    private String stationName;
    /** 执行人Id */
    private String employeeId;
    /** 执行人 */
    private String employee;
    /** 是否为sop */
    private String sop;
    /** 计划开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    /** 计划结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    /** 时间开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date realStartTime;
    /** 时间结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date realEndTime;

    public String getProcessFull() {
        return this.getProcessCode() + "/" + this.getProcessName();
    }

    public String getItemFull() {
        return this.getItemCode() + "/" + this.getItemName();
    }

    public String getStationFull() {
        return this.getStationCode() + "/" + this.getStationName();
    }

    public String getTaskProgress() {
        return BigDecimalUtils.format(this.getGoodQty(), "###0.###") + "/"
            + BigDecimalUtils.format(this.getPlanQty(), "###0.###");
    }

}
