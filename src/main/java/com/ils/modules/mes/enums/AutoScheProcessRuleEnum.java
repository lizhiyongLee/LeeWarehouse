package com.ils.modules.mes.enums;

/**
 * @Description: 自动排程规则
 * @author: fengyi
 * @date: 2021年2月23日 下午4:34:04
 */

public enum AutoScheProcessRuleEnum {
    /**
     * 常规方案
     */
    COMMON_RULE("1", "常规方案"),
    /**
     * 最小换型时间
     */
    MIN_TIME_SWITCH_RULE("2", "最小换型时间");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    AutoScheProcessRuleEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByValue(String value) {
        for (AutoScheProcessRuleEnum anEnum : AutoScheProcessRuleEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getDesc();
            }
        }

        return "DEFAULT";
    }


}
