package com.ils.modules.mes.enums;

import com.ils.modules.mes.constants.MesCommonConstant;

import java.util.HashMap;

/**
 * sop控制类型类型枚举类
 *
 * @author Anna.
 * @date 2021/7/15 15:22
 */
public enum SopControlTypeEnum {

    /**
     * 标签码入库
     */
    LABEL_IN("1", "标签码入库", new HashMap<>()),
    /**
     * 库存入库
     */
    STORAGE_IN("2", "库存入库", new HashMap<>()),
    /**
     * 投料
     */
    FEED("3", "投料", new HashMap<>()),
    /**
     * 产出
     */
    OUTPUT("4", "产出", new HashMap<>()),
    /**
     * 质检
     */
    QC("5", "质检", new HashMap<>()),
    /**
     * 标签出库
     */
    LABEL_OUT("6", "标签出库", new HashMap<>()),
    /**
     * 库存出库
     */
    STORAGE_OUT("7", "库存出库", new HashMap<>()),
    /**
     * 报告模板
     */
    REPORT_TEMPLATE("8", "报告模板", new HashMap<>()),
    /**
     * 联产品
     */
    JOINT_PRODUCT("9", "联产品", new HashMap<>()),
    /**
     * 参数模板
     */
    PARA_TEMPLATE("10", "参数模板", new HashMap<>());



    static {
        SopControlTypeEnum.valueOf("LABEL_IN").getControlLogicMap().putAll(MesCommonConstant.LABEL_IN);
        SopControlTypeEnum.valueOf("STORAGE_IN").getControlLogicMap().putAll(MesCommonConstant.STORAGE_IN);
        SopControlTypeEnum.valueOf("FEED").getControlLogicMap().putAll(MesCommonConstant.FEED);
        SopControlTypeEnum.valueOf("LABEL_OUT").getControlLogicMap().putAll(MesCommonConstant.LABEL_OUT);
        SopControlTypeEnum.valueOf("OUTPUT").getControlLogicMap().putAll(MesCommonConstant.OUTPUT);
        SopControlTypeEnum.valueOf("QC").getControlLogicMap().putAll(MesCommonConstant.QC);
        SopControlTypeEnum.valueOf("STORAGE_OUT").getControlLogicMap().putAll(MesCommonConstant.STORAGE_OUT);
        SopControlTypeEnum.valueOf("REPORT_TEMPLATE").getControlLogicMap().putAll(MesCommonConstant.REPORT_TEMPLATE);
        SopControlTypeEnum.valueOf("JOINT_PRODUCT").getControlLogicMap().putAll(MesCommonConstant.JOINT_PRODUCT);
        SopControlTypeEnum.valueOf("PARA_TEMPLATE").getControlLogicMap().putAll(MesCommonConstant.PARA_TEMPLATE);
    }

    /**
     * @param value
     * @param desc
     */
    private final String value;

    private final String desc;

    private final HashMap<String, String> controlLogicMap;

    /**
     * @param value
     * @param desc
     * @param controlLogicMap
     */
    SopControlTypeEnum(String value, String desc, HashMap<String, String> controlLogicMap) {
        this.value = value;
        this.desc = desc;
        this.controlLogicMap = controlLogicMap;
    }

    public static SopControlTypeEnum match(String value) {
        for (SopControlTypeEnum sopControlTypeEnum : SopControlTypeEnum.values()) {
            if (sopControlTypeEnum.getValue().equals(value)) {
                return sopControlTypeEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public HashMap<String, String> getControlLogicMap() {
        return controlLogicMap;
    }
}
