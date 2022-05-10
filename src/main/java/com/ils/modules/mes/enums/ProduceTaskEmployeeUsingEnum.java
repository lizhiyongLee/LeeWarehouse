package com.ils.modules.mes.enums;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 * @date: 2020年12月9日 上午11:18:31
 */

public enum ProduceTaskEmployeeUsingEnum {
    /**
     * 指派领取
     */
    ASSIGN("1", "指派领取"),
    /**
     * 自己领取
     */
    SELF("2", "自己领取"),
    /**
     * 交接
     */
    SUSPEND("3", "交接");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProduceTaskEmployeeUsingEnum(String value, String desc) {
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
