package com.ils.modules.mes.enums;

/**
 * @Description: 工厂区域类型定义
 * @author: fengyi
 * @date: 2020年10月15日 上午11:41:34
 */

public enum FactoryAreaTypeEnum {
    /**
     * 工厂
     */
    FACTORY("1", "工厂"),
    /**
     * 车间
     */
    WORK_SHOP("2", "车间"),
    /**
     * 产线
     */
    WORK_LINE("3", "产线"),
    /**
     * 工位
     */
    WORKSTATION("4", "工位");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    FactoryAreaTypeEnum(String value, String desc) {
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
