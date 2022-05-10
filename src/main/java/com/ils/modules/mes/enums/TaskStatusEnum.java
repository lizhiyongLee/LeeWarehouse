package com.ils.modules.mes.enums;

/**
 * @author lishaojie
 * @description 1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；
 * @date 2021/7/14 17:04
 */
public enum TaskStatusEnum {
    /**
     * 未开始
     */
    NOT_STARTED("1", "未开始"),
    /**
     * 已使用
     */
    EXECUTING("2", "执行中"),
    /**
     * 已暂停
     */
    PAUSED("3", "已暂停"),
    /**
     * 已结束
     */
    COMPLETE("4", "已结束"),
    /**
     * 已取消
     */
    CANCEL("5", "已取消"),
    /**
     * 验收
     */
    ACCEPTED("6", "验收");

    private final String value;
    private final String desc;

    /**
     * @param value
     * @param desc
     */
    TaskStatusEnum(String value, String desc) {
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
