package com.ils.modules.mes.enums;

/**
 * 质检任务状态枚举类
 *
 * @author Anna.
 * @date 2021/5/10 17:08
 */
public enum QcTaskExeStatusEnum {

    /**
     *
     */
    NOT_START("1", "未开始"),
    /**
     *
     */
    EXE_QC_TASK("2", "质检中"),
    /**
     * 结束
     */
    FINISH("3", "结束"),
    /**
     * 取消
     */
    CANCEL("4", "取消");
    private final String value;

    private final String desc;

    /**
     * @param value
     * @param desc
     */
    QcTaskExeStatusEnum(String value, String desc) {
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
