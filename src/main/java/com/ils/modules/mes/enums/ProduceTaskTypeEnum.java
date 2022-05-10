package com.ils.modules.mes.enums;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 * @date: 2020年12月8日 下午3:41:35
 */

public enum ProduceTaskTypeEnum {
    /**
     * 任务排他
     */
    EXCLUDE("1", "任务排他"),
    /**
     * 任务共接；
     */
    TOGETHER("2", "任务共接");

    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProduceTaskTypeEnum(String value, String desc) {
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
