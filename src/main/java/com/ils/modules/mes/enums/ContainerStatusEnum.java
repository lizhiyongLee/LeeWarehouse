package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description 1、装载；2、卸载。
 * @date 2021/10/26 16:34
 */
public enum ContainerStatusEnum {
    /**
     * 空载
     */
    EMPTY("1", "空载"),
    /**
     * 满载
     */
    FULL("2", "满载"),
    /**
     * 可载
     */
    AVAILABLE("3", "可载");

    private final String value;

    private final String desc;

    private ContainerStatusEnum(String value, String desc) {
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
