package com.ils.modules.mes.material.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 以物料为工序的统计物料数量看板
 *
 * @author Anna.
 * @date 2021/6/16 17:51
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class DashBoardProcessItemQtyVO {
    /**
     * 工序id
     */
    private String processId;
    /**
     * 工序名称
     */
    private String name;
    /**
     * 值
     */
    private BigDecimal value;
}
