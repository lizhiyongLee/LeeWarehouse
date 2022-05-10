package com.ils.modules.mes.base.product.vo;

import java.math.BigDecimal;

import com.ils.modules.mes.base.product.entity.ItemBomDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 物料清单明细+对应单位的转换关系
 * @author: fengyi
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemBomUnitVO extends ItemBomDetail {
    /** 主单位数量 */
    private BigDecimal mainUnitQty;
    /** 主单位 */
    private String mainUnit;
    /** 转换单位数量 */
    private BigDecimal convertQty;
    /** 转换单位 */
    private String convertUnit;
}
