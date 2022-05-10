package com.ils.modules.mes.enums;

/**
 * @Description: 日历类型
 * @author: fengyi
 * @date: 2021年1月29日 上午9:24:56
 */

public enum WorkCalendarTypeEnum {
    /** 生产 */
    PRODUCTION("1","生产"),
    /** 设备 */
    BASIC ("2","设备"),
    /** 质量 */
    QUALITY("3", "质量");
    
    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkCalendarTypeEnum(String value, String desc) {
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
