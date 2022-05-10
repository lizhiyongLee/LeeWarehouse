package com.ils.modules.mes.produce.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lishaojie
 * @date 2021/5/27 14:57
 */
@Data
public class GanttChartVO {

    /**
     * 甘特图id:任务id
     */
    private String id;
    /**
     * 任务名称
     */
    private String label;
    /**
     * 工位id
     */
    private String rowId;
    /**
     * 工位名称
     */
    private String rowName;
    /**
     * 工位名称
     */
    private List<String> optionalStationId;
    /**
     * 展示状态：已排程、已下发、生产、暂停、结束
     */
    private String status;
    /**
     * 1、锁定；0、释放；
     */
    private String lockStatus;

    /**
     * start和end
     */
    private Map<String, Long> time;
    /**
     * 工位类型，是否允许该工位同时进行多个任务
     */
    private String multistation;

    /**
     * 工单id
     */
    private String orderId;

    /**
     * 工单号
     */
    private String orderNo;

    /**
     * 工序编码
     */
    private String processCode;

    /**
     * 1,排时间；2，排班次；
     * dicCode = "mesTaskPlanType"
     */
    private String planType;

}
