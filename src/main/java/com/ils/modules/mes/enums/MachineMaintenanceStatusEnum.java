package com.ils.modules.mes.enums;

/**
 * 设备维护状态
 *
 * @author Anna.
 * @date 2021/1/6 14:19
 */
public enum MachineMaintenanceStatusEnum {
    /**
     * not yet start
     */
    NOT_YET_START("1","未开始"),
    /**
     * running
     */
    RUNNING("2","执行中"),
    /**
     * stop
     */
    STOP("3","暂停中"),
    /**
     * ended
     */
    ENDED("4","已结束"),
    /**
     * cancel
     */
    CANCEL("5","已取消");


    private String value;
    private String desc;

    MachineMaintenanceStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
