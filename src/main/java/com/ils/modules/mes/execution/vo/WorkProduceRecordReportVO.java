package com.ils.modules.mes.execution.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author lishaojie
 * @description 生产报表
 * @date 2021/12/10 10:55
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkProduceRecordReportVO {
    /**
     * id
     */
    private String id;
    /**
     * 工序id
     */
    @Excel(name = "工序id", width = 30)
    private String processId;
    /**
     * 工序编码
     */
    @Excel(name = "工序编码", width = 15)
    private String processCode;
    /**
     * 工序名称
     */
    @Excel(name = "工序名称", width = 15)
    private String processName;
    /**
     * 产品id
     */
    @Excel(name = "产品id", width = 30)
    private String itemId;
    /**
     * 产品编码
     */
    @Excel(name = "产品编码", width = 15)
    private String itemCode;
    /**
     * 产品编码
     */
    @Excel(name = "产品编码", width = 15)
    private String itemName;
    /**
     * 报工数量
     */
    @Excel(name = "报工数量", width = 15)
    private BigDecimal submitQty;
    /**
     * 员工IDs
     */
    @Excel(name = "员工ID", width = 30)
    private String employeeId;
    /**
     * 员工名称
     */
    @Excel(name = "员工名称", width = 15)
    private String employeeName;
    /**
     * 工位id
     */
    @Excel(name = "工位id", width = 30)
    private String stationId;
    /**
     * 工位编码
     */
    @Excel(name = "工位编码", width = 15)
    private String stationCode;
    /**
     * 工位名称
     */
    @Excel(name = "工位名称", width = 15)
    private String stationName;
    /**
     * 报表日期
     */
    @Excel(name = "报表日期", width = 30)
    private String reportDate;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称", width = 15)
    private String unitName;
}
