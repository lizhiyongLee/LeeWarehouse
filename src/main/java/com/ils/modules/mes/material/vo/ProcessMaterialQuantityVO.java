package com.ils.modules.mes.material.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 工序物料数量VO
 *
 * @author Anna.
 * @date 2021/6/16 17:08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ProcessMaterialQuantityVO {

    /**
     * 工序名称
     */
    private String processName;
    /**
     * 工序编码
     */
    private String processCode;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 占比
     */
    private BigDecimal percentage;
}
