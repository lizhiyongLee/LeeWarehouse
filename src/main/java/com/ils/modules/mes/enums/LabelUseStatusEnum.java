package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/14 17:04
 */
public enum LabelUseStatusEnum {
    /**
     * 未使用
     */
    UNUSED("1", "未使用"),
    /**
     * 已使用
     */
    USED("2", "已使用"),
    /**
     * 已作废
     */
    CANCEL("3", "已作废");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    LabelUseStatusEnum(String value, String desc) {
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
