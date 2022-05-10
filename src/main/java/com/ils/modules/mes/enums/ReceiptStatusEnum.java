package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description 1、新建；2、已打印；3、已使用；4、完结。
 * @date 2021/7/13 11:29
 */
public enum ReceiptStatusEnum {
    /**
     * 新建
     */
    NEW("1", "新建"),
    /**
     * 已打印
     */
    PRINTED("2", "已打印"),
    /**
     * 已使用
     */
    USED("3", "已使用"),
    /**
     * 完结
     */
    END("4", "完结");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    ReceiptStatusEnum(String value, String desc) {
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
