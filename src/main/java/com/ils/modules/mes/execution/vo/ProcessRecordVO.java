package com.ils.modules.mes.execution.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 统计工序任务报工数量
 * @author: fengyi
 * @date: 2021年1月11日 上午11:26:21
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ProcessRecordVO implements Serializable {
    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = 1L;

    /**
     * 工序任务Id
     */
    private String processTaskId;

    /**
     * 工序任务报工数量
     */
    private BigDecimal submitTotalQty;

}
