package com.ils.modules.mes.enums;

/**
 * 物料单元类型
 *
 * @author Anna.
 * @date 2021/1/6 13:03
 */

public enum ItemTypeEunm {
    /**
     * 库存品
     */
    STORAGE("1","库存品"),
    /**
     * In the products
     */
    IN_THER_PRODUCTS("2","在制品"),
    /**
     * Completion of unwarehoused goods
     */
    COMPLETION_OF_UNWAREHOUSED_GOODS("3","完工未入库品");



    private final String value;
    private final String desc;
    /**
     *
     * @param value
     * @param desc
     */
    ItemTypeEunm(String value, String desc) {
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
