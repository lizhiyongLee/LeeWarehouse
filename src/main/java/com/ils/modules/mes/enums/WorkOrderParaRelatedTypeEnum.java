package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/19 11:44
 */
public enum WorkOrderParaRelatedTypeEnum {
    /**
     * 工艺路线
     */
    ROUTE("1", "工艺路线"),
    /**
     * 产品BOM；
     */
    ROUTE_ITEM_BOM("2", "产品BOM");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkOrderParaRelatedTypeEnum(String value, String desc) {
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
