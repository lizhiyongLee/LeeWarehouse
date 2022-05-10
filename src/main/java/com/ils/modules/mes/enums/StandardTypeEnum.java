package com.ils.modules.mes.enums;

/**
 * @Description: 标准产能工序来源
 * @author: fengyi
 * @date: 2021年2月2日 上午11:47:24
 */

public enum StandardTypeEnum {
    /** 生产 */
    PROCESS("1", "工序"),
    /** 设备 */
    ROUTE("2", "工艺路线"),
    /** 质量 */
    PRODUCT_BOM("3", "产品BOM");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    StandardTypeEnum(String value, String desc) {
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
