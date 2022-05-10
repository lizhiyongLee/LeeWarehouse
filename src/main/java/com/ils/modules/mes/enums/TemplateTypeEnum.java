package com.ils.modules.mes.enums;

/**
 * sop模板实体类型
 *
 * @author Anna.
 * @date 2021/7/19 10:40
 */
public enum TemplateTypeEnum {
    /**
     * 工艺路线
     */
    ROUTE("2", "工艺路线"),
    /**
     * 产品BOM
     */
    PRODUCT_BOM("1", "产品BOM");

    private final String value;

    private final String desc;

    /**
     * @param value
     * @param desc
     */
    TemplateTypeEnum(String value, String desc) {
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
