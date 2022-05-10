package com.ils.modules.mes.enums;

/**
 * 出入库事务全局开关枚举类
 *
 * @author Anna.
 * @date 2021/6/10 15:06
 */
public enum StorageEventRelateSwitchEnum {

    /**
     * 出库时开启
     */
    OPEN_OUT_STORAGE("1", "出库时开启"),
    /**
     * 入库时开启
     */
    OPEN_IN_STORAGE("2", "入库时开启"),
    /**
     * 都不开启
     */
    NO_OPEN("3", "都不开启");


    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    StorageEventRelateSwitchEnum(String value, String desc) {
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
