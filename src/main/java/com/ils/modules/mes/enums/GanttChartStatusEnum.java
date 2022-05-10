package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/8 8:56
 */
public enum GanttChartStatusEnum {
    /**
     * 已排程
     */
    SCHEDULED("1", "已排程"),
    /**
     * 已下发
     */
    ISSUED("2", "已下发"),

    /**
     * 生产
     */
    PRODUCE("3","生产"),

    /**
     * 已下发
     */
    SUSPEND("4","暂停"),

    /**
     * 已下发
     */
    FINISH("5","结束");


    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    GanttChartStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
