package com.ils.modules.mes.base.factory.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.framework.poi.excel.annotation.Excel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 添加工作日历VO
 * @author: fengyi
 * @date: 2020年10月21日 下午1:52:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkCalendarParamsVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 开始日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    /**
     * 工作日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    /** 1,生产日历；2，设备日历；3，质量日历； */
    @Excel(name = "1,生产日历；2，设备日历；3，质量日历；", width = 15)
    private String type;

    /** 工作日；休息日；法定节假日； */
    @Excel(name = "工作日；休息日；法定节假日；", width = 15)
    private String dateType;

    /**设置星期周几：用逗号隔开*/
    private String weeks;

    /** 设置班次：用逗号隔开 */
    private String shiftIds;

    /** 设置工位：用逗号隔开;重置是一个单工位字段 */
    private String stationIds;
}
