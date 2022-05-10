package com.ils.modules.mes.qc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * X-R控制图数据vo
 *
 * @author Anna.
 * @date 2021/6/24 10:07
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class XrControlChartVO {

    /**
     * 物料名称
     */
    private String itemName;
    /**
     * 质检方案
     */
    private String qcMethod;
    /**
     * 控制项
     */
    private String ControlItem;
    /**
     * 位置
     */
    private String location;
    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
