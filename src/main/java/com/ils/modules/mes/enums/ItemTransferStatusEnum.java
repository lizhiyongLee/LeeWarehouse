package com.ils.modules.mes.enums;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/1/7 11:22
 */
public enum ItemTransferStatusEnum {
    /**
     * out_storaged
     */
    OUT_STORAGED("1", "已出库"),
    /**
     * in_storaged
     */
    IN_STORAGED("2", "已入库");

    private String value;
    private String desc;

    ItemTransferStatusEnum(String value, String desc) {
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
