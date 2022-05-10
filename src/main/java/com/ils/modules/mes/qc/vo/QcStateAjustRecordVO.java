package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.qc.entity.QcStateAjustRecord;
import lombok.*;

/**
 * 质检任务调整记录vo类
 *
 * @author Anna.
 * @date 2021/5/27 14:37
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcStateAjustRecordVO extends QcStateAjustRecord {
    private static final long serialVersionUID = 1L;
    /**
     * 质量调整记录
     */
    private String adjustRecord;
}
