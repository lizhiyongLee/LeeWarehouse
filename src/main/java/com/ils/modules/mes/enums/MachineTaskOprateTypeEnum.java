package com.ils.modules.mes.enums;

/**
 * 设备任务操作类型
 *
 * @author Anna.
 * @date 2021/1/6 13:38
 */
public enum MachineTaskOprateTypeEnum {
    /**
     * Create a task
     */
    CREATE_TASK("1","创建任务"),
    /**
     * Get the task
     */
    GET_TASK("2","执行任务"),
    /**
     * Suspended task
     */
    SUSPENDED_TASK("3","暂停任务"),
    /**
     * The restore task
     */
    RESTORE_TASK("4","恢复任务"),
    /**
     * end task
     */
    END_TASK("5","结束任务"),
    /**
     * Cancel the task
     */
    CANCEL_TASK("6","取消任务");

    private String value;
    private String desc;

    MachineTaskOprateTypeEnum(String value, String desc) {
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
