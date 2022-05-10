package com.ils.modules.mes.execution.vo;

import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日生产报表导出类
 *
 * @author Anna.
 * @date 2021/12/13 11:12
 */
@Data
public class DayProductDetailExportVO extends ILSEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "产品名称", width = 15)
    private String itemName;

    @Excel(name = "产品编码", width = 15)
    private String itemCode;

    @Excel(name = "工序", width = 15)
    private String processName;

    @Excel(name = "工序编码", width = 15)
    private String processCode;


    @Excel(name = "工位名称", width = 15)
    private String stationName;


    @Excel(name = "工位编码", width = 15)
    private String stationCode;


    @Excel(name = "工单号", width = 15)
    private String orderNo;


    @Excel(name = "生产批号", width = 15)
    private String batchNo;


    @Excel(name = "标签码", width = 15)
    private String qrcode;


    @Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
    private Date produceDate;


    @Excel(name = "数量", width = 15)
    private BigDecimal submitQty;


    @Excel(name = "单位", width = 15, dicCode = "id", dictTable = "mes_unit", dicText = "unit_name")
    private String unit;


    @Excel(name = "生产人员", width = 15)
    private String employeeName;


    @Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;


    @Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
