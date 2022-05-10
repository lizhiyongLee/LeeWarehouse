package com.ils.modules.mes.enums;

/**
 * 库位类型
 *
 * @author Anna.
 * @date 2021/6/23 16:38
 */
public enum  StorageTypeEnum {


    /**
     * 仓库
     */
    WARE_HOUSE("1", "仓库"),
    /**
     * 一级仓位
     */
    WARE_STORAGE_ONE("2", "一级仓位"),
    /**
     * 二级仓位
     */
    WARE_STORAGE_TWO("3","二级仓位");


    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    StorageTypeEnum(String value, String desc) {
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
