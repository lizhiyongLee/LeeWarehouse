package com.ils.modules.mes.enums;

/**
 * @Description: 工序的接续方式
 * @author: fengyi
 * @date: 2020年11月30日 下午1:14:36
 */

public enum ProcessLineTypeEnum {
    /**
     * 前续开始后续可开始
     */
    LINK_TYPE_1("1", "前续开始后续可开始"),
    /**
     * 前续结束后续可开始；
     */
    LINK_TYPE_2("2", "前续结束后续可开始");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProcessLineTypeEnum(String value, String desc) {
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
