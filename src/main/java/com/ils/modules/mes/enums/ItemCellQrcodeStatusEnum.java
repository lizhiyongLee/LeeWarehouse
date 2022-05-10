package com.ils.modules.mes.enums;

/**
 * @Description: 物料单元标签状态
 * @author: fengyi
 * @date: 2020年12月15日 下午3:55:38
 */

public enum ItemCellQrcodeStatusEnum {
    /**
     * 厂内
     */
    FACTORY("1", "厂内"),
    /**
     * 已发货
     */
    DELIVERED("2", "已发货"),
    /**
     * 已投产
     */
    OPERATION("3", "已投产"),
    
    /**
     * 已置空
     */
    EMPTY("4", "已置空"),
    /**
     * 已退料
     */
    RETURNMATERIAL("5", "已退料"),
    /**
     * 已报废
     */
    DISCARD("6", "已报废");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ItemCellQrcodeStatusEnum(String value, String desc) {
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
