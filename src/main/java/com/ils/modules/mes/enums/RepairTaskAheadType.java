package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/9/9 15:42
 */
public enum RepairTaskAheadType {
    /**
     * 提前1小时
     */
    ONE_HOUR("1", "提前1小时"),
    /**
     * 提前8小时
     */
    EIGHT_HOURS("2", "提前8小时"),
    /**
     * 提前1天
     */
    ONE_DAY("3", "提前1天"),
    /**
     * 提前1周
     */
    ONE_WEEK("4", "提前1周"),
    /**
     * 不提醒
     */
    MAINTENANCE("5", "不提醒");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    RepairTaskAheadType(String value, String desc) {
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
