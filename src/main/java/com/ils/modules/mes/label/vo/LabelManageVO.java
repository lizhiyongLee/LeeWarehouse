package com.ils.modules.mes.label.vo;

import com.ils.modules.mes.label.entity.LabelManage;
import com.ils.modules.mes.label.entity.LabelManageLine;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 13:46
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class LabelManageVO extends LabelManage {
    /**
     * 标签管理行集合
     */
    private List<LabelManageLine> labelManageLineList;

    /**
     * 已打印数量
     */
    private Integer printedQuantity;

    /**
     * 已作废数量
     */
    private Integer cancelQuantity;

    /**
     * 已使用数量
     */
    private Integer usedQuantity;

}
