package com.ils.modules.mes.base.material.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 转换单位
 * @author: fengyi
 * @date: 2021年1月21日 下午2:50:57
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ConvertUnitVO {

    public String convertUnit;

    public String mainUnit;

    private BigDecimal mainUnitQty;

    private BigDecimal convertQty;

}
