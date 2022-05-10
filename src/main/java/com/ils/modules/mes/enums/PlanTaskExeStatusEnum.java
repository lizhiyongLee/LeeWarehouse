package com.ils.modules.mes.enums;

/**
 * @Description: 派工任务执行状态
 * @author: fengyi
 * @date: 2020年11月24日 下午5:25:51
 */

public enum PlanTaskExeStatusEnum {
    /**
     * 未开始
     */
    NOT_START("1", "未开始"),
    /**
     * 生产
     */
    PRODUCE("2", "生产"),
    /**
     * 暂停
     */
 SUSPEND("3", "暂停"),
    /**
     * 结束
     */
    FINISH("4", "结束"),
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
    PlanTaskExeStatusEnum(String value, String desc) {
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
