package com.ils.modules.mes.enums;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 * @date: 2020年12月17日 下午3:16:34
 */

public enum MaterialRecordFeedTypeEnum {
    /**
     * 投料
     */
    FEED("1", "投料"),
    /**
     * 撤料
     */
    UNDO("2", "撤料");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    MaterialRecordFeedTypeEnum(String value, String desc) {
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
