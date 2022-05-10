package com.ils.modules.mes.enums;

/**
 * 移动事务对象枚举类
 *
 * @author Anna.
 * @date 2021/6/3 13:43
 */
public enum EventObjectEnum {
    /**
     *工单
     */
    ORDER("1", "工单"),
    /**
     * 销售订单
     */
    SALE_ORDER("2", "销售订单");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    EventObjectEnum(String value, String desc) {
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
