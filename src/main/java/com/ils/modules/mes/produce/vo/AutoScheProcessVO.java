package com.ils.modules.mes.produce.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 自动排程工序列表
 * @author: fengyi
 * @date: 2021年2月24日 下午4:38:42
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheProcessVO implements Serializable {
    /** 工序任务id */
    private String processTaskId;
    /** 工单id */
    private String orderId;
    /** 工单号 */
    private String orderNo;
    /** 生产批次 */
    private String batchNo;
    /** 成品物料id */
    private String itemId;
    /** 成品物料编码 */
    private String itemCode;
    /** 成品物料名称 */
    private String itemName;
    /** 成品物料规格 */
    private String spec;
    /** 工序id */
    private String processId;
    /** 工序 */
    private String processName;
    /** 工序编码 */
    private String processCode;
    /** 工序 */
    private Integer seq;
    /** 计划数量 */
    private BigDecimal planQty;
    /** 任务数量 */
    private Integer taskSum;

}
