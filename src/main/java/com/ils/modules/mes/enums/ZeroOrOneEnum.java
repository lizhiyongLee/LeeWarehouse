package com.ils.modules.mes.enums;

/**
 * @Description:0,1标记枚举类
 * @Author: fengyi
 * @Date:   2020-06-18
 * @Version: V1.0
 */
public enum ZeroOrOneEnum {
    /**
     * 0标记
     */
    ZERO(0,"0","0标记"),
    /**
     * 1标记
     */
    ONE(1,"1","1标记");

    Integer icode;
    /** 编码 */
    String strCode;
    /** 名称 */
    String name;

    private ZeroOrOneEnum(Integer icode,String strCode, String name) {

        this.icode = icode;
        
        this.strCode = strCode;

        this.name = name;
    }

    public Integer getIcode() {
        return icode;
    }

    public void setIcode(Integer icode) {
        this.icode = icode;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    

}
