package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.qc.entity.QcTask;
import lombok.*;

/**
 * 质检任务vo类型
 *
 * @author Anna.
 * @date 2021/3/1 16:46
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcTaskVO extends QcTask {
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;
    /**
     * 计划执行人员
     */
    private String taskEmployee;
    /**
     * 次品数
     */
    private String badQty;
    /**
     * 不合格率
     */
    private String badRate;
    /**
     * 销售订单编号
     */
    private String saleOrderNo;
}
