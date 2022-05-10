package com.ils.modules.mes.enums;

/**
 * @Description: 标准产能类型
 * @author: fengyi
 * @date: 2021年2月3日 下午4:34:48
 */

public enum VolumeTypeEnum {
    /**
     * 产能
     */
    volume("1", "产能"),
    /**
     * 生产节拍
     */
    per("2", "生产节拍");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    VolumeTypeEnum(String value, String desc) {
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
