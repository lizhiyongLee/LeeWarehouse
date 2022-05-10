package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/26 16:34
 */
public enum CalendarTypeEnum {
    /**
     * 生产日历
     */
    PRODUCT("1", "生产日历"),
    /**
     * 设备日历
     */
    MACHINE("2", "设备日历"),
    /**
     * 质量日历
     */
    QC("3", "质量日历");

    private final String value;

    private final String desc;

    private CalendarTypeEnum(String value, String desc) {
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
