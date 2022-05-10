package com.ils.modules.mes.base.qc.vo;

import lombok.Getter;
import lombok.ToString;

/**
 * 用来结束查询质检方案的参数，物料类型Id和质检类型
 *
 * @author Anna.
 * @date 2021/3/3 15:24
 */
@Getter
@ToString
public class QcMethodWithQcTypeVO {
    /**
     * 物料类型Id集合
     */
    private String itemIds;
    /**
     * 质检类型
     */
    private String qcType;
}
