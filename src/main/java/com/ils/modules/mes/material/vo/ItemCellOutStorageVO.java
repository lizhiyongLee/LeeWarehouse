package com.ils.modules.mes.material.vo;

import com.ils.modules.mes.material.entity.ItemCell;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 库存出库出库数量
 *
 * @author Anna.
 * @date 2021/6/9 14:08
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ItemCellOutStorageVO extends ItemCell {
    /**
     * 出库数量
     */
    private BigDecimal outQty;
}
