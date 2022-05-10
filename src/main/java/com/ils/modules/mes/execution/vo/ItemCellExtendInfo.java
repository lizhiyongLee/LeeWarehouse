package com.ils.modules.mes.execution.vo;

import java.math.BigDecimal;

import com.ils.modules.mes.material.entity.ItemCell;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 投料有码扫描返回物料扩展信息
 * @author: fengyi
 * @date: 2021年1月12日 上午9:22:12
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ItemCellExtendInfo extends ItemCell {

    /** 物料净投料总量=投料-撤料 */
    private BigDecimal feedQty;
    /** 工序物料总量 */
    private BigDecimal processTotalQty;
    /** 物料回撤总量 */
    private BigDecimal undoQty;
    /** workFlowType */
    private String workflowType;

    /** 工序编码 */
    private String processCode;
    /** 工序名称 */
    private String processName;
    /** 工位编码 */
    private String stationCode;
    /** 工位名称 */
    private String stationName;

}
