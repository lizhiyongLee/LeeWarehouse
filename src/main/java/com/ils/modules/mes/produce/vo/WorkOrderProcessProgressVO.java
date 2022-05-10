package com.ils.modules.mes.produce.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 工单工序进度VO
 * @author lishaojie
 * @date 2021/5/25 9:57
 */
@Data
public class WorkOrderProcessProgressVO  extends ILSEntity {
    /**
     * 工单层级
     */
    private Integer orderLayer;
    /**
     * 工单编号
     */
    private String orderNo;
    /**
     * 工序名称
     */
    private String processName;
    /**
     * 工序编码
     */
    private String processCode;
    /**
     * 上道工序编码
     */
    private String priorCode;
    /**
     * 下道工序编码
     */
    private String nextCode;
    /**
     * 物料名称
     */
    private String itemName;
    /**
     * 物料编码
     */
    private String itemCode;
    /**
     *产品规格
     */
    private String spec;
    /**
     * 计划数量
     */
    private BigDecimal planQty;
    /**
     * 单位
     */
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String unit;
    /**
     * 完成数量
     */
    private BigDecimal completedQty;
    /**
     * 排程数量
     */
    private BigDecimal scheduledQty;
    /**
     * 下发数量
     */
    private BigDecimal publishQty;

    /**
     * 完成数量百分比
     */
    private BigDecimal completedPercentage;
}
