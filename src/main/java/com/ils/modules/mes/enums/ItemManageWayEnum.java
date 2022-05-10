package com.ils.modules.mes.enums;

/**
 * @Description: 物料单元管理方式
 * @author: fengyi
 * @date: 2020年12月14日 上午10:50:27
 */
public enum ItemManageWayEnum {

    /**
     * 标签管理
     */
    QRCODE_MANAGE("1", "标签管理"),
    /**
     * 批次管理
     */
    BATCH_MANAGE("2", "批次管理"),
    /**
     * 无码管理
     */
    NONE_MANAGE("3", "无码管理");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ItemManageWayEnum(String value, String desc) {
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
