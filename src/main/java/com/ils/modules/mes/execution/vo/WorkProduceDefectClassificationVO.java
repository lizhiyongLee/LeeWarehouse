package com.ils.modules.mes.execution.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/4 13:39
 */
@Data
public class WorkProduceDefectClassificationVO {
    private String taskId;
    private String ngTypeId;
    private String ngItemId;
    private BigDecimal qty;
}
