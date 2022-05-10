package com.ils.modules.mes.execution.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 查询投料数量VO
 * @author: fengyi
 * @date: 2020年12月16日 下午1:37:03
 */
@Setter
@Getter
@ToString(callSuper = true)
public class MaterialRecordQtyVO {
    /** 物料ID */
    private String itemId;
    /** 任务ID */
    private String produceTaskId;
    /** 投料数量 */
    private BigDecimal feedQty;
    /** 测回数量 */
    private BigDecimal undoQty;
}
