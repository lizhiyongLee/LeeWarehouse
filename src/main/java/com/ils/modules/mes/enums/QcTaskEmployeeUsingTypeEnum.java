package com.ils.modules.mes.enums;

/**
 *  任务领取方式
 *
 * @author Anna.
 * @date 2021/5/12 11:42
 */
public enum QcTaskEmployeeUsingTypeEnum {

    /**
     * 指派领取
     */
    ASSIGNED_RECEIVE("1", "指派领取"),
    /**
     * 自己领取
     */
    SELF_RECEIVE("2", "自己领取"),
    /**
     * 交接
     */
    HANDOVER("3", "交接"),
    /**
     * 初始
     */
    DEFAULT("0", "初始");


    private final String value;

    private final String desc;

    /**
     * @param value
     * @param desc
     */
    QcTaskEmployeeUsingTypeEnum(String value, String desc) {
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
