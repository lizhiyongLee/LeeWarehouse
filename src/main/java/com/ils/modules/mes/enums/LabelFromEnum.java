package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 11:14
 */
public enum LabelFromEnum {
    /**
     * 手动创建
     */
    MANUAL("1", "手工创建"),
    /**
     * 导入
     */
    IMPORT("2", "导入"),
    /**
     * 其他
     */
    OTHER("3", "其他");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    LabelFromEnum(String value, String desc) {
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
