package com.ils.modules.mes.enums;

/**
 * 策略规则
 *
 * @author Anna.
 * @date 2021/2/4 11:44
 */
public enum MachinePolicyRuleEnum {

    /**
     * 固定周期 fixed cycle
     */
    FIXED_CYCLE("1","固定周期"),
    /**
     * 浮动周期 Floating cycle
     */
    FLOATING_CYCLE("2","浮动周期"),
    /**
     * 累计用度 accumulate
     */
    THE_ACCUMULATE("3","累计用度"),
    /**
     * 固定用度
     */
    THE_FIXED("4","固定用度"),
    /**
     * 手工创建 manual
     */
    CRETE_BY_MANUAL("5","手工创建");

    private String value;
    private String desc;

    MachinePolicyRuleEnum(String value, String desc) {
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
