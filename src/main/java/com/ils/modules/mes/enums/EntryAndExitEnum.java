package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/9 13:41
 */
public enum EntryAndExitEnum {
    /**
     * 收货
     */
    ENTRY("0","收货"),
    /**
     * 发货
     */
    EXIT("1","发货");

    private final String value;

    private final String desc;

    private EntryAndExitEnum(String value, String desc) {
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
