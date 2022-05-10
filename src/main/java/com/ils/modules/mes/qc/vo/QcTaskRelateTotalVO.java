package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.qc.entity.QcTaskRelateTotal;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/5/14 14:05
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcTaskRelateTotalVO extends QcTaskRelateTotal {
    /**
     * 是否是样本
     */
    private String sample;
}
