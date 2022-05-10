package com.ils.modules.mes.enums;

/**
 * @Description: 工单工艺类型
 * @author: fengyi
 * @date: 2020年11月12日 下午2:14:08
 */

public enum WorkflowTypeEnum {
    /**
     * 工艺路线
     */
    ROUTE("1", "route", "工艺路线"),
    /**
     * 工艺路线+物料清单；
     */
    ROUTE_ITEM_BOM("2","bom",  "工艺路线+物料清单"),
    /**
     * 3、产品BOM
     */
    PRODUCT_BOM("3", "route_bom", "产品BOM");

    private final String value;
    private final String code;
    private final String desc;

    /**
     *
     * @param value
     * @param desc
     */
    WorkflowTypeEnum(String value, String code, String desc) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }

    public static WorkflowTypeEnum matchWorkflowType(String routeType) {

        for (WorkflowTypeEnum value : WorkflowTypeEnum.values()) {

            if (value.getCode().equals(routeType)) {
                return value;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static WorkflowTypeEnum matchCode(String workflowType) {
        for (WorkflowTypeEnum value : WorkflowTypeEnum.values()) {

            if (value.getValue().equals(workflowType)) {
                return value;
            }
        }
        return null;
    }
}
