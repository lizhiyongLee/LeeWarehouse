package com.ils.modules.mes.enums;

/**
 * 设备任务类型
 *
 * @author Anna.
 * @date 2021/1/6 13:25
 */
public enum MachineTaskTypeEnum {
    /**
     * repair tasks
     */
    REPAIR_TASKS("1","维修任务"),
    /**
     * Maintenance tasks
     */
    MAINTENANCE_TASKS("2","维保任务"),
    /**
     * Check the task 点检
     */
    CHECK_TASKS("3","点检任务");


    private String value;
    private String desc;

    MachineTaskTypeEnum(String value, String desc) {
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
