package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description 1、装载；2、卸载。
 * @date 2021/10/26 16:34
 */
public enum ContainerLoadTypeEnum {
    /**
     * 收货
     */
    LOAD("1","装载"),
    /**
     * 发货
     */
    UNLOAD("2","卸载");

    private final String value;

    private final String desc;

    private ContainerLoadTypeEnum(String value, String desc) {
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
