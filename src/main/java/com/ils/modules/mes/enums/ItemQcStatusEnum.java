package com.ils.modules.mes.enums;

/**
 * 质量状态枚举类
 *
 * @author Anna.
 * @date 2021/1/7 16:05
 */
public enum ItemQcStatusEnum {
    /**
     * 合格
     */
    QUALIFIED("1", "合格"),
    /**
     * 待检；
     */
    WAIT_TEST("2", "待检"),
    /**
     * 不合格
     */
    UNQUALIFIED("3", "不合格");
    private String value;
    private String desc;

    ItemQcStatusEnum(String value, String desc) {
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
