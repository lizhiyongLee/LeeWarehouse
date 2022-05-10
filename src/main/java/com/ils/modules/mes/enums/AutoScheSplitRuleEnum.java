package com.ils.modules.mes.enums;

/**
 * @Description: 自动排程规则
 * @author: fengyi
 * @date: 2021年2月23日 下午4:56:29
 */

public enum AutoScheSplitRuleEnum {
    /**
     * 按班次拆分
     */
    SHIFT_SPLIT_RULE("1", "按班次拆分"),
    /**
     * 按设置数量拆分
     */
    SUM_SPLIT_RULE("2", "按设置数量拆分"),
    /**
    * 无拆分
    */
    NO_SPLIT_RULE("3", "无拆分");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    AutoScheSplitRuleEnum(String value, String desc) {
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
        for (AutoScheSplitRuleEnum anEnum : AutoScheSplitRuleEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getDesc();
            }
        }

        return "DEFAULT";
    }

}
