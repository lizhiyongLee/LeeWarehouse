package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/22 15:46
 */
public enum RelatedTaskTypeEnum {
    /**
     * 生产任务
     */
    PRODUCE("1", "生产任务"),
    /**
     * 质检任务
     */
    QC("2", "质检任务"),
    /**
     * 维保任务
     */
    MAINTENANCE("3", "维保任务");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    RelatedTaskTypeEnum(String value, String desc) {
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
