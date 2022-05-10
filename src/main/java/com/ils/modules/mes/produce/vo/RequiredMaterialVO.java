package com.ils.modules.mes.produce.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lishaojie
 * @description
 * @date 2021/12/15 16:19
 */
@Data
public class RequiredMaterialVO {
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    private String itemName;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    private String itemCode;
    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 20)
    private String orderNo;
    /**
     * 工序
     */
    @Excel(name = "工序", width = 15)
    private String processName;
    /**
     * 工序编码
     */
    @Excel(name = "工序编码", width = 15)
    private String processCode;
    /**
     * 需求数量
     */
    @Excel(name = "需求数量", width = 20)
    private BigDecimal totalQty;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    private String unitName;
    /**
     * 需求时间
     */
    @Excel(name = "需求时间", width = 40)
    private String requiredTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planStartTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planEndTime;
}
