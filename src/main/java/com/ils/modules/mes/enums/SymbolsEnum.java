package com.ils.modules.mes.enums;

/**
 * 符号枚举类
 *
 * @author niushuai
 * @date 2021/10/26 15:57:42
 */
public enum SymbolsEnum {

    /**
     * 逗号
     */
    COMMA(",", "逗号"),

    /**
     * 分号
     */
    SEMICOLON(";", "分号"),

    /**
     * 点
     */
    POINT(".", "点"),

    /**
     * 左括号
     */
    BRACKETS_LEFT("(", "左括号"),

    /**
     * 右括号
     */
    BRACKETS_RIGHT(")", "右括号"),

    /**
     * 空串
     */
    EMPTY_STRING("", "空串"),

    /**
     * 下划线
     */
    UNDER_LINE("_", "下划线"),

    /**
     * 单引号
     */
    SINGLE_LINK("'", "单引号"),

    /**
     * 双引号
     */
    DOUBLE_LINK("\"", "双引号"),

    /**
     * @ 符号
     */
    AT("@", "@符号"),

    /**
     * 三个@符号
     */
    AT_3("@@@", "三个@符号"),
    /**
     * 空格
     */
    SPACE(" ", "空格");


    private String symbol;
    private String desc;

    SymbolsEnum(String symbol, String desc) {
        this.symbol = symbol;
        this.desc = desc;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
