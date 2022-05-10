package com.ils.modules.mes.enums;

/**
 * @Description: 工单状态枚举类
 * @author: fengyi
 * @date: 2020年11月17日 下午2:40:49
 */

public enum WorkOrderStatusEnum {
    /**
     * 计划
     */
    PLAN("1", "计划"),
    /**
     * 已排程；
     */
    SCHEDULED("2", "已排程"),
    /**
     * 已下发
     */
    ISSUED("3", "已下发"),
    /**
     * 开工
     */
    STARTED("4", "开工"),
    /**
     * 完工
     */
    FINISH("5", "完工"),
    /**
     * 取消
     */
    CANCEL("6", "取消");
    

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkOrderStatusEnum(String value, String desc) {
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
