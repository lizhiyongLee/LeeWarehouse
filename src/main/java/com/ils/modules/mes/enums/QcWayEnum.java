package com.ils.modules.mes.enums;

/**
 * 质检方式
 * @author Anna.
 * @date 2021/5/12 11:23
 */
public enum QcWayEnum {
    /**
     * 全检
     */
    ALL_CHECK("1", "全检"),
    /**
     * 比例抽检
     */
    PROPORTION_CHECK("2", "比例抽检"),
    /**
     * 固定抽检
     */
    FIXED_CHECK("3", "固定抽检"),
    /**
     * 自定义抽检
     */
    CUSTOM_CHECK("4", "自定义抽检");


    private final String value;

    private final String desc;

    /**
     * @param value
     * @param desc
     */
    QcWayEnum(String value, String desc) {
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
