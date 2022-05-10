package com.ils.modules.mes.enums;

/**
 * @Description: 物料单元业务状态
 * @author: fengyi
 * @date: 2020年12月15日 下午3:56:42
 */

public enum ItemCellBusinessStatusEnum {
    /**
     * 质检中
     */
    QCING("1", "质检中"),
    /**
     * 盘点中
     */
    CHECKING("2", "盘点中"),
    /**
     * 无业务
     */
    NONE("3", "无业务"),
    /**
     * 生产中
     */
    PRODUCEING("4", "生产中"),
    /**
     * 生产完
     */
    PRODUCED("5", "生产完");
    
    
    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ItemCellBusinessStatusEnum(String value, String desc) {
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
