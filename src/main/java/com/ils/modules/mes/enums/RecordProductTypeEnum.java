package com.ils.modules.mes.enums;

/**
 * 流程审批状态枚举类
 * 
 * @author fengyi
 * @date 2020/07/14
 */
public enum RecordProductTypeEnum {

    /**
     * 审批中
     */
    GENERAL_PRODUCT("1", "普通产出"),
    /**
     * 审批通过
     */
    JOINT_PRODUCT("2", "联产出");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    RecordProductTypeEnum(String value, String desc) {
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
