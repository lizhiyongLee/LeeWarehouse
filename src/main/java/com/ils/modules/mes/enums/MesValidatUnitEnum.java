package com.ils.modules.mes.enums;

/**
 * 时间单位枚举类
 *
 * @author fengyi
 * @date 2020/07/14
 */
public enum MesValidatUnitEnum {
    /**
     * 分钟
     */
    MINUTE("1", "分钟"),
    /**
     * 小时
     */
    HOUR("2", "小时"),
    /**
     * 天
     */
    DAY("3", "天"),
    /**
     * 月
     */
    MONTH("4", "月"),
    /**
     * 年
     */
    YEAR("5", "年");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    MesValidatUnitEnum(String value, String desc) {
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
