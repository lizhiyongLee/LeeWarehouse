package com.ils.modules.mes.execution.vo;

import java.math.BigDecimal;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.enums.WorkflowTypeEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 分组查询
 * @author: fengyi
 * @date: 2020年12月17日 上午11:53:30
 */
@Setter
@Getter
@ToString(callSuper = true)
public class GroupMaterialRecordVO extends ILSEntity {
    /** 物料ID */
    private String itemId;
    /** 物料name */
    private String itemName;
    /** 物料code */
    private String itemCode;
    /** 物料单位 */
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String mainUnit;
    /** 启动二维码1，是；0，否； */
    private String qrcodeFlag;
    /** 批次管理 */
    private String batch;

    /** 物料投料总量 */
    private BigDecimal feedQty;
    /** 工序物料总量 */
    private BigDecimal processTotalQty;
    /** 物料回撤总量 */
    private BigDecimal undoQty;
    /** workFlowType */
    private String workflowType;

    /** 投料进度信息 */
    public String getFeedProcessQtyInfo() {
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(this.getWorkflowType())) {
            return BigDecimalUtils.format(BigDecimalUtils.subtract(this.getFeedQty(), this.getUndoQty()), "###0.###")
                + "/" + BigDecimalUtils.format(this.getProcessTotalQty(), "###0.###");
        }else {
            return BigDecimalUtils.format(this.getFeedQty(), "###0.###");
        }
    }
}
