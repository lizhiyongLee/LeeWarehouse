package com.ils.modules.mes.enums;

/**
 * @Description: 产出记录状态
 * @author: fengyi
 */

public enum ProduceRecordQcStatusEnum {
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

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProduceRecordQcStatusEnum(String value, String desc) {
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
