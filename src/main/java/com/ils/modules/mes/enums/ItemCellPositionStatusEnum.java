package com.ils.modules.mes.enums;

/**
 * @Description: 物料单位位置状态
 * @author: fengyi
 * @date: 2020年12月15日 下午3:54:58
 */

public enum ItemCellPositionStatusEnum {
    /**
     * 仓储中
     */
    STORAGE("1", "仓储中"),
    /**
     * 转运中
     */
    TRANSPORT("2", "转运中"),
    /**
     * 在制
     */
    MAKE("3", "在制");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ItemCellPositionStatusEnum(String value, String desc) {
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
