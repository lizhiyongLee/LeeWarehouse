package com.ils.modules.mes.enums;

/**
 * 质检任务检查类型
 *
 * @author Anna.
 * @date 2021/5/18 14:20
 */
public enum QcTaskQcTypeEnum {

    /**
     * 入厂检
     */
    ADMISSION_CHECK("1", "入厂检"),
    /**
     * 出厂检
     */
    LEAVE_FACTORY_CHECK("2", "出厂检"),
    /**
     * 首检
     */
    FIRST_CHECK("3", "首检"),
    /**
     * 生产检
     */
    PRODUCTION_CHECK("4", "生产检"),
    /**
     * 巡检
     */
    TOUR_CHECK("5", "巡检"),
    /**
     * 普通检
     */
    NORMAL_CHECK("6", "普通检");

    private final String value;

    private final String desc;

    /**
     * @param value
     * @param desc
     */
    QcTaskQcTypeEnum(String value, String desc) {
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
