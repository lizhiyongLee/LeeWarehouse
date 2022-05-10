package com.ils.modules.mes.enums;

/**
 * @Description: 工位排程规则
 * @author: fengyi
 * @date: 2021年2月23日 下午4:18:47
 */

public enum AutoScheStationRuleEnum {
    /**
     * N型排程
     */
    N_RULE("1", "N型排程"),
    /**
     * Z型排程
     */
    Z_RULE("2", "Z型排程");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    AutoScheStationRuleEnum(String value, String desc) {
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
        for (AutoScheStationRuleEnum anEnum : AutoScheStationRuleEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getDesc();
            }
        }

        return "DEFAULT";
    }


}
