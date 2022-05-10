package com.ils.modules.mes.produce.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import lombok.*;

/**
 * 工单物料bomVO
 *
 * @author Anna.
 * @date 2021/2/3 9:51
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkOrderBomVO extends WorkOrderBom {
    /**
     * 销售订单Id
     */
    private String saleOrderId;
    /**
     *销售订单编号
     */
    @KeyWord
    @TableField("sale_order_no")
    private String saleOrderCode;
    /**
     * 工单id
     */
    private String workOrderId;
    /**
     * 工单编号
     */
    @KeyWord
    @TableField("order_no")
    private String workOrderCode;
}
