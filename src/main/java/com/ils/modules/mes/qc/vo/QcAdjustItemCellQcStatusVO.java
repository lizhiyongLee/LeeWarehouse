package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.material.entity.ItemCell;
import lombok.*;

import java.util.List;

/**
 * 质量状态调整物料集合list
 *
 * @author Anna.
 * @date 2021/3/8 10:32
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcAdjustItemCellQcStatusVO {
    private static final long serialVersionUID = 1L;
    /**
     * 物料集合
     */
    private List<ItemCell> itemCellList;
    /**
     * 调整状态值
     */
    private String qcStatus;
    /**
     * 备注
     */
    private String note;
}
