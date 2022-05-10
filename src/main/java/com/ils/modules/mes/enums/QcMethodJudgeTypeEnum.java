package com.ils.modules.mes.enums;

/**
 * 质检任务检查类型
 *
 * @author Anna.
 * @date 2021/5/18 14:20
 */
public enum QcMethodJudgeTypeEnum {

    /**
     * 判定单次样本
     */
    JUDGE_BY_SAMPLE("1", "判定单次样本"),
    /**
     * 判断整个批次
     */
    JUDGE_BY_BATCH("2", "判断整个批次"),
    /**
     * 不更新物料状态
     */
    NOT_UPDATA_QCSTATUS("3", "不更新物料状态");

    private final String value;

    private final String desc;

    /**
     * @param value
     * @param desc
     */
    QcMethodJudgeTypeEnum(String value, String desc) {
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
