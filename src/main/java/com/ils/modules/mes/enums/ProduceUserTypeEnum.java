package com.ils.modules.mes.enums;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 */

public enum ProduceUserTypeEnum {
    /**
     * 任务排他
     */
    SHIFT("1", "指定班组"),
    /**
     * 任务共接；
     */
    EMPLOYEE("2", "指定用户");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProduceUserTypeEnum(String value, String desc) {
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
