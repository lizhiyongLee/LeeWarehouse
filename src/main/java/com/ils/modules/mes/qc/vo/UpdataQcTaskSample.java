package com.ils.modules.mes.qc.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/5/10 14:13
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UpdataQcTaskSample {

    /**
     * 总量集合
     */
    private List<QcTaskRelateTotalVO> qcTaskRelateTotalVO;
    /**
     * 抽样数
     */
    private BigDecimal sampleQty;
    /**
     * 总数
     */
    private BigDecimal totalQty;
    /**
     * 质检任务Id
     */
    private String taskId;
}
