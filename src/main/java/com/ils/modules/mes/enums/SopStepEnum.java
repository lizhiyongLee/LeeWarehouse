package com.ils.modules.mes.enums;

/**
 * sop任务状态
 *
 * @author Anna.
 * @date 2021/7/19 16:14
 */
public enum SopStepEnum {

    /**
     * 未开始
     */
    NEW("1", "未开始"),
    /**
     * 执行中
     */
    IN_PROGRESS("2", "执行中"),
    /**
     * 结束
     */
    COMPLETE("3", "完成");
    private final String value;

    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    SopStepEnum(String value, String desc) {
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
