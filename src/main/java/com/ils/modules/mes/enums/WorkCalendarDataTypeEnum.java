package com.ils.modules.mes.enums;

/**
 * @Description: 工作日历日期类型
 * @author: fengyi
 */

public enum WorkCalendarDataTypeEnum {
    /** 工作日 */
    WORK("1", "工作日"),
    /** 休息日 */
    DAY_OFF("2", "休息日"),
    /** 法定节假日 */
    STATUTORY_HOLIDAY("3", "法定节假日");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkCalendarDataTypeEnum(String value, String desc) {
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
