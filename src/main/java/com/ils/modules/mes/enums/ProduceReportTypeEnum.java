package com.ils.modules.mes.enums;

/**
 * @Description: 报工类型
 * @author: fengyi
 */

public enum ProduceReportTypeEnum {
    /**
     * 自动报工
     */
    AUTO_REPORT("1", "自动报工"),
    /**
     * 手工报工
     */
    MAN_REPORT("2", "手工报工"),
    /**
     * 有生成任务报工
     */
    PRODUCE_REPORT("3", "有生成任务报工"),
    /**
     * 其他形式报工
     */
    OTHER("4", "其他形式报工"),
    /**
     * 取消
     */
    CANCEL("5", "取消");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    ProduceReportTypeEnum(String value, String desc) {
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
