package com.ils.modules.mes.enums;

/**
 * 流程审批状态枚举类
 * 
 * @author fengyi
 * @date 2020/07/14
 */
public enum AuditStatusEnum {
    /**
     * 未审批
     */
    AUDIT_NEW("0", "未审批"), 
    /**
     * 审批中
     */
    AUDIT_DOING("1", "审批中"), 
    /**
     * 审批通过
     */
    AUDIT_FINISH("2", "审批通过"), 
    /**
     * 审批拒绝
     */
    AUDIT_REJECT("3", "审批拒绝");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    AuditStatusEnum(String value, String desc) {
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
