package com.ils.modules.mes.enums;

/**
 * 报表统计类型枚举类
 *
 * @author fengyi
 * @date 2020/07/14
 */
public enum ReportGroupColumnEnum {
    /**
     * 工单
     */
    GROUP_ORDER("0", "工单"),
    /**
     * 工序
     */
    GROUP_PROCESS("1", "工序"),
    /**
     * 工位
     */
    GROUP_STATION("2", "工位"),
    /**
     * 人员
     */
    GROUP_EMPLOYEE("3", "人员");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    ReportGroupColumnEnum(String value, String desc) {
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
