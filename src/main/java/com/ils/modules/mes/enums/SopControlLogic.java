package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/9/17 10:17
 */
public enum SopControlLogic {

    /**
     * 管控
     */
    CONTROL("1", "管控"),
    /**
     * 不管控
     */
    UN_CONTROL("2", "不管控");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    SopControlLogic(String value, String desc) {
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
