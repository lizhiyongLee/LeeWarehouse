package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 11:29
 */
public enum NodeTypeEnum {
    /**
     * 工厂
     */
    FACTORY("1", "工厂"),
    /**
     * 车间
     */
    SHOP("2", "车间"),
    /**
     * 产线
     */
    LINE("3", "产线"),
    /**
     * 工位
     */
    WORKSTATION("4", "工位");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    NodeTypeEnum(String value, String desc) {
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
