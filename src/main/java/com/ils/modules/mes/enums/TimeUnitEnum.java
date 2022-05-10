package com.ils.modules.mes.enums;

/**
 * @Description: 时间单位枚举类
 * @author: fengyi
 * @date: 2021年2月3日 下午4:41:56
 */

public enum TimeUnitEnum {
    /** 秒 */
    ss("0", "秒"),
    /** 分钟 */
    mi("1", "分钟"),
    /** 小时 */
    hh("2", "小时"),
    /** 质量 */
    dd("3", "天");
    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    TimeUnitEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getEnumDesc(String value) {
        for (TimeUnitEnum e : TimeUnitEnum.values()) {
            if (e.getValue().equals(value)) {
                return e.getDesc();
            }
        }
        return "";
    }

    public static TimeUnitEnum getTimeUnitEnum(String value) {
        for (TimeUnitEnum e : TimeUnitEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
