package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/8/4 14:11
 */
public enum WorkOrderLineRelatedTypeEnum {
    /**
     * 工艺路线
     */
    ROUTE("1", "工艺路线"),
    /**
     * 2、产品BOM
     */
    PRODUCT_BOM("2", "产品BOM");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkOrderLineRelatedTypeEnum(String value, String desc) {
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
