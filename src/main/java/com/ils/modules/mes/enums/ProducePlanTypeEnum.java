package com.ils.modules.mes.enums;

/**
 * @Description: 排程方法
 * @author: fengyi
 */

public enum ProducePlanTypeEnum {
    /**
     * 库存
     */
    TIME("1", "时间"),
    /**
     * 销售
     */
    SHIFT("2", "班次");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProducePlanTypeEnum(String value, String desc) {
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
