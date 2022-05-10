package com.ils.modules.mes.enums;

/**
 * @Description:工单批号生成类型
 * @author: fengyi
 * @date: 2021年1月26日 下午2:41:48
 */

public enum WorkOrderBatchTypeEnum {
    /**
     * 手动生成
     */
    customer("1", "手动生成"),
    
    /**
     * 规则生成
     */
    RULE("2", "规则生成");

    private final String value;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkOrderBatchTypeEnum(String value, String desc) {
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
