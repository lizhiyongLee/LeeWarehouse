package com.ils.modules.mes.enums;

/**
 * @Description: 自动排程订单规则
 * @author: fengyi
 * @date: 2021年2月23日 下午4:47:04
 */
public enum AutoScheOrderRuleEnum {
    /**
     * 按工单优先级降序
     */
    PRIORITY_RULE("1", "按工单优先级降序"),
    /**
     * 按工单计划时间升序
     */
    STARTTIME_ASE_RULE("2", "按工单计划时间升序"),
    /**
     * 按工单计划结束时间升序
     */
    ENDTIME_ASE_RULE("3", "按工单计划结束时间升序");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    AutoScheOrderRuleEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AutoScheOrderRuleEnum getRuleEnum(String value) {
        AutoScheOrderRuleEnum[] rules = AutoScheOrderRuleEnum.values();
        for (AutoScheOrderRuleEnum rule : rules) {
            if (rule.getValue().equals(value)) {
                return rule;
            }
        }
        return null;
    }

    public static String getDescByValue(String value) {
        for (AutoScheOrderRuleEnum anEnum : AutoScheOrderRuleEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getDesc();
            }
        }

        return "DEFAULT";
    }


}
