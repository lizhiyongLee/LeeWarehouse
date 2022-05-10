package com.ils.modules.mes.qc.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/6/1 11:31
 */
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QcTaskRecodTraceVO extends ILSEntity {
    /**
     * 质检任务号
     */
    private String qcTaskCode;
    /**
     * 质检类型
     */
    @Dict(dicCode = "mesQcType")
    private String qcType;
    /**
     * 数量
     */
    private BigDecimal qty;
    /**
     * 质检结果
     */
    @Dict(dicCode = "mesQcResult")
    private String qcResult;
    /**
     * 执行人
     */
    private String executor;
    /**
     * 位置
     */
    private String locationName;
    /**
     * 实际执行时间
     */
    private String realStartTime;
    /**
     * 实际结束时间
     */
    private String realEndTime;
}
