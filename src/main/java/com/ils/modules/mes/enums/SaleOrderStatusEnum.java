package com.ils.modules.mes.enums;

/**
 * @Description: 销售订单状态
 * @author: fengyi
 * @date: 2021年1月20日 上午10:40:02
 */

public enum SaleOrderStatusEnum {
    /**
     * 新建
     */
    NEW("1", "新建"),
    /**
     * 完成
     */
    COMPLETE("2", "完成"),
    /**
     * 结束
     */
    FINISH("3", "结束");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    SaleOrderStatusEnum(String value, String desc) {
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
