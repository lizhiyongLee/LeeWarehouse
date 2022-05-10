package com.ils.modules.mes.material.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 看板数据
 *
 * @author Anna.
 * @date 2021/6/17 16:38
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class DashBoardDataVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private BigDecimal value;
}
