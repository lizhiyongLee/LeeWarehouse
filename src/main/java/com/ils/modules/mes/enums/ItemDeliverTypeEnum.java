package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/3 19:31
 */
public enum ItemDeliverTypeEnum {

    /**
     * 按销售订单发货
     */
    SALE_ORDER("1", "按销售订单发货"),
    /**
     * 普通发货
     */
    GENERAL ("2", "普通发货"),
    /**
     * 退料发货
     */
    RETURN("3", "退料发货");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ItemDeliverTypeEnum(String value, String desc) {
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
