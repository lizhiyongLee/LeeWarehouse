package com.ils.modules.mes.material.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 批次
 * @author: fengyi
 * @date: 2020年12月15日 下午2:26:54
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ItemCellBatchVO {
    /** 批次 */
    public String batchNo;
    /** 质量状态 */
    public String qcStatus;
    /** 数量 */
    public BigDecimal qty;
    /** 单位ID */
    private String unit;
    /** 单位名称 */
    private String unitName;
}
