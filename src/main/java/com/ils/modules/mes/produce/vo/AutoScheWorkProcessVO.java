package com.ils.modules.mes.produce.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.modules.mes.produce.entity.WorkProcessTask;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 自动排程工序数量
 * @author: fengyi
 * @date: 2021年2月25日 上午11:20:33
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheWorkProcessVO extends WorkProcessTask {
    /** 工艺路线id */
    private String routeId;
    /** 产品bomid */
    private String productId;
    /** 1、工艺路线；2、工艺路线+物料清单；3、产品BOM */
    private String workflowType;
    /** 本次排程数量 */
    private BigDecimal schePlanQty;
    /** 任务数量 */
    private Integer taskSum;
    /** 计划开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    /** 计划结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    public String getFullItem() {
        return this.getItemCode() + "/" + this.getItemName();
    }

    public String getFullProcess() {
        return this.getSeq() + "/" + this.getProcessCode() + "/" + this.getProcessName();
    }
}
