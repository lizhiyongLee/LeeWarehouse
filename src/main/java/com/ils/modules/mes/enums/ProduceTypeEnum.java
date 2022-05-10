package com.ils.modules.mes.enums;

/**
 * @Description: 工单生产类型
 * @author: fengyi
 * @date: 2021年1月25日 下午3:40:11
 */

public enum ProduceTypeEnum {
    /**
     * 库存
     */
    PRODUCE_STORE("1", "库存"),
    /**
     * 销售
     */
    PRODUCE_SALE("2", "销售");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProduceTypeEnum(String value, String desc) {
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
