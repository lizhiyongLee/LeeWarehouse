package com.ils.modules.mes.enums;

/**
 * @Description: 派工任务排程状态
 * @author: fengyi
 * @date: 2020年11月24日 下午5:22:44
 */

public enum PlanTaskStatusEnum {
    /**
     * 已排程
     */
    SCHEDULED("1", "已排程"),
    /**
     * 已下发
     */
    ISSUED("2", "已下发");
   

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    PlanTaskStatusEnum(String value, String desc) {
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