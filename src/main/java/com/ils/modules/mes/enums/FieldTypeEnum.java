package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/2 10:35
 */
public enum FieldTypeEnum {
    /**
     * 字符串类型
     */
    STRING("String", "字符串"),
    /**
     * 大数字类型
     */
    BIG_DECIMAL("BigDecimal", "大数字"),
    /**
     * 整数类型
     */
    INTEGER("Integer", "整数"),
    /**
     * 长整型
     */
    LONG("Long", "长整型"),
    /**
     * 日期
     */
    DATE("Date", "日期");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    FieldTypeEnum(String value, String desc) {
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
