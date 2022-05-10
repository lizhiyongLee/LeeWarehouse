package com.ils.modules.mes.machine.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * 备件仓库数量
 *
 * @author Anna.
 * @date 2021/3/1 9:17
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SparePartsQtyVO extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 仓库名称
     */
    private String storageName;
    /**
     * 该仓库下的备件数量
     */
    private BigDecimal sparePartsQty;
}
